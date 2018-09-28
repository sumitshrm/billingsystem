package com.org.web;
import com.org.domain.LogUser;
import com.org.entity.ItemName;
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

@RequestMapping("/itemnames")
@Controller
@RooWebScaffold(path = "itemnames", formBackingObject = ItemName.class)
public class ItemNameController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ItemName itemName, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itemName);
            return "itemnames/create";
        }
        uiModel.asMap().clear();
        itemName.persist();
        return "redirect:/itemnames/" + encodeUrlPathSegment(itemName.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ItemName());
        return "itemnames/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itemname", ItemName.findItemName(id));
        uiModel.addAttribute("itemId", id);
        return "itemnames/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itemnames", ItemName.findItemNameEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ItemName.countItemNames() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itemnames", ItemName.findAllItemNames(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "itemnames/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ItemName itemName, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itemName);
            return "itemnames/update";
        }
        uiModel.asMap().clear();
        itemName.merge();
        return "redirect:/itemnames/" + encodeUrlPathSegment(itemName.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ItemName.findItemName(id));
        return "itemnames/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ItemName itemName = ItemName.findItemName(id);
        itemName.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/itemnames";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("itemName_createdon_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("itemName_lastupdatedon_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, ItemName itemName) {
        uiModel.addAttribute("itemName", itemName);
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
