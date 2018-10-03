package com.org.web;
import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.appengine.api.urlfetch.HTTPMethod;
import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.Document;
import com.org.entity.ItemAbstract;
import com.org.entity.MeasurementSheet;
import com.org.entity.MeasurementSheetShared;
import com.org.report.service.AbstractGeneratorService;
import com.org.report.service.AbstractGeneratorServiceV2;
import com.org.report.service.PartRateStatementGeneratorService;
import com.org.service.DocumentService;
import com.org.service.MeasurementSheetService;
import com.org.service.blobstore.FileStorageService;
import com.org.util.QueryUtil;
import com.org.view.MessageType;
import com.org.view.MessageVo;

@RequestMapping("/measurementsheets")
@Controller
@RooWebScaffold(path = "measurementsheets", formBackingObject = MeasurementSheet.class)
public class MeasurementSheetController {
	
	final static Logger logger = Logger.getLogger(MeasurementSheetController.class);


    @Autowired
    private DocumentService documentService;

    @Autowired
    private MeasurementSheetService measurementService;

    @Autowired
    @Qualifier("abstractGeneratorService")
    private AbstractGeneratorService abstractService;

    @Autowired
    @Qualifier("abstractGeneratorServiceV2")
    private AbstractGeneratorServiceV2 abstractServiceV2;

    @Autowired
    private FileStorageService fileStorageService;

