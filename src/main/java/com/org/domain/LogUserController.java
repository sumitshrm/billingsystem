package com.org.domain;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.org.report.service.AbstractGeneratorServiceV2;
import com.org.view.MessageType;
import com.org.view.MessageVo;

@RequestMapping("/logusers")
@Controller
@RooWebScaffold(path = "logusers", formBackingObject = LogUser.class)
public class LogUserController {
	
	final static Logger logger = Logger.getLogger(LogUserController.class);

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        LogUser user = new LogUser();
        user.setEnabled(true);
        populateEditForm(uiModel, user);
        return "logusers/create";
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LogUser logUser, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, logUser);
            return "logusers/create";
        }
        if (!validateUser(uiModel, logUser)) {
            return "logusers/create";
        }
        uiModel.asMap().clear();
        try {
            logUser.persist();
        } catch (ConstraintViolationException e) {
            populateEditForm(uiModel, logUser);
            uiModel.addAttribute("message", new MessageVo("log_user_duplicate_username", MessageType.ERROR));
            return "logusers/update";
        }
        return "redirect:/logusers/" + encodeUrlPathSegment(logUser.getId().toString(), httpServletRequest);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LogUser logUser, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, logUser);
            return "logusers/update";
        }
        if (!validateUser(uiModel, logUser)) {
        	System.out.println("invalid user");
        	 populateEditForm(uiModel, logUser);
            return "logusers/update";
        }
        try {
            logUser.merge();
            uiModel.asMap().clear();
        } catch (Exception e) {
            populateEditForm(uiModel, logUser);
            uiModel.addAttribute("message", new MessageVo("log_user_duplicate_username", MessageType.ERROR));
            return "logusers/update";
        }
        System.out.println("success");
        return "redirect:/logusers/" + encodeUrlPathSegment(logUser.getId().toString(), httpServletRequest);
    }

    private boolean validateUser(Model uiModel, LogUser logUser) {
    	List<LogUser> users=LogUser.findLogUsersByEmailAddress(logUser.getEmailAddress()).getResultList();
    	if(users.size()>=1) {
    		if(logUser.getId()==null) {
    			populateEditForm(uiModel, logUser);
                uiModel.addAttribute("message", new MessageVo("email_address_already_registered", MessageType.ERROR));
                return false;
    		}else if(users.get(0).getId().longValue()!=logUser.getId().longValue()){
    			populateEditForm(uiModel, logUser);
                uiModel.addAttribute("message", new MessageVo("email_address_already_registered", MessageType.ERROR));
                return false;
    		}
    	}
    	users=LogUser.findLogUsersByUsernameEquals(logUser.getUsername()).getResultList();
    	if(users.size()>=1) {
    		if(logUser.getId()==null) {
    			populateEditForm(uiModel, logUser);
                uiModel.addAttribute("message", new MessageVo("mobile_number_already_registered", MessageType.ERROR));
                return false;
    		}else if(users.get(0).getId().longValue()!=logUser.getId().longValue()){
    			populateEditForm(uiModel, logUser);
                uiModel.addAttribute("message", new MessageVo("mobile_number_already_registered", MessageType.ERROR));
                return false;
    		}
            
    	}
        if (logUser.getRoles() == null || logUser.getRoles().size() < 1) {
            populateEditForm(uiModel, logUser);
            uiModel.addAttribute("message", new MessageVo("log_user_roles_not_selected", MessageType.ERROR));
            return false;
        }
        return true;
    }
    
    
    @RequestMapping(value="/register", params = "form", produces = "text/html")
    public String registerForm(Model uiModel) {
        LogUser user = new LogUser();
        user.setEnabled(false);
        populateEditForm(uiModel, user);
        return "logusers/register";
    }
    
    @RequestMapping(value="/register", method = RequestMethod.POST, produces = "text/html")
    public String register(@Valid LogUser logUser, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        /*if (bindingResult.hasErrors()) {
        	System.out.println("binding result error");
            populateEditForm(uiModel, logUser);
            return "logusers/register";
        }*/
    	logUser.setEnabled(false);
    	LogUserRole role = LogUserRole.findLogUserRolesByRoleNameEquals(LogUserRole.USER_ROLE).getSingleResult();
    	Set<LogUserRole> roles = new HashSet<LogUserRole>();
    	roles.add(role);
    	logUser.setRoles(roles);
    	System.out.println(logUser);
        if (!validateUser(uiModel, logUser)) {
        	System.out.println("invalid user");
            return "logusers/register";
        }
        uiModel.asMap().clear();
        try {
            logUser.persist();
        } catch (ConstraintViolationException e) {
        	e.printStackTrace();
            populateEditForm(uiModel, logUser);
            uiModel.addAttribute("message", new MessageVo("log_user_duplicate_username", MessageType.ERROR));
            return "logusers/register";
        }
        return "redirect:/logusers/register?form&success";
    }
}
