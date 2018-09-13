package com.org.web;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.entity.Aggreement;
import com.org.entity.ScheduledStatement;
import com.org.service.blobstore.FileStorageService;

@RequestMapping("/books/dar")
@Controller
public class DarController {
	
	@Autowired
	private FileStorageService fileStorageService;

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    @RequestMapping
    public String index(Model uiModel) {
    	uiModel.addAttribute("scheduleModel", new ScheduledStatement());
        return "books/dar/index";
    }
    
    @RequestMapping("/download")
	public String download(@RequestParam(value = "file", required = false) String file, HttpServletResponse response, Model model) {
		try {
            response.setHeader("Content-Disposition", "attachment;filename=\"" + file + "\"");
			System.out.println("URL--------------"+file);
			fileStorageService.doGet(response, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,  consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createStatement(@RequestBody ScheduledStatement statement) {
    	System.out.println(statement);
    	return "books/dar/index";
    	
    	
    }
    
    @RequestMapping(value="/test",method=RequestMethod.POST)
    public  @ResponseBody String  getSearchUserProfiles(@RequestBody ScheduledStatement statement, HttpServletRequest request) {
        //String pName = search.getPName();
        //String lName = search.getLName();
    	System.out.println(statement);
        // your logic next
    	return "success";
    }
}
