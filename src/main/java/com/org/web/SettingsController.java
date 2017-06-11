package com.org.web;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
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
import com.org.service.BillformsService;
import com.org.service.DocumentService;
import com.org.settings.entity.ResetPasswordTo;
import com.org.view.MessageType;
import com.org.view.MessageVo;

@RequestMapping("/settings")
@Controller
public class SettingsController {
	


	 @RequestMapping(value = "/resetpassword", produces = "text/html")
	    public String createForm(Model uiModel) {
	        ResetPasswordTo resetPasswordTo = new ResetPasswordTo();
	        uiModel.addAttribute("resetPassword", resetPasswordTo);
	        return "settings/resetpassword";
	    }
    
	 @RequestMapping(value = "/resetpassword", method = RequestMethod.POST, produces = "text/html")
	    public String create(ResetPasswordTo resetPassword, Model uiModel, HttpServletRequest httpServletRequest) {
	       
	        uiModel.asMap().clear();
	        System.out.println("password updated");
	        System.out.println(resetPassword);
	        LogUser currentUser = LogUser.getCurrentUser();
	        if(!currentUser.getPassword().equals(resetPassword.getCurrentPassword())){
	        	uiModel.addAttribute("resetPassword", new ResetPasswordTo());
	            uiModel.addAttribute("message", new MessageVo("settings_reset_password_failed_current_password", MessageType.ERROR));
	        	return "settings/resetpassword";
	        }else if(!resetPassword.getNewPassword().equals(resetPassword.getConfirmPassword())){
	            uiModel.addAttribute("message", new MessageVo("settings_reset_password_failed_confirm_password", MessageType.ERROR));
	            uiModel.addAttribute("resetPassword", new ResetPasswordTo(resetPassword.getCurrentPassword()));
	            return "settings/resetpassword";
	        }else{
	        	try{
		        	currentUser.setPassword(resetPassword.getNewPassword());
			        currentUser.merge();
		            uiModel.addAttribute("message", new MessageVo("settings_reset_password_successful", MessageType.SUCCESS));
		        }catch(Exception e){
		            uiModel.addAttribute("message", new MessageVo("notification_message_error", MessageType.ERROR).addParam(e.getMessage()));
		        }
	        }
	        return "index";
	    }
    
}
