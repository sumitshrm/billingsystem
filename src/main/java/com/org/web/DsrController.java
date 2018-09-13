package com.org.web;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.hibernate.annotations.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.org.service.blobstore.FileStorageService;

@RequestMapping("/books/dsr")
@Controller
public class DsrController {
 
	@Autowired
	private FileStorageService fileStorageService;
	
    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    @RequestMapping
    public String index(Model uiModel) {
        return "books/dsr/index";
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
}
