package com.org.web;
import com.org.domain.Config;
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

@RequestMapping("/configs")
@Controller
@RooWebScaffold(path = "configs", formBackingObject = Config.class)
public class ConfigController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Config config, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, config);
            return "configs/create";
        }
        uiModel.asMap().clear();
        config.persist();
        return "redirect:/configs/" + encodeUrlPathSegment(config.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Config());
        return "configs/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("config", Config.findConfig(id));
        uiModel.addAttribute("itemId", id);
        return "configs/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("configs", Config.findConfigEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) Config.countConfigs() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("configs", Config.findAllConfigs(sortFieldName, sortOrder));
        }
        return "configs/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Config config, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, config);
            return "configs/update";
        }
        uiModel.asMap().clear();
        config.merge();
        return "redirect:/configs/" + encodeUrlPathSegment(config.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Config.findConfig(id));
        return "configs/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Config config = Config.findConfig(id);
        config.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/configs";
    }

	void populateEditForm(Model uiModel, Config config) {
        uiModel.addAttribute("config", config);
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