    @RequestMapping(params = { "form", "aggreement" }, produces = "text/html")
    public String createForm(Model uiModel, @RequestParam("aggreement") Long aggreementId) {
        MeasurementSheet sheet = new MeasurementSheet();
        Aggreement aggreement = Aggreement.findAggreement(aggreementId);
        sheet.setSerialNumber(aggreement.getNextSerialNumber());
        sheet.setAggreement(aggreement);
        uiModel.addAttribute("measurementSheet", sheet);
        return "measurementsheets/create";
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid MeasurementSheet measurementSheet, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, measurementSheet);
            return "measurementsheets/create";
        }
        uiModel.asMap().clear();
        Aggreement aggreement = Aggreement.findAggreement(measurementSheet.getAggreement().getId());
        measurementSheet.setAggreement(aggreement);
        String filename = measurementSheet.getDocumentFileName();
        measurementSheet.persist();
        Document defaultDoc;
        try {
            defaultDoc = documentService.createDefaultDocument(measurementSheet);
            defaultDoc.persist();
            measurementSheet.setDocument(defaultDoc);
            documentService.generateReportManualy(measurementSheet);
            measurementSheet.persist();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "redirect:/measurementsheets/" + encodeUrlPathSegment(measurementSheet.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        MeasurementSheet msheet = MeasurementSheet.findMeasurementSheet(id);
        uiModel.addAttribute("measurementSheet", msheet);
        return "measurementsheets/update";
    }

    @RequestMapping(value = "updatedoc", method = RequestMethod.POST)
    public String updateMeasurementSheet(@Valid MeasurementSheet measurementSheet, BindingResult bindingResult, Model uiModel, @RequestParam("document.content") MultipartFile content, HttpServletRequest httpServletRequest) {
        try {
            measurementService.uploadExcelDocument(measurementSheet, content);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // measurementSheet.persist();
        uiModel.asMap().clear();
        return "redirect:/measurementsheets/" + measurementSheet.getId();
    }

    @RequestMapping(value = "reloadabstract/{id}")
    public String reloadAbstractDataFromMeasurementSheet(@PathVariable("id") Long msheetId, Model uiModel) {
        MeasurementSheet msheet = QueryUtil.getUniqueResult(MeasurementSheet.findMeasurementSheetsByIdAndLogUser(msheetId, LogUser.getCurrentUser()));
        if (msheet != null) {
            try {
                XSSFWorkbook workbook = new XSSFWorkbook(fileStorageService.doGet(msheet.getStorageFileName()));
                if (msheet.getTemplateVersion() == 0) {
                    abstractService.reloadData(msheet, workbook);
                } else {
                    abstractServiceV2.reloadData(msheet, workbook);
                }
                workbook.write(fileStorageService.getOutputStream(msheet.getStorageFileName()));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                uiModel.addAttribute("message", e.getMessage());
            } finally {
            }
        }
        return "redirect:/measurementsheets/" + msheet.getId();
    }

    @RequestMapping(value = "{id}/excel")
    public String showMeasurementExcelView(@PathVariable("id") Long id, Model modelui) {
        MeasurementSheet sheet = QueryUtil.getUniqueResult(MeasurementSheet.findMeasurementSheetsByIdAndLogUser(id, LogUser.getCurrentUser()));
        sheet = sheet == null ? new MeasurementSheet() : sheet;
        modelui.addAttribute("measurementsheet", sheet);
        return "measurementsheets/excel";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MeasurementSheet measurementSheet = MeasurementSheet.findMeasurementSheet(id);
        /* File excelFile = measurementSheet.getDocument() == null ? null : measurementSheet.getDocument().getExcelFile();
         if (excelFile == null) {
         System.out.println("WARNING:File not found for measuremnt sheet id : " + measurementSheet.getId());
         }
         boolean fileDeleted = excelFile == null ? false : excelFile.delete();
         if (!fileDeleted) {
         System.out.println("WARNING:File can not be deleted for measurement sheet id : " + measurementSheet.getId());
         }*/
        try {
            fileStorageService.delete(measurementSheet.getStorageFileName());
        } catch (Exception e) {
            System.out.println("FILE CAN NOT BE DELETED : " + measurementSheet.getStorageFileName());
            e.printStackTrace();
        }
        measurementSheet.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/aggreements/" + measurementSheet.getAggreement().getId().toString();
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("measurementsheets", MeasurementSheet.findMeasurementSheetsByLogUser(LogUser.getCurrentUser(), sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) MeasurementSheet.countMeasurementSheets() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("measurementsheets", MeasurementSheet.findMeasurementSheetsByLogUser(LogUser.getCurrentUser(), sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "measurementsheets/list";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        MeasurementSheet msheet = QueryUtil.getUniqueResult(MeasurementSheet.findMeasurementSheetsByIdAndLogUser(id, LogUser.getCurrentUser()));
        //Collections.sort(msheet.getItemAbstracts(), new ItemAbstractComparator());
        List<MeasurementSheetShared> shared =  MeasurementSheetShared.findMeasurementSheetSharedsByMeasurementSheet(msheet).getResultList();
        uiModel.addAttribute("measurementsheet", msheet);
        uiModel.addAttribute("itemId", id);
        uiModel.addAttribute("sharedwith", shared);
        return "measurementsheets/show";
    }

    @RequestMapping(value = "/{id}/report", produces = "text/html", method = RequestMethod.POST)
    public String generateReport(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MeasurementSheet measurementSheet = MeasurementSheet.findMeasurementSheet(id);
        try {
            if (!measurementSheet.isUserManaged()) {
                documentService.generateReport(measurementSheet);
                uiModel.addAttribute("message", new MessageVo("measurement_sheet_report_generated_successfully", MessageType.SUCCESS));
            } else {
                uiModel.addAttribute("message", new MessageVo("measurement_sheet_report_user_managed", MessageType.ERROR));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            uiModel.addAttribute("message", new MessageVo("notification_message_error", MessageType.ERROR).addParam(e.getMessage()));
        } finally {
            //measurementSheet.getDocument().close();
        }
        uiModel.addAttribute("measurementsheet", measurementSheet);
        uiModel.addAttribute("itemId", measurementSheet.getId());
        return "measurementsheets/show";
    }

    @RequestMapping(value = "/{id}/report/manualy", produces = "text/html")
    public String generateReportManualy(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MeasurementSheet measurementSheet = MeasurementSheet.findMeasurementSheet(id);
        documentService.generateReportManualy(measurementSheet);
        return "redirect:/measurementsheets/" + measurementSheet.getId();
    }

    @RequestMapping(value = "updateabstract/{id}", method = RequestMethod.POST)
    public String updateItemAbstractData(@Valid MeasurementSheet measurementSheet, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        System.out.println(measurementSheet.getId());
        ItemAbstract itemAbstract = null;
        for (ItemAbstract ia : measurementSheet.getItemAbstracts()) {
            itemAbstract = ItemAbstract.findItemAbstract(ia.getId());
            itemAbstract.setTotal(ia.getTotal());
            itemAbstract.persist();
        }
        return "redirect:/measurementsheets/" + measurementSheet.getId();
    }
    
    @RequestMapping(value="/share", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity shareMeasurementSheet(@RequestParam(value = "userid", required=true) Long userid, @RequestParam(value = "msheetid", required=true) Long msheetid){
    	LogUser user = LogUser.findLogUser(userid);
    	if(user==null) {
    		logger.error("Invalid user id "+ userid);
    		return new ResponseEntity<String>("Invalid user selected",HttpStatus.BAD_REQUEST);
    	}
    	try {
    		LogUser currentuser = LogUser.getCurrentUser();
        	MeasurementSheet msheet = MeasurementSheet.findMeasurementSheetsByIdAndLogUser(msheetid, currentuser).getSingleResult();
        	MeasurementSheetShared shared = new MeasurementSheetShared();
        	shared.setMeasurementSheet(msheet);
        	shared.setSharedWith(user);
        	shared.setOpened(false);
        	shared.setSharedDate(new Date());
        	shared.persist();
			return new ResponseEntity<String>(shared.getId().toString(), HttpStatus.OK);
    	} catch (Exception e) {
			return new ResponseEntity<String>("Error occurred while sharing document : "+e.getMessage(),HttpStatus.BAD_REQUEST);
		}
    	
    }
    
    @RequestMapping(value="/share", produces=MediaType.APPLICATION_JSON_VALUE, method=RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> unShareMeasurementSheet(@RequestParam(value = "shareid", required=true) Long shareid, @RequestParam(value = "msheetid", required=false) Long msheetid){
    	
    	try {
    		MeasurementSheetShared shared = MeasurementSheetShared.findMeasurementSheetShared(shareid);
    		shared.remove();
			return new ResponseEntity<>("success",HttpStatus.OK);
    	} catch (Exception e) {
			return new ResponseEntity<String>("Error occurred while sharing document : "+e.getMessage(),HttpStatus.BAD_REQUEST);
		}
    	
    }
}
