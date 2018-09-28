package com.org.web;
import com.org.constants.MeasurementSheetConstants;
import com.org.domain.Config;
import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.ManagedDocument;
import com.org.entity.MaterialEntry;
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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.poi.POIXMLProperties;
import org.apache.poi.POIXMLProperties.CustomProperties;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/manageddocuments")
@Controller
@RooWebScaffold(path = "manageddocuments", formBackingObject = ManagedDocument.class)
public class ManagedDocumentController {

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
        managedDocument.merge();
        System.out.println(managedDocument.getUrl() + "=========");
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
    public String createFromTemplate(@PathVariable("type") String type,@RequestParam(value = "redirect", required = false) String redirectUrl, @Valid ManagedDocument managedDocument, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        managedDocument.setFileSize(managedDocument.getContent().getSize());
        String queryParam= "";
        if(managedDocument.getAggreement()!=null) {
        	queryParam="?aggreement="+managedDocument.getAggreement().getId();
        	Aggreement aggreement = Aggreement.findAggreement(managedDocument.getAggreement().getId());
            managedDocument.setAggreement(aggreement);
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
        managedDocument.merge();
        if(redirectUrl!=null) {
        	return "redirect:"+redirectUrl;
        }
        return "redirect:/manageddocuments" +queryParam ;
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
    	ManagedDocument doc = ManagedDocument.findManagedDocumentsByLogUser(LogUser.getCurrentUser()).getSingleResult();
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
    public String updateDescription(@RequestParam("id") String id, @RequestParam(value = "aggreement", required = false) Integer agg,@RequestParam("description") String description,HttpServletRequest httpServletRequest) {
        System.out.println("update called" + id + description);
        ManagedDocument document = ManagedDocument.findManagedDocument(Long.parseLong(id));
        document.setDescription(description);
        document.merge();
        if(agg!=null) {
        	return "redirect:/manageddocuments?aggreement="+agg;
        }
        return "redirect:/manageddocuments";

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
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "aggreement", required = false) Integer agg, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ManagedDocument managedDocument = ManagedDocument.findManagedDocument(id);
        managedDocument.remove();
        fileStorageService.delete(managedDocument.getUrl());
        uiModel.asMap().clear();
        //uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        //uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        if(agg!=null) {
        	return "redirect:/manageddocuments?aggreement="+agg;
        }
        return "redirect:/manageddocuments";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "aggreement", required = false) Long aggId,@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        /*if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            
            uiModel.addAttribute("manageddocuments", ManagedDocument.findManagedDocumentsByLogUser(LogUser.getCurrentUser(), sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ManagedDocument.countManagedDocuments() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("manageddocuments", ManagedDocument.findManagedDocumentsByLogUser(LogUser.getCurrentUser(), sortFieldName, sortOrder).getResultList());
        }*/
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
        uiModel.addAttribute("managedDocument", new ManagedDocument());
       return "manageddocuments/list";
    }

    void populateEditForm(Model uiModel, ManagedDocument managedDocument) {
        uiModel.addAttribute("managedDocument", managedDocument);
        uiModel.addAttribute("logusers", LogUser.findAllLogUsers());
        uiModel.addAttribute("aggreements", Aggreement.findAggreementsByLogUser(LogUser.getCurrentUser()).getResultList());
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("manageddocument", ManagedDocument.findManagedDocument(id));
        uiModel.addAttribute("itemId", id);
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
}
