// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.web;

import com.org.entity.ManagedDocument;
import com.org.web.ManagedDocumentController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect ManagedDocumentController_Roo_Controller {
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ManagedDocumentController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("manageddocument", ManagedDocument.findManagedDocument(id));
        uiModel.addAttribute("itemId", id);
        return "manageddocuments/show";
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ManagedDocumentController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ManagedDocument.findManagedDocument(id));
        return "manageddocuments/update";
    }
    
    String ManagedDocumentController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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