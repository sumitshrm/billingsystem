package com.org.web;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.LabourEntry;
import com.org.entity.LabourSupplier;
import com.org.service.LabourEntryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/labourentrys")
@Controller
@RooWebScaffold(path = "labourentrys", formBackingObject = LabourEntry.class)
public class LabourEntryController {
	
	@Autowired
	private LabourEntryService labourEntryService;
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LabourEntry labourEntry, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, labourEntry);
            return "labourentrys/create";
        }
        uiModel.asMap().clear();
        labourEntryService.create(labourEntry);
        return "redirect:/labourentrys/" + encodeUrlPathSegment(labourEntry.getId().toString(), httpServletRequest);
    }
	
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LabourEntry labourEntry, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, labourEntry);
            return "labourentrys/update";
        }
        uiModel.asMap().clear();
        labourEntryService.udate(labourEntry);
        return "redirect:/labourentrys/" + encodeUrlPathSegment(labourEntry.getId().toString(), httpServletRequest);
    }
	
	
	
	void populateEditForm(Model uiModel, LabourEntry labourEntry) {
		LogUser currentUser = LogUser.getCurrentUser();
        uiModel.addAttribute("labourEntry", labourEntry);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("logusers", LogUser.findAllLogUsers());
        uiModel.addAttribute("aggreements", Aggreement.findAggreementsByLogUser(currentUser).getResultList());
        uiModel.addAttribute("laboursuppliers", LabourSupplier.findLabourSuppliersByCreatedBy(currentUser).getResultList());
        
    }
	
}
