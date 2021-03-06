// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.web;

import com.org.entity.MaterialEntry;
import com.org.web.MaterialEntryController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect MaterialEntryController_Roo_Controller {
    
    @RequestMapping(params = "form", produces = "text/html")
    public String MaterialEntryController.createForm(Model uiModel) {
        populateEditForm(uiModel, new MaterialEntry());
        return "materialentrys/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String MaterialEntryController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("materialentry", MaterialEntry.findMaterialEntry(id));
        uiModel.addAttribute("itemId", id);
        return "materialentrys/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String MaterialEntryController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("materialentrys", MaterialEntry.findMaterialEntryEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) MaterialEntry.countMaterialEntrys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("materialentrys", MaterialEntry.findAllMaterialEntrys(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "materialentrys/list";
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String MaterialEntryController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, MaterialEntry.findMaterialEntry(id));
        return "materialentrys/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String MaterialEntryController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MaterialEntry materialEntry = MaterialEntry.findMaterialEntry(id);
        materialEntry.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/materialentrys";
    }
    
    void MaterialEntryController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("materialEntry_createdon_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("materialEntry_lastupdatedon_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("materialEntry_date_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }
    
    String MaterialEntryController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
