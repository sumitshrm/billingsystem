package com.org.web;
import com.org.constants.Worksheets;
import com.org.domain.LogUser;
import com.org.entity.Estimate;
import com.org.entity.EstimateItem;
import com.org.entity.ScheduledStatement;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.MasterDataCellName;
import com.org.excel.util.XLColumnRange;
import com.org.service.EstimateService;
import com.org.service.blobstore.FileStorageService;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/estimates")
@Controller
@RooWebScaffold(path = "estimates", formBackingObject = Estimate.class)
public class EstimateController {
	
	@Autowired
	private EstimateService estimateService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	 @RequestMapping(value="/save", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<String> createStatement(@RequestBody Estimate estimate, HttpServletResponse httpServletResponse) throws Exception {
		 	try {
				estimateService.createEstimate(estimate);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
				
			}
	    	String resp = "{\"url\":\"/estimates/"+estimate.getId()+"\"}";
	    	return new ResponseEntity<String>(resp,HttpStatus.OK);
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
	            uiModel.addAttribute("estimates", Estimate.findEstimatesByLogUser(LogUser.getCurrentUser(), sortFieldName, sortOrder).getResultList());
	        }
	        return "estimates/list";
	    }
	 
	 @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
	        Estimate estimate = Estimate.findEstimate(id);
	        estimate.remove();
	        fileStorageService.delete(estimate.getUrl());
	        uiModel.asMap().clear();
	        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
	        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
	        return "redirect:/estimates";
	    }
	 
	 @RequestMapping(value="/update", method = RequestMethod.POST, produces = "text/html")
	    public String update(@Valid Estimate estimate, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
	        
	        uiModel.asMap().clear();
	        if(estimate.getContent().getSize()>0) {
	        	try {
					fileStorageService.doPost(estimate.getContent().getInputStream(), estimate.getUrl());
					
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	        estimate.merge();
	        return "redirect:/estimates/" + encodeUrlPathSegment(estimate.getId().toString(), httpServletRequest);
	    }
	
}
