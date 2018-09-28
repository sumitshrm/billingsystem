package com.org.web;
import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import com.org.entity.MeasurementSheet;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

@RequestMapping("/itemabstracts")
@Controller
@RooWebScaffold(path = "itemabstracts", formBackingObject = ItemAbstract.class)
public class ItemAbstractController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ItemAbstract itemAbstract, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itemAbstract);
            return "itemabstracts/create";
        }
        uiModel.asMap().clear();
        itemAbstract.persist();
        return "redirect:/itemabstracts/" + encodeUrlPathSegment(itemAbstract.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ItemAbstract());
        return "itemabstracts/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("itemabstract", ItemAbstract.findItemAbstract(id));
        uiModel.addAttribute("itemId", id);
        return "itemabstracts/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itemabstracts", ItemAbstract.findItemAbstractEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ItemAbstract.countItemAbstracts() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itemabstracts", ItemAbstract.findAllItemAbstracts(sortFieldName, sortOrder));
        }
        return "itemabstracts/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ItemAbstract itemAbstract, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itemAbstract);
            return "itemabstracts/update";
        }
        uiModel.asMap().clear();
        itemAbstract.merge();
        return "redirect:/itemabstracts/" + encodeUrlPathSegment(itemAbstract.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ItemAbstract.findItemAbstract(id));
        return "itemabstracts/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ItemAbstract itemAbstract = ItemAbstract.findItemAbstract(id);
        itemAbstract.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/itemabstracts";
    }

	void populateEditForm(Model uiModel, ItemAbstract itemAbstract) {
        uiModel.addAttribute("itemAbstract", itemAbstract);
        uiModel.addAttribute("items", Item.findAllItems());
        uiModel.addAttribute("measurementsheets", MeasurementSheet.findAllMeasurementSheets());
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
