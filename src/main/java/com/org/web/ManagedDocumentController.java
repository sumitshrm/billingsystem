package com.org.web;
import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.ManagedDocument;
import com.org.entity.MaterialEntry;
import com.org.excel.service.ExcelUtill;
import com.org.service.blobstore.FileStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/manageddocuments")
@Controller
@RooWebScaffold(path = "manageddocuments", formBackingObject = ManagedDocument.class)
public class ManagedDocumentController {
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel, @RequestParam(value="aggreement", required=false) Long aggreementId) {
		ManagedDocument managedDocument = new ManagedDocument();
		if(aggreementId!=null) {
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
        System.out.println(managedDocument.getUrl()+"=========");
        try {
			fileStorageService.doPost(managedDocument.getContent().getInputStream(), managedDocument.getStorageUrl());
		} catch (IOException e) {
			e.printStackTrace();
			populateEditForm(uiModel, managedDocument);
            return "manageddocuments/create";
		}
        return "redirect:/manageddocuments/" + encodeUrlPathSegment(managedDocument.getId().toString(), httpServletRequest);
    }
	
	
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ManagedDocument managedDocument, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	System.out.println(bindingResult);
            //populateEditForm(uiModel, managedDocument);
            //return "manageddocuments/update";
        }
        Aggreement aggreement = Aggreement.findAggreement(managedDocument.getAggreement().getId());
        managedDocument.setAggreement(aggreement);
        uiModel.asMap().clear();
        managedDocument.merge();
        return "redirect:/manageddocuments/" + managedDocument.getId();
    }
	
	@RequestMapping("/download")
	public String download(@RequestParam(value = "file", required = false) String file, HttpServletResponse response, Model model) {
		try {
            response.setHeader("Content-Disposition", "attachment;filename=\"" + file + "\"");
			System.out.println("URL--------------"+file);
			fileStorageService.doGet(response, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	 @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
	        ManagedDocument managedDocument = ManagedDocument.findManagedDocument(id);
	        managedDocument.remove();
	        fileStorageService.delete(managedDocument.getUrl());
	        uiModel.asMap().clear();
	        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
	        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
	        return "redirect:/manageddocuments";
	    }
	 
	 @RequestMapping(produces = "text/html")
	    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
	        if (page != null || size != null) {
	            int sizeNo = size == null ? 10 : size.intValue();
	            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
	            uiModel.addAttribute("manageddocuments", ManagedDocument.findManagedDocumentsByLogUser(LogUser.getCurrentUser(), sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
	            float nrOfPages = (float) ManagedDocument.countManagedDocuments() / sizeNo;
	            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
	        } else {
	            uiModel.addAttribute("manageddocuments", ManagedDocument.findManagedDocumentsByLogUser(LogUser.getCurrentUser(), sortFieldName, sortOrder).getResultList());
	        }
	        return "manageddocuments/list";
	    }
	 
	 void populateEditForm(Model uiModel, ManagedDocument managedDocument) {
	        uiModel.addAttribute("managedDocument", managedDocument);
	        uiModel.addAttribute("logusers", LogUser.findAllLogUsers());
	        uiModel.addAttribute("aggreements", Aggreement.findAggreementsByLogUser(LogUser.getCurrentUser()).getResultList());
	    }

}
