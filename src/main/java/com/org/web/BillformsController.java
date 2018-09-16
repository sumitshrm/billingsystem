package com.org.web;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.BillformsTo;
import com.org.entity.MaterialEntry;
import com.org.entity.Template;
import com.org.report.service.AbstractGeneratorServiceV2;
import com.org.service.BillformsService;
import com.org.service.DocumentService;

@RequestMapping("/billforms/**")
@Controller
public class BillformsController {
	
	final static Logger logger = Logger.getLogger(BillformsController.class);
	
	@Autowired
	private BillformsService billformsService;

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    /*@RequestMapping
    public String index() {
    	
        return "billforms/index";
    }*/
    
    @RequestMapping(method = RequestMethod.GET)
    public String show(Model uiModel){
    	System.out.println("Billlform Controller");
    	uiModel.addAttribute("billforms", new BillformsTo());
    	uiModel.addAttribute("aggreements",Aggreement.findAggreementsByLogUser(LogUser.getCurrentUser()).getResultList());
    	
    	return "billforms/index";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String generateBillForm(@Valid BillformsTo billforms, BindingResult bindingResult, Model uiModel,  HttpServletResponse response){
    	System.out.println("Billform generated for Aggreement : " + billforms.getAggreement().getAggreementNum());
    	System.out.println("Billform generated for REports : " + billforms.getSelectedReports());
    	logger.info(billforms.getSelectedReports());
    	 try {
             Template template = billformsService.createBillformsDocument(billforms);
             response.setHeader("Content-Disposition", "attachment;filename=\"" + template.getName()+".xlsx" + "\"");
             OutputStream out = response.getOutputStream();
             response.setContentType(template.getContentType());
             IOUtils.copy(new ByteArrayInputStream(template.getContent()), out);
             out.flush();
         } catch (Exception e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
        return null;
    }
}
