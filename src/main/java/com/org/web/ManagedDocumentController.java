package com.org.web;
import com.org.constants.ManagedDocumentType;
import com.org.constants.MeasurementSheetConstants;
import com.org.domain.Config;
import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.ManagedDocument;
import com.org.entity.ManagedDocumentShared;
import com.org.entity.MaterialEntry;
import com.org.entity.MeasurementSheet;
import com.org.entity.MeasurementSheetShared;
import com.org.entity.UserStorage;
import com.org.excel.gateway.ExcelGatewayTo;
import com.org.excel.gateway.ResponseStatus;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.XLColumnRange;
import com.org.service.blobstore.FileStorageService;
import com.org.util.FileStorageProperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.POIXMLProperties.CustomProperties;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/manageddocuments")
@Controller
@RooWebScaffold(path = "manageddocuments", formBackingObject = ManagedDocument.class)
public class ManagedDocumentController {

	final static Logger logger = Logger.getLogger(ManagedDocumentController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel, @RequestParam(value = "aggreement", required = false) Long aggreementId) {
        ManagedDocument managedDocument = new ManagedDocument();
        if (aggreementId != null) {
            managedDocument.setAggreement(Aggreement.findAggreement(aggreementId));
        }
        populateEditForm(uiModel, managedDocument);
        return "manageddocuments/create";
    }

    
    @RequestMapping(value="/folder", method = RequestMethod.POST)
    public String createFolder( ManagedDocument managedDocument, Model uiModel) {
    	LogUser user = LogUser.getCurrentUser();
    	managedDocument.setType(ManagedDocumentType.FOLDER);
    	if(managedDocument.getParent()!=null) {
    		ManagedDocument current = ManagedDocument.findManagedDocumentsByIdAndLogUser(managedDocument.getParent().getId(), user).getSingleResult();
    		managedDocument.setParent(current);
    	}
    	managedDocument.persist();
    	String param = managedDocument.getParent()==null?"":"?currentFolder="+managedDocument.getParent().getId();
        return "redirect:/manageddocuments"+param;
    }
    
    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value="currentFolder", required=false) Long currentFolder, Model uiModel) {
    	LogUser user = LogUser.getCurrentUser();
    	ManagedDocument managedDocument = new ManagedDocument();
    	if(currentFolder==null) {
    		uiModel.addAttribute("fileFolders", ManagedDocument.findManagedDocumentsByLogUserAndParentIsNull(user).getResultList());
    	}else {
    		ManagedDocument current = ManagedDocument.findManagedDocumentsByIdAndLogUser(currentFolder, user).getSingleResult();
    		uiModel.addAttribute("currentFolder", current);
    		uiModel.addAttribute("fileFolders", ManagedDocument.findManagedDocumentsByLogUserAndParent(user, current).getResultList());
    		managedDocument.setParent(current);
    	}
    	uiModel.addAttribute("managedDocument", managedDocument);
    	uiModel.addAttribute("storageLimit", UserStorage.getStorageLimitByUser(user));
    	
    	return "manageddocuments/list";
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ManagedDocument managedDocument, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        managedDocument.setFileSize(managedDocument.getContent().getSize());
        managedDocument.setUrl(managedDocument.getStorageUrl());
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, managedDocument);
            return "manageddocuments/create";
        }
        uiModel.asMap().clear();
        Aggreement aggreement = Aggreement.findAggreement(managedDocument.getAggreement().getId());
        managedDocument.setAggreement(aggreement);
        managedDocument.persist();
        managedDocument.setUrl(managedDocument.getStorageUrl());
        managedDocument.persist();
        try {
            fileStorageService.doPost(managedDocument.getContent().getInputStream(), managedDocument.getStorageUrl());
        } catch (IOException e) {
            e.printStackTrace();
            populateEditForm(uiModel, managedDocument);
            return "manageddocuments/create";
        }
        return "redirect:/manageddocuments/" + encodeUrlPathSegment(managedDocument.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value="/template/{type}",method = RequestMethod.POST, produces = "text/html")
    public String createFromTemplate(@PathVariable("type") String type, @Valid ManagedDocument managedDocument, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        LogUser user = LogUser.getCurrentUser();
    	managedDocument.setFileSize(managedDocument.getContent().getSize());
    	managedDocument.setType(ManagedDocumentType.FILE);
        if(managedDocument.getParent()!=null) {
        	ManagedDocument current = ManagedDocument.findManagedDocumentsByIdAndLogUser(managedDocument.getParent().getId(), user).getSingleResult();
    		managedDocument.setParent(current);
        }
        managedDocument.setUrl("default_file_name");
        managedDocument.persist();
        InputStream iostream = null;
        String fileName = null;
        if(type.equals("xls")) {
        	fileName = createExcelFileFromTemplate(managedDocument);
    		
        }else if(type.equals("doc")) {
        	fileName = createWordFileFromTemplate(managedDocument);
        }else {
        	iostream = managedDocument.getContent().getInputStream();
        	fileName = managedDocument.getStorageUrl();
        	fileStorageService.doPost(iostream, fileName);
        }
        managedDocument.setUrl(fileName);
        managedDocument.persist();
        String param = managedDocument.getParent()==null?"":"?currentFolder="+managedDocument.getParent().getId();
        return "redirect:/manageddocuments"+param;
    }



	private String createWordFileFromTemplate(ManagedDocument managedDocument) throws IOException {
		InputStream iostream;
		String fileName;
		iostream = fileStorageService.doGet(FileStorageProperties.WORD_TEMPLATE_FILE);
		XWPFDocument document = new XWPFDocument(iostream);
		List<Config> configs = Config.findAllConfigs();
		for(Config config : configs){
			try {
			      document.getProperties().getCustomProperties().getProperty(config.getCellName()).setLpwstr(config.getValue());
			} catch (Exception e) {
				System.out.println("WARNING : "+e.getMessage());
			}
		}
	    document.getProperties().getCustomProperties().getProperty("T_DOC_ID").setLpwstr(managedDocument.getId().toString());
		fileName = managedDocument.getStorageUrl()+".docm";
		document.write(fileStorageService.getOutputStream(fileName));
		return fileName;
	}



	private String createExcelFileFromTemplate(ManagedDocument managedDocument) throws IOException, Exception {
		InputStream iostream;
		String fileName;
		iostream = fileStorageService.doGet(FileStorageProperties.EXCEL_TEMPLATE_FILE);
		XSSFWorkbook workbook = new XSSFWorkbook(iostream);
		fileName = managedDocument.getStorageUrl()+".xlsm";
		try {
			XSSFCell documentIdCell = new XLColumnRange(workbook, MeasurementSheetConstants.TEMPLATE_MEASUREMENT_SHEET_ID).fetchSingleCell();
			ExcelUtill.writeCellValue(managedDocument.getId(), documentIdCell);
			List<Config> configs = Config.findAllConfigs();
			for(Config config : configs){
				try {
					ExcelUtill.writeCellValue(config.getValue(), (new XLColumnRange(workbook, config.getCellName())).fetchSingleCell());
				} catch (Exception e) {
					System.out.println("WARNING : "+e.getMessage());
				}
			}
			workbook.write(fileStorageService.getOutputStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return fileName;
	}
    
    @RequestMapping(value = "/updatefile/{id}", method = RequestMethod.POST)
	public String updateFromGateway(ExcelGatewayTo command, Model uiModel,
			@PathVariable("id") Long id,
			@RequestParam("FileField") MultipartFile content) {
    	ManagedDocument doc = ManagedDocument.findManagedDocumentsByIdAndLogUser(id, LogUser.getCurrentUser()).getSingleResult();
    	if(doc!=null) {
    		try {
    			fileStorageService.doPost(content.getInputStream(), doc.getUrl());
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	command.setMessage("file saved successfully");
    		command.setStatus(ResponseStatus.SUCCESS);
    		uiModel.addAttribute("command", command);
    	}
    	else {
    		command.setMessage("document not found");
    		command.setStatus(ResponseStatus.EXCEPTION);
    	}
    	return "excelgateway/index";
    	
    }
    

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ManagedDocument managedDocument, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
        }
        Aggreement aggreement = Aggreement.findAggreement(managedDocument.getAggreement().getId());
        managedDocument.setAggreement(aggreement);
        uiModel.asMap().clear();
        managedDocument.merge();
        return "redirect:/manageddocuments/" + managedDocument.getId();
    }
    
    @RequestMapping(value="/update/description", method = RequestMethod.POST, produces = "text/html")
    public String updateDescription(@RequestParam("id") String id, @RequestParam(value = "agg", required = false) Integer agg,@RequestParam("description") String description,HttpServletRequest httpServletRequest) {
        System.out.println("update called" + id + description);
        ManagedDocument document = ManagedDocument.findManagedDocument(Long.parseLong(id));
        document.setDescription(description);
        document.merge();
        String param = document.getParent()==null?"":"?currentFolder="+document.getParent().getId();
        return "redirect:/manageddocuments"+param;

    }

    @RequestMapping("/download")
    public String download(@RequestParam(value = "file", required = false) String file, HttpServletResponse response, Model model) {
        try {
            response.setHeader("Content-Disposition", "attachment;filename=\"" + file + "\"");
            System.out.println("URL--------------" + file);
            fileStorageService.doGet(response, file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "agg", required = false) Integer agg, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ManagedDocument managedDocument = ManagedDocument.findManagedDocument(id);
        String param =managedDocument.getParent()==null?"":"?currentFolder="+managedDocument.getParent().getId();
        this.deleteFile(managedDocument);
        managedDocument.remove();
        uiModel.asMap().clear();
        //uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        //uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/manageddocuments"+param;
    }
    
    private void deleteFile(ManagedDocument doc) {
    	for(ManagedDocument child : doc.getChildren()) {
        	deleteFile(child);
        }
    	if(doc.getType()==ManagedDocumentType.FILE) {
    		fileStorageService.delete(doc.getUrl());
    	}
    }
    

    /*@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "agg", required = false) Long aggId,@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
       
    	LogUser user = LogUser.getCurrentUser();
    	
    	if(aggId!=null) {
    		Aggreement agg = Aggreement.findAggreement(aggId);
    		if(agg!=null) {
                uiModel.addAttribute("manageddocuments", ManagedDocument.findManagedDocumentsByAggreementAndLogUser(agg, user).getResultList());
                uiModel.addAttribute("aggreement",aggId);
    		}
    	}else {
            uiModel.addAttribute("manageddocuments", ManagedDocument.findManagedDocumentsByLogUser(LogUser.getCurrentUser(), sortFieldName, sortOrder).getResultList());
            uiModel.addAttribute("aggreements", Aggreement.findAggreementsByLogUser(LogUser.getCurrentUser(), sortFieldName, sortOrder).getResultList());
    	}
    	uiModel.addAttribute("storageLimit", UserStorage.getStorageLimitByUser(user));
    	
        uiModel.addAttribute("managedDocument", new ManagedDocument());
       return "manageddocuments/list";
    }*/

    void populateEditForm(Model uiModel, ManagedDocument managedDocument) {
        uiModel.addAttribute("managedDocument", managedDocument);
        uiModel.addAttribute("logusers", LogUser.findAllLogUsers());
        uiModel.addAttribute("aggreements", Aggreement.findAggreementsByLogUser(LogUser.getCurrentUser()).getResultList());
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
		ManagedDocument managedDocument = ManagedDocument.findManagedDocument(id);
        uiModel.addAttribute("manageddocument", managedDocument);
        uiModel.addAttribute("itemId", id);
        List<ManagedDocumentShared> managedDocShared = ManagedDocumentShared.findManagedDocumentSharedsByManagedDocument(managedDocument).getResultList();
        uiModel.addAttribute("managedDocShared", managedDocShared);
        return "manageddocuments/show";
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ManagedDocument.findManagedDocument(id));
        return "manageddocuments/update";
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
	
	@RequestMapping(value="/share", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity shareMeasurementSheet(@RequestParam(value = "userid", required=true) Long userid, @RequestParam(value = "mdocid", required=true) Long mdocid){
    	LogUser user = LogUser.findLogUser(userid);
    	if(user==null) {
    		logger.error("Invalid user id "+ userid);
    		return new ResponseEntity<String>("Invalid user selected",HttpStatus.BAD_REQUEST);
    	}
    	try {
    		LogUser currentuser = LogUser.getCurrentUser();
        	ManagedDocument managedDoc = ManagedDocument.findManagedDocumentsByIdAndLogUser(mdocid, currentuser).getSingleResult();
        	ManagedDocumentShared shared = new ManagedDocumentShared();
        	shared.setManagedDocument(managedDoc);
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
    public ResponseEntity<String> unShareMeasurementSheet(@RequestParam(value = "shareid", required=true) Long shareid, @RequestParam(value = "mdocid", required=false) Long mdocid){
    	
    	try {
    		ManagedDocumentShared shared = ManagedDocumentShared.findManagedDocumentShared(shareid);
    		shared.remove();
			return new ResponseEntity<>("success",HttpStatus.OK);
    	} catch (Exception e) {
			return new ResponseEntity<String>("Error occurred while deleting document : "+e.getMessage(),HttpStatus.BAD_REQUEST);
		}
    	
    }
    
    @RequestMapping(value="/storage/{id}", produces=MediaType.APPLICATION_JSON_VALUE, method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> fetchStorageUsed(@PathVariable("id") Long id){
    	System.out.println("getting storage");
    	long storage = ManagedDocument.getStorageByUser(id);
    	storage = Math.round(storage/1048576);//Convert to MB
    	System.out.println("storege"+storage);
    	return new ResponseEntity<String>(Long.toString(storage), HttpStatus.OK);
    }
}
