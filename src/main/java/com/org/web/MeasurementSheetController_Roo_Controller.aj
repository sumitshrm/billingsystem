// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.web;

import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.Document;
import com.org.entity.ItemAbstract;
import com.org.entity.MeasurementSheet;
import com.org.web.MeasurementSheetController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect MeasurementSheetController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String MeasurementSheetController.update(@Valid MeasurementSheet measurementSheet, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, measurementSheet);
            return "measurementsheets/update";
        }
        uiModel.asMap().clear();
        measurementSheet.merge();
        return "redirect:/measurementsheets/" + encodeUrlPathSegment(measurementSheet.getId().toString(), httpServletRequest);
    }
    
    void MeasurementSheetController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("measurementSheet_createdate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("measurementSheet_lastupdateddate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("measurementSheet_lastreportdate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }
    
    void MeasurementSheetController.populateEditForm(Model uiModel, MeasurementSheet measurementSheet) {
        uiModel.addAttribute("measurementSheet", measurementSheet);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("logusers", LogUser.findAllLogUsers());
        uiModel.addAttribute("aggreements", Aggreement.findAllAggreements());
        uiModel.addAttribute("documents", Document.findAllDocuments());
        uiModel.addAttribute("itemabstracts", ItemAbstract.findAllItemAbstracts());
    }
    
    String MeasurementSheetController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
