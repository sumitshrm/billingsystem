package com.org.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.org.constants.Worksheets;
import com.org.domain.Config;
import com.org.entity.Aggreement;
import com.org.entity.Document;
import com.org.entity.IDocument;
import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import com.org.entity.MeasurementSheet;
import com.org.entity.Template;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.ItemRanges;
import com.org.excel.util.TemplateType;
import com.org.excel.util.XLColumnRange;
import com.org.report.service.AbstractGeneratorService;
import com.org.report.service.AbstractGeneratorServiceV2;
import com.org.report.service.DeviationGeneratorService;
import com.org.report.service.ExtraItemStatementGeneratorService;
import com.org.report.service.ItemsGeneratorService;
import com.org.report.service.MeasurementGeneratorService;
import com.org.report.service.PartRateStatementGeneratorService;
import com.org.report.service.ScheduleGeneratorService;
import com.org.service.blobstore.FileStorageService;

@Service
public class DocumentService {
	
	final static Logger logger = Logger.getLogger(DocumentService.class);
	
	@Autowired 
	private AbstractGeneratorService abstractGeneratorService;
	
	@Autowired 
	private AbstractGeneratorServiceV2 abstractGeneratorServiceV2;
	
	@Autowired
	private MeasurementGeneratorService measurementGeneratorService;
	
	@Autowired
	private ScheduleGeneratorService scheduleService;
	
	@Autowired
	private DeviationGeneratorService deviationService;
	
	@Autowired
	private ItemsGeneratorService itemsGeneratorService;
	
	@Autowired
	private ExtraItemStatementGeneratorService extraItemGeneratorService;
	
    @Autowired
    private PartRateStatementGeneratorService partRateStatementGeneratorService;
    
    @Autowired
    private FileStorageService fileStorageService;
	
