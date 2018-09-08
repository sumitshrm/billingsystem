package com.org.web;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.LabourEntry;

@RequestMapping("/calculator/eisdiscalculator")
@Controller
public class EisDisCalculatorController {

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }
    
    @RequestMapping
    public String index(ModelMap modelMap) {
    	
    	List<Aggreement> aggreements = Aggreement.findAggreementsByLogUser(LogUser.getCurrentUser()).getResultList();
    	modelMap.addAttribute("labour", new LabourEntry() );
    	modelMap.addAttribute("aggreements", aggreements);
        return "calculator/eisdiscalculator/index";
    }
}
