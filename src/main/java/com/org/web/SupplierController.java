package com.org.web;
import com.org.domain.LogUser;
import com.org.entity.Supplier;
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

@RequestMapping("/suppliers")
@Controller
@RooWebScaffold(path = "suppliers", formBackingObject = Supplier.class)
public class SupplierController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Supplier supplier, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, supplier);
            return "suppliers/create";
        }
        uiModel.asMap().clear();
        supplier.persist();
        return "redirect:/suppliers/" + encodeUrlPathSegment(supplier.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Supplier());
        return "suppliers/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("supplier", Supplier.findSupplier(id));
        uiModel.addAttribute("itemId", id);
        return "suppliers/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("suppliers", Supplier.findSupplierEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) Supplier.countSuppliers() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("suppliers", Supplier.findAllSuppliers(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "suppliers/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Supplier supplier, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, supplier);
            return "suppliers/update";
        }
        uiModel.asMap().clear();
        supplier.merge();
        return "redirect:/suppliers/" + encodeUrlPathSegment(supplier.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Supplier.findSupplier(id));
        return "suppliers/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Supplier supplier = Supplier.findSupplier(id);
        supplier.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/suppliers";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("supplier_createdon_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("supplier_lastupdatedon_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Supplier supplier) {
        uiModel.addAttribute("supplier", supplier);
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