    @Deprecated
	public Document createDefaultDocument(String filename, MeasurementSheet msheet) throws Exception{
		String msheetUrl = System.getenv("OPENSHIFT_DATA_DIR")+"\\"+"MSHEET_"+msheet.getId()+"_"+msheet.getAggreement().getId()+".xlsm";
		String tempUrl = this.getTemplateUrl(msheet.getTemplateVersion());
		Document newDoc = new Document();
		newDoc.setFilename(filename+".xlsm");
		newDoc.setUrl(msheetUrl);
		msheet.setDocument(newDoc);
		MeasurementSheet prevMsheet = msheet.getPreviousMeasurementSheet();
		//Copy from previous measurement sheet if selected by user.
		File templateFile = msheet.isCopyPreviousMeasurement()
				&& prevMsheet != null ? prevMsheet.getDocument().getExcelFile()
				: new File(tempUrl);
		File msheetFile = new File(msheetUrl);
		msheet.setExcelFile(msheetFile);
		copyFileUsingChannel(templateFile, msheetFile);
		XSSFWorkbook workbook = null;
		//msheet.getDocument().getWorkbook();
		measurementGeneratorService.generateMasterData(msheet, workbook);
		if (msheet.isCopyPreviousMeasurement() && prevMsheet != null) {
			abstractGeneratorService.removeReportData(msheet, workbook);
			scheduleService.removeReportData(msheet, workbook);
			deviationService.removeReportData(msheet, workbook);
			itemsGeneratorService.removeExtraItemsData(workbook);
		}
		if(msheet.getTemplateVersion()>0){
			partRateStatementGeneratorService.generateMasterData(msheet, workbook);
			extraItemGeneratorService.generateMasterData(msheet, workbook);
		}
		if(!msheet.isUserManaged()){
			abstractGeneratorService.generateMasterData(msheet, workbook);
			scheduleService.generateMasterData(msheet, workbook);
			deviationService.generateMasterData(msheet, workbook);
			itemsGeneratorService.writeItems(msheet.getAggreement().getItems(), workbook, msheet, true);
		}else{
			workbook.setActiveSheet(workbook.getSheetIndex(Worksheets.EXTRA_ITEMS_SHEET));
			workbook.setSheetHidden(workbook.getSheetIndex(Worksheets.MEASUREMENTSHEET), true);
			workbook.setSheetHidden(workbook.getSheetIndex(Worksheets.ABSTRACTSHEET), true);
			workbook.setSheetHidden(workbook.getSheetIndex(Worksheets.DEVIATIONSHEET), true);
			workbook.setSheetHidden(workbook.getSheetIndex(Worksheets.RB_SCHEDULE), true);
			workbook.setSheetHidden(workbook.getSheetIndex(Worksheets.FNFB_SCHEDULE), true);
			workbook.setSheetHidden(workbook.getSheetIndex(Worksheets.TEMPSHEET), true);
		}
		//msheet.getDocument().save();
		return newDoc;
	}
	
	
	public Document createDefaultDocument(MeasurementSheet msheet) throws Exception {
		String tempUrl = this.getTemplateUrl(msheet.getTemplateVersion());
		Document newDoc = new Document();
		newDoc.setFilename(msheet.getDocumentFileName()+".xlsm");
		newDoc.setUrl("test");
		msheet.setDocument(newDoc);
		MeasurementSheet prevMsheet = msheet.getPreviousMeasurementSheet();
		//Copy from previous measurement sheet if selected by user.
		InputStream msheetFileStream = null;
		if(msheet.isCopyPreviousMeasurement() && prevMsheet != null) {
			msheetFileStream = fileStorageService.doGet(prevMsheet.getStorageFileName());
		}else {
			msheetFileStream = new FileInputStream(new File(tempUrl));
		}
		XSSFWorkbook workbook = new XSSFWorkbook(msheetFileStream);
		measurementGeneratorService.generateMasterData(msheet, workbook);
		if (msheet.isCopyPreviousMeasurement() && prevMsheet != null) {
			abstractGeneratorService.removeReportData(msheet, workbook);
			scheduleService.removeReportData(msheet, workbook);
			deviationService.removeReportData(msheet, workbook);
			itemsGeneratorService.removeExtraItemsData(workbook);
		}
		if(msheet.getTemplateVersion()>0){
			partRateStatementGeneratorService.generateMasterData(msheet, workbook);
			extraItemGeneratorService.generateMasterData(msheet, workbook);
		}
		if(!msheet.isUserManaged()){
			abstractGeneratorService.generateMasterData(msheet, workbook);
			scheduleService.generateMasterData(msheet, workbook);
			deviationService.generateMasterData(msheet, workbook);
			itemsGeneratorService.writeItems(msheet.getAggreement().getItems(), workbook, msheet, true);
		}else{
			workbook.setActiveSheet(workbook.getSheetIndex(Worksheets.EXTRA_ITEMS_SHEET));
			workbook.setSheetHidden(workbook.getSheetIndex(Worksheets.MEASUREMENTSHEET), true);
			workbook.setSheetHidden(workbook.getSheetIndex(Worksheets.ABSTRACTSHEET), true);
			workbook.setSheetHidden(workbook.getSheetIndex(Worksheets.DEVIATIONSHEET), true);
			workbook.setSheetHidden(workbook.getSheetIndex(Worksheets.RB_SCHEDULE), true);
			workbook.setSheetHidden(workbook.getSheetIndex(Worksheets.FNFB_SCHEDULE), true);
			workbook.setSheetHidden(workbook.getSheetIndex(Worksheets.TEMPSHEET), true);
		}
		
		workbook.write(fileStorageService.getOutputStream(msheet.getStorageFileName()));
		workbook.close();
		return newDoc;
	}
	
	private static void copyFileUsingChannel(File source, File dest){
	    FileChannel sourceChannel = null;
	    FileChannel destChannel = null;
	    try {
	        sourceChannel = new FileInputStream(source).getChannel();
	        destChannel = new FileOutputStream(dest).getChannel();
	        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
	       } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
	           try {
				sourceChannel.close();
				 destChannel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          
	       }
	}
	
