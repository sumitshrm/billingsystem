package com.org.web;
import com.org.domain.LogUser;
import com.org.entity.Estimate;
import com.org.entity.ScheduledStatement;

import org.springframework.http.MediaType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/estimates")
@Controller
@RooWebScaffold(path = "estimates", formBackingObject = Estimate.class)
public class EstimateController {
	
	 @RequestMapping(value="/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,  consumes=MediaType.APPLICATION_JSON_VALUE)
	    @ResponseBody
	    public String createStatement(@RequestBody Estimate estimate) {
		 	
		 	estimate.persist();
		 	estimate.setUrl("ESTIMATES_"+estimate.getId()+".xlsx");
		 	estimate.merge();
	    	System.out.println(estimate);
	    	return "books/dar/index";
	    }
	 
	 @RequestMapping(produces = "text/html")
	    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
	        if (page != null || size != null) {
	            int sizeNo = size == null ? 10 : size.intValue();
	            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
	            uiModel.addAttribute("estimates", Estimate.findEstimatesByLogUser(LogUser.getCurrentUser(), sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
	            float nrOfPages = (float) Estimate.countEstimates() / sizeNo;
	            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
	        } else {
	            uiModel.addAttribute("estimates", Estimate.findEstimatesByLogUser(LogUser.getCurrentUser()));
	        }
	        return "estimates/list";
	    }
}
