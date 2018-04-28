package com.org.web;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.org.constants.Worksheets;
import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import com.org.entity.MeasurementSheet;
import com.org.excel.gateway.ExcelGatewayTo;
import com.org.excel.gateway.ResponseStatus;
import com.org.excel.gateway.ItemsTo;
import com.org.excel.service.ExcelUtill;
import com.org.exception.ForeignKeyConstraintException;
import com.org.exception.ParentItemNotFoundException;
import com.org.report.service.ItemsGeneratorService;
import com.org.service.MeasurementSheetService;
import com.org.service.blobstore.FileStorageService;
import com.org.util.QueryUtil;

@RequestMapping("/excelgateway")
@Controller
public class ExcelGatewayController {
	
	@Autowired
	private MeasurementSheetService meausrementService;
	
	@Autowired
	private ItemsGeneratorService itemsService;
	
	@Autowired
	private FileStorageService fileStorageService;

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }
    
    @RequestMapping(params = {"aggreement"}, produces = "text/html")
    public String showByAggreement(Model uiModel, @RequestParam("aggreement") Long aggreementId,HttpServletResponse response) {
    	ExcelGatewayTo command = new ExcelGatewayTo();
    	Aggreement aggreement = QueryUtil.getUniqueResult(Aggreement.findAggreementsByIdAndLogUser(aggreementId, LogUser.getCurrentUser()));
        if(aggreement!=null){
        	command.setAggreementId(aggreement.getId());
        }
    	uiModel.addAttribute("command", command);
        return index();
    }
    
    @RequestMapping(params = {"measurementSheet"}, produces = "text/html")
    public String showByMeasurementSheet(Model uiModel, @RequestParam("measurementSheet") Long msheetId,HttpServletResponse response) {
    	ExcelGatewayTo command = new ExcelGatewayTo();
    	MeasurementSheet msheet = QueryUtil.getUniqueResult(MeasurementSheet.findMeasurementSheetsByIdAndLogUser(msheetId, LogUser.getCurrentUser()));
        if(msheet!=null){
        	command.setMeasurementSheetId(msheet.getId());
        }
    	uiModel.addAttribute("command", command);
        return index();
    }
    
    @RequestMapping(value="{id}/updateitems", method = RequestMethod.POST, produces = "text/html")
	public String updateItemsFromJSON(@PathVariable("id") Long id,
			@Valid ExcelGatewayTo command, Model uiModel,
			HttpServletRequest httpServletRequest) {
		System.out.println(command.getItemsAsJSON());
		Aggreement aggreement = QueryUtil.getUniqueResult(Aggreement
				.findAggreementsByIdAndLogUser(id, LogUser.getCurrentUser()));
		try {
			ObjectMapper mapper = new ObjectMapper();
			ItemsTo itemsTo = mapper.readValue(command.getItemsAsJSON(), ItemsTo.class);
			prepareItems(command, aggreement, itemsTo);
			Item invalidItem = validateItems(itemsTo);
			if(invalidItem!=null){
				command.setStatus(ResponseStatus.EXCEPTION);
				command.setMessage(invalidItem.getValidationMessage());
			}else{
				removeExistingItems(aggreement, itemsTo);
				saveItemsAndAddToCommand(itemsTo, command);
				if(itemsTo.getMsheetId()!=null){
					updateExtraItemsInExcel(itemsTo);
				}
				createItemAbstracts(aggreement, itemsTo);
				command.setStatus(ResponseStatus.SUCCESS);
				command.setMessage("data saved successfully");
			}
		} catch (Exception e) {
			command.setStatus(ResponseStatus.EXCEPTION);
			command.setMessage(e.getMessage());
			e.printStackTrace();
		} 
		uiModel.addAttribute("command", command);
		return index();
	}

	private void saveItemsAndAddToCommand(ItemsTo itemsTo, ExcelGatewayTo command) {
		int count =1;
		for(Item item : itemsTo.getItems()){
			item.persist();
			command.getItemNumbers().add(item.getItemNumber());
			 if ( ++count % 50 == 0 ) {
			      item.flush();
			      item.clear();
			   }
		}
		
	}

	private Item validateItems(ItemsTo itemsTo) {
		for(Item item : itemsTo.getItems()){
			if(!item.validate()){
				return item;
			}
		}
		return null;
	}

	private void createItemAbstracts(Aggreement aggreement, ItemsTo itemsTo) {
		if(itemsTo.getMsheetId()!=null){
			MeasurementSheet msheet = MeasurementSheet.findMeasurementSheet(itemsTo.getMsheetId());
			for (Item item : itemsTo.getItems()) {
				if(item.getMeasurementSheetId()!=null && item.isValidItem()){
					ItemAbstract itemAbstract = new ItemAbstract();
					itemAbstract.setMeasurementSheet(msheet);
					itemAbstract.setItem(item);
					itemAbstract.persist();
				}
			}
		}
		
	}

	private void prepareItems(ExcelGatewayTo command,
			Aggreement aggreement, ItemsTo itemsTo)
			throws ParentItemNotFoundException {
		List<Item> items = itemsTo.getItems();
		Map<String, Item> itemMap = new HashMap<String, Item>();
		Long sortOrder = getSortOrder(aggreement, itemsTo);
		Item parentItem;
		for (Item item : items) {
			int lastindex = item.getItemNumber().lastIndexOf(".");
			if (lastindex > 0) {
				parentItem = itemMap.get(item.getItemNumber().substring(0,
						lastindex));
				if (parentItem == null) {
					throw new ParentItemNotFoundException(item
							.getItemNumber().substring(0, lastindex));
				}
				item.addParentItem(parentItem);
			}
			item.setSortOrder(++sortOrder);
			item.setQuantityPerUnit(item.getQuantityPerUnit() == null ? 1.0
					: item.getQuantityPerUnit());
			item.setPartRate(item.getPartRate() == null ? item
					.getFullRate() : item.getPartRate());
			item.setAggreement(aggreement);
			item.setIsExtraItem(itemsTo.isExtraItem());
			item.setMeasurementSheetId(itemsTo.getMsheetId());
			//item.persist();
			itemMap.put(item.getItemNumber(), item);
			//command.getItemNumbers().add(item.getItemNumber());
		}
	}

	private void removeExistingItems(Aggreement aggreement, ItemsTo itemsTo) {
		if (itemsTo.getMsheetId() != null) {
			List<Item> existingItems = Item
					.findItemsByAggreementAndMeasurementSheetId(aggreement,
							itemsTo.getMsheetId(), "sortOrder", "desc")
					.getResultList();
			for (Item i : existingItems) {
				i.remove();
			}
		} else {
			List<Item> existingItems = Item.findItemsByAggreement(
					aggreement, "sortOrder", "desc").getResultList();
			for (Item i : existingItems) {
				if (!i.isIsExtraItem()) {
					i.remove();
				}
			}
		}
	}
    
    private Long getSortOrder(Aggreement aggreement, ItemsTo itemsTo){
    	Long sortOrder = 0L;
		if (itemsTo.isExtraItem()) {
			sortOrder = Item
					.countFindItemsByAggreementAndMeasurementSheetIdIsNullOrMeasurementSheetIdNotEquals(
							aggreement, itemsTo.getMsheetId());
		}
		return sortOrder;
    }
    
    private void updateExtraItemsInExcel(ItemsTo itemsTo) throws Exception{
    	MeasurementSheet msheet = MeasurementSheet.findMeasurementSheet(itemsTo.getMsheetId());
    	XSSFWorkbook wb = new XSSFWorkbook(fileStorageService.doGet(msheet.getStorageFileName()));
    	Set<Item> items = itemsTo.getItems()!=null? new HashSet<Item>(itemsTo.getItems()):null;
    	if(items!=null){
    		itemsService.writeItems(items, wb, msheet, true);
    	}
    	wb.write(fileStorageService.getOutputStream(msheet.getStorageFileName()));
    }
    
    @RequestMapping(value = "updatedocxl/{id}", method = RequestMethod.POST)
	public String updateMeasurementSheetFromExcel(@Valid ExcelGatewayTo command, Model uiModel,
			@PathVariable("id") Long id,
			@RequestParam("FileField") MultipartFile content) {
		MeasurementSheet measurementSheet = QueryUtil.getUniqueResult(MeasurementSheet.findMeasurementSheetsByIdAndLogUser(id, LogUser.getCurrentUser()));
		XSSFWorkbook wb = null;
		if(measurementSheet!=null){
		try {
			meausrementService.uploadExcelDocument(measurementSheet, content);
			System.out.println("upload complete");
			wb = new XSSFWorkbook(fileStorageService.doGet(measurementSheet.getStorageFileName()));
			XSSFSheet abstractSheet = wb.getSheet(Worksheets.ABSTRACTSHEET);
			System.out.println("start updating part reference");
			meausrementService.updatePartRateFromAbstract(measurementSheet, abstractSheet);
			System.out.println("update part reference complete");
			command.setMessage("file saved successfully");
			command.setStatus(ResponseStatus.SUCCESS);
		} catch (Exception e) {
			command.setMessage(e.getMessage());
			command.setStatus(ResponseStatus.EXCEPTION);
			e.printStackTrace(); 
		} finally {
			if(wb!=null) {
				try {
					wb.write(fileStorageService.getOutputStream(measurementSheet.getStorageFileName()));
				} catch (IOException e) {
					command.setStatus(ResponseStatus.EXCEPTION);
					command.setMessage("ERROR : Measurement sheet with id '"+id+"' can not be saved.");
				}
			}
			
		}
		}else{
			command.setStatus(ResponseStatus.EXCEPTION);
			command.setMessage("ERROR : Measurement sheet with id '"+id+"' not found.");
		}
		uiModel.addAttribute("command", command);
		return index();
	}
    
    @RequestMapping
    public String index() {
        return "excelgateway/index";
    }
}
