package com.org.web;
import java.util.List;
import java.util.Set;
import javax.management.Query;
import javax.naming.Context;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.org.constants.ManagedDocumentType;
import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.Item;
import com.org.entity.ManagedDocument;
import com.org.entity.MeasurementSheet;
import com.org.service.blobstore.FileStorageService;
import com.org.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/aggreements")
@Controller
@RooWebScaffold(path = "aggreements", formBackingObject = Aggreement.class)
public class AggreementController {

    @Autowired
    private FileStorageService fileStorageService;
    
    @RequestMapping(params = "redirect", produces = "text/html", method=RequestMethod.GET)
    public String createFromBillform(@RequestParam(value = "redirect", required = true)String redirect,Model uiModel) {
        populateEditForm(uiModel, new Aggreement());
        uiModel.addAttribute("redirect",redirect);
        return "aggreements/create";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        LogUser user = LogUser.findLogUsersByUsernameEquals(username).getSingleResult();
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("aggreements", Aggreement.findAggreementsByLogUser(user, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Aggreement.countAggreements() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("aggreements", Aggreement.findAggreementsByLogUser(user, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "aggreements/list";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        Aggreement aggreement = QueryUtil.getUniqueResult(Aggreement.findAggreementsByIdAndLogUser(id, getCurrentUser()));
        uiModel.addAttribute("aggreement", aggreement);
        uiModel.addAttribute("manageddocuments", ManagedDocument.findManagedDocumentsByAggreement(aggreement).getResultList());
        uiModel.addAttribute("itemId", id);
        return "aggreements/show";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Aggreement aggreement = Aggreement.findAggreement(id);
        Set<MeasurementSheet> msheets = aggreement.getMeasurementSheets();
        if (msheets != null) {
            for (MeasurementSheet msheet : msheets) {
                boolean fileDeleted = fileStorageService.delete(msheet.getStorageFileName());
                if (!fileDeleted) {
                    // TODO:add to orphan files database;
                }
            }
        }
        aggreement.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/aggreements";
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@RequestParam(value = "redirect", required = false) String redirect, @Valid Aggreement aggreement, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        System.out.println("post called billformn"+redirect);
        LogUser user = LogUser.getCurrentUser();
    	if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, aggreement);
            return "aggreements/create";
        }
        uiModel.asMap().clear();
        aggreement.persist();
        if(redirect!=null) {
        	return "redirect:"+redirect;
        }
        
        //Craete folder for aggreement
        try {
        	ManagedDocument myDoc = new ManagedDocument();
            myDoc.setType(ManagedDocumentType.FOLDER);
            myDoc.setDescription(aggreement.getAggreementNum());
            ManagedDocument root = ManagedDocument.findManagedDocumentsByLogUserAndType(user, ManagedDocumentType.AGG_FOLDER).getSingleResult();
            myDoc.setParent(root);
            myDoc.merge();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return "redirect:/items/aggreement/" + encodeUrlPathSegment(aggreement.getId().toString(), httpServletRequest);
    }

    private LogUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        LogUser user = LogUser.findLogUsersByUsernameEquals(username).getSingleResult();
        return user;
    }
}
