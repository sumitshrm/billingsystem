package com.org.web;
import com.org.domain.LogUser;
import com.org.entity.Labour;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
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

@RequestMapping("/labours")
@Controller
@RooWebScaffold(path = "labours", formBackingObject = Labour.class)
public class LabourController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Labour labour, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, labour);
            return "labours/create";
        }
        uiModel.asMap().clear();
        labour.persist();
        return "redirect:/labours/" + encodeUrlPathSegment(labour.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Labour());
        return "labours/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("labour", Labour.findLabour(id));
        uiModel.addAttribute("itemId", id);
        return "labours/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labours", Labour.findLabourEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) Labour.countLabours() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labours", Labour.findAllLabours(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "labours/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Labour labour, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, labour);
            return "labours/update";
        }
        uiModel.asMap().clear();
        labour.merge();
        return "redirect:/labours/" + encodeUrlPathSegment(labour.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Labour.findLabour(id));
        return "labours/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Labour labour = Labour.findLabour(id);
        labour.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/labours";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("labour_createdon_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("labour_lastupdatedon_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Labour labour) {
        uiModel.addAttribute("labour", labour);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("logusers", LogUser.findAllLogUsers());
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
