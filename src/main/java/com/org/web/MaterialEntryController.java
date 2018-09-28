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
import java.io.UnsupportedEncodingException;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

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

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new MaterialEntry());
        return "materialentrys/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("materialentry", MaterialEntry.findMaterialEntry(id));
        uiModel.addAttribute("itemId", id);
        return "materialentrys/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
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
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, MaterialEntry.findMaterialEntry(id));
        return "materialentrys/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MaterialEntry materialEntry = MaterialEntry.findMaterialEntry(id);
        materialEntry.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/materialentrys";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("materialEntry_createdon_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("materialEntry_lastupdatedon_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("materialEntry_date_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
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
