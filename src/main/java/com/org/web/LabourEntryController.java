package com.org.web;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.LabourEntry;
import com.org.entity.LabourSupplier;
import com.org.service.LabourEntryService;
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

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LabourEntry());
        return "labourentrys/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("labourentry", LabourEntry.findLabourEntry(id));
        uiModel.addAttribute("itemId", id);
        return "labourentrys/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labourentrys", LabourEntry.findLabourEntryEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LabourEntry.countLabourEntrys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labourentrys", LabourEntry.findAllLabourEntrys(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "labourentrys/list";
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LabourEntry.findLabourEntry(id));
        return "labourentrys/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LabourEntry labourEntry = LabourEntry.findLabourEntry(id);
        labourEntry.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/labourentrys";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("labourEntry_createdon_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("labourEntry_lastupdatedon_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("labourEntry_date_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
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
