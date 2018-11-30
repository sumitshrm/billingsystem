package com.org.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.org.domain.LogUser;
import com.org.entity.EstimateShared;
import com.org.entity.ManagedDocumentShared;
import com.org.entity.MeasurementSheetShared;

@RequestMapping("/sharedDocuments/**")
@Controller
public class SharedDocumentsController {

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }
    

    @RequestMapping("/inbox")
    public String inbox(Model uiModel) {
    	LogUser currentUser = LogUser.getCurrentUser();
    	uiModel.addAttribute("managedDocuments", ManagedDocumentShared.findManagedDocumentSharedsBySharedWith(currentUser, "sharedDate", "DESC").getResultList());
    	uiModel.addAttribute("estimates", EstimateShared.findEstimateSharedsBySharedWith(currentUser, "sharedDate", "DESC").getResultList());
    	uiModel.addAttribute("measurementSheets", MeasurementSheetShared.findMeasurementSheetSharedsBySharedWith(currentUser, "sharedDate", "DESC").getResultList());
        return "sharedDocuments/inbox";
    }
    
    @RequestMapping("/outbox")
    public String outbox(Model uiModel) {
    	LogUser currentUser = LogUser.getCurrentUser();
    	uiModel.addAttribute("managedDocuments", ManagedDocumentShared.findManagedDocumentSharedsBySharedBy(currentUser, "sharedDate", "DESC").getResultList());
    	uiModel.addAttribute("estimates", EstimateShared.findEstimateSharedsBySharedBy(currentUser, "sharedDate", "DESC").getResultList());
    	uiModel.addAttribute("measurementSheets", MeasurementSheetShared.findMeasurementSheetSharedsBySharedBy(currentUser, "sharedDate", "DESC").getResultList());
        return "sharedDocuments/outbox";
    }
    
    @RequestMapping("/measurementsheet/{id}")
    public String showMeasurementSheet(@PathVariable("id") Long id) {
    	MeasurementSheetShared shared = MeasurementSheetShared.findMeasurementSheetShared(id);
    	return "redirect:/measurementsheets/"+shared.getMeasurementSheet().getId();
    }
    
    @RequestMapping("/estimates/{id}")
    public String showEstimate(@PathVariable("id") Long id) {
    	EstimateShared shared = EstimateShared.findEstimateShared(id);
    	return "redirect:/estimates/"+shared.getEstimate().getId();
    }
    
    @RequestMapping("/manageddocuments/{id}")
    public String showManagedDocument(@PathVariable("id") Long id) {
    	ManagedDocumentShared shared = ManagedDocumentShared.findManagedDocumentShared(id);
    	return "redirect:/manageddocuments/"+shared.getManagedDocument().getId();
    }
}
