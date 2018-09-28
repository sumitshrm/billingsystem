package com.org.web;
import com.org.entity.Template;
import com.org.excel.util.TemplateType;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
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

@RequestMapping("/templates")
@Controller
@RooWebScaffold(path = "templates", formBackingObject = Template.class)
public class TemplateController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Template template, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, template);
            return "templates/create";
        }
        uiModel.asMap().clear();
        template.persist();
        return "redirect:/templates/" + encodeUrlPathSegment(template.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Template());
        return "templates/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("template", Template.findTemplate(id));
        uiModel.addAttribute("itemId", id);
        return "templates/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("templates", Template.findTemplateEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) Template.countTemplates() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("templates", Template.findAllTemplates(sortFieldName, sortOrder));
        }
        return "templates/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Template template, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, template);
            return "templates/update";
        }
        uiModel.asMap().clear();
        template.merge();
        return "redirect:/templates/" + encodeUrlPathSegment(template.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Template.findTemplate(id));
        return "templates/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Template template = Template.findTemplate(id);
        template.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/templates";
    }

	void populateEditForm(Model uiModel, Template template) {
        uiModel.addAttribute("template", template);
        uiModel.addAttribute("templatetypes", Arrays.asList(TemplateType.values()));
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
