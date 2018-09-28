package com.org.web;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.Document;
import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import com.org.entity.MeasurementSheet;
import com.org.entity.Template;
import com.org.excel.util.TemplateType;
import com.org.service.DocumentService;
import com.org.util.QueryUtil;
import com.org.view.MessageType;
import com.org.view.MessageVo;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

@RequestMapping("/items")
@Controller
@RooWebScaffold(path = "items", formBackingObject = Item.class)
public class ItemController {

    @Autowired
    private DocumentService documentService;

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("item", QueryUtil.getUniqueResult(Item.findItemsByIdAndLogUser(id, LogUser.getCurrentUser())));
        uiModel.addAttribute("itemId", id);
        return "items/show";
    }

    @RequestMapping(value = "/aggreement/{id}", produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, @PathVariable("id") long aggreementid, Model uiModel) {
        Aggreement aggreement = Aggreement.findAggreement(aggreementid);
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("items", Item.findItemsByAggreementAndLogUser(Aggreement.findAggreement(aggreementid), LogUser.getCurrentUser(), "sortOrder", "asc").setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Item.countItems() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("items", Item.findItemsByAggreementAndLogUser(aggreement, LogUser.getCurrentUser(), "sortOrder", "asc").getResultList());
        }
        uiModel.addAttribute("aggreement", aggreement);
        return "items/list";
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, QueryUtil.getUniqueResult(Item.findItemsByIdAndLogUser(id, LogUser.getCurrentUser())));
        return "items/update";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Item item, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        Item merged = Item.findItem(item.getId());
        merged.setDescription(item.getDescription());
        merged.setDrsCode(item.getDrsCode());
        merged.setDsrRate(item.getDsrRate());
        merged.setFullRate(item.getFullRate());
        merged.setPartRate(item.getPartRate() == null ? item.getFullRate() : item.getPartRate());
        merged.setQuantity(item.getQuantity());
        merged.setQuantityPerUnit(item.getQuantityPerUnit());
        merged.setUnit(item.getUnit());
        uiModel.asMap().clear();
        if (!merged.validate()) {
            populateEditForm(uiModel, merged);
            uiModel.addAttribute("message", new MessageVo("notification_message", MessageType.ERROR).addParam(merged.getValidationMessage()));
            return "items/update";
        }
        merged.persist();
        return "redirect:/items/" + encodeUrlPathSegment(item.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = { "form", "aggreement" }, produces = "text/html")
    public String createFromAggreementForm(Model uiModel, @RequestParam("aggreement") Long aggreementId, HttpServletResponse response) {
        Aggreement aggreement = Aggreement.findAggreement(aggreementId);
        try {
            Template itemsheet = documentService.createItemsDocument(aggreement);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + itemsheet.getFileName() + "\"");
            OutputStream out = response.getOutputStream();
            response.setContentType(itemsheet.getContentType());
            IOUtils.copy(new ByteArrayInputStream(itemsheet.getContent()), out);
            out.flush();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Item item, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, item);
            return "items/create";
        }
        uiModel.asMap().clear();
        item.persist();
        return "redirect:/items?form&aggreement=" + item.getAggreement().getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Item item = Item.findItem(id);
        item.remove();
        uiModel.asMap().clear();
        return "redirect:/items/aggreement/" + item.getAggreement().getId().toString();
    }

    void populateEditForm(Model uiModel, Item item) {
        uiModel.addAttribute("item", item);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Item());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Aggreement.countAggreements() == 0) {
            dependencies.add(new String[] { "aggreement", "aggreements" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "items/create";
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
