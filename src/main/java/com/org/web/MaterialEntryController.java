package com.org.web;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.Company;
import com.org.entity.ItemName;
import com.org.entity.MaterialEntry;
import com.org.entity.Supplier;
import com.org.service.MaterialEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/materialentrys")
@Controller
@RooWebScaffold(path = "materialentrys", formBackingObject = MaterialEntry.class)
public class MaterialEntryController {

    @Autowired
    private MaterialEntryService materialEntryService;

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid MaterialEntry materialEntry, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, materialEntry);
            return "materialentrys/create";
        }
        uiModel.asMap().clear();
        materialEntryService.create(materialEntry);
        return "redirect:/materialentrys/" + encodeUrlPathSegment(materialEntry.getId().toString(), httpServletRequest);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid MaterialEntry materialEntry, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, materialEntry);
            return "materialentrys/update";
        }
        uiModel.asMap().clear();
        materialEntryService.update(materialEntry);
        return "redirect:/materialentrys/" + encodeUrlPathSegment(materialEntry.getId().toString(), httpServletRequest);
    }

    void populateEditForm(Model uiModel, MaterialEntry materialEntry) {
        uiModel.addAttribute("materialEntry", materialEntry);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("logusers", LogUser.findAllLogUsers());
        uiModel.addAttribute("aggreements", Aggreement.findAggreementsByLogUser(LogUser.getCurrentUser()).getResultList());
        uiModel.addAttribute("companys", Company.findCompanysByCreatedBy(LogUser.getCurrentUser()).getResultList());
        uiModel.addAttribute("itemnames", ItemName.findItemNamesByCreatedBy(LogUser.getCurrentUser()).getResultList());
        uiModel.addAttribute("suppliers", Supplier.findSuppliersByCreatedBy(LogUser.getCurrentUser()).getResultList());
    }
}