	public Template createItemsDocument(Aggreement aggreement) throws Exception{
		Template itemsTemplate = Template.findTemplatesByType(TemplateType.ITEMS).getSingleResult();
		Template tempTemplate = new Template();
		tempTemplate.setContent(itemsTemplate.getContent());
		tempTemplate.setContentType(tempTemplate.getContentType());
		tempTemplate.setFileSize(itemsTemplate.getFileSize());
		tempTemplate.setName(itemsTemplate.getName());
		XSSFWorkbook workbook = ExcelUtill.getWorkbookFromContent(itemsTemplate.getContent());
		itemsGeneratorService.writeItems(aggreement.getItems(), workbook, null, false);
		itemsGeneratorService.writeExistingItemsDataInConfig(aggreement.getItems(), workbook);
		XLColumnRange agg_num_range = new XLColumnRange(workbook, ItemRanges.T_AGGREEMENT_ID);
		ExcelUtill.writeCellValue(aggreement.getId(), agg_num_range.fetchSingleCell());
		List<Config> configs = Config.findAllConfigs();
		for(Config config : configs){
			try {
				ExcelUtill.writeCellValue(config.getValue(), (new XLColumnRange(workbook, config.getCellName())).fetchSingleCell());
			} catch (Exception e) {
				System.out.println("WARNING : "+e.getMessage());
			}
		}
		ExcelUtill.saveChangesToDocument(tempTemplate, workbook);
		return tempTemplate;
	}
	
	public void generateReport(MeasurementSheet msheet) throws Exception{
		// create the map of items and extra items
		//generateReportManualy(msheet);
		try {
			if(msheet.getAggreement().getItems().size()>0){
				XSSFWorkbook workbook = new XSSFWorkbook(fileStorageService.doGet(msheet.getStorageFileName()));
				if(msheet.getTemplateVersion()==0){
					abstractGeneratorService.generateReport(msheet, workbook);
				}else{
					abstractGeneratorServiceV2.generateReport(msheet, workbook);
					partRateStatementGeneratorService.generateReport(msheet, workbook);
					extraItemGeneratorService.generateReport(msheet, workbook);
				}
				
				scheduleService.generateReport(msheet, workbook);
				deviationService.generateReport(msheet, workbook);
				workbook.write(fileStorageService.getOutputStream(msheet.getStorageFileName()));
				workbook.close();
			}
			msheet.setLastReportDate(new Date());
			msheet.persist();
		} catch (IOException e) {
			logger.error("error occured while saving excel file to server", e);
			throw new Exception("error occured while saving excel file to server", e);
		}
	}
	
	public void removeReportData(MeasurementSheet msheet) throws IOException{
		
		XSSFWorkbook workbook = new XSSFWorkbook(fileStorageService.doGet(msheet.getStorageFileName()));
		abstractGeneratorService.removeReportData(msheet, workbook);
		scheduleService.removeReportData(msheet, workbook);
		deviationService.removeReportData(msheet, workbook);
	}
	
	public void generateReportManualy(MeasurementSheet msheet) {
		// create the map of items and extra items
		msheet.getItemAbstracts().clear();
		for(Item item : msheet.getAggreement().getItems()){
			if(item.isValidItem()){
				msheet.getItemAbstracts().add(new ItemAbstract(msheet, item));	
			}
		}
		msheet.setLastReportDate(new Date());
		msheet.persist();
	}
	
	private String getTemplateUrl(int version){
		if(version==0){
			return getClass().getClassLoader().getResource("TEMPLATE.xlsm").getPath();
		}else if(version==1){
			return getClass().getClassLoader().getResource("TEMPLATE_V1.xlsm").getPath();
		}
		return null;
	}
	
	  

}
