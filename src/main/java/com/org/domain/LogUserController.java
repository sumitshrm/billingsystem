package com.org.domain;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.org.view.MessageType;
import com.org.view.MessageVo;

@RequestMapping("/logusers")
@Controller
@RooWebScaffold(path = "logusers", formBackingObject = LogUser.class)
public class LogUserController {

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
        try {
            logUser.merge();
            uiModel.asMap().clear();
        } catch (Exception e) {
            populateEditForm(uiModel, logUser);
            uiModel.addAttribute("message", new MessageVo("log_user_duplicate_username", MessageType.ERROR));
            return "logusers/update";
        }
        return "redirect:/logusers/" + encodeUrlPathSegment(logUser.getId().toString(), httpServletRequest);
    }

    private boolean validateUser(Model uiModel, LogUser logUser) {
        if (LogUser.findLogUsersByUsernameEquals(logUser.getUsername()).getResultList().size() >= 1) {
            populateEditForm(uiModel, logUser);
            uiModel.addAttribute("message", new MessageVo("log_user_duplicate_username", MessageType.ERROR));
            return false;
        }
        if (logUser.getRoles() == null || logUser.getRoles().size() < 1) {
            populateEditForm(uiModel, logUser);
            uiModel.addAttribute("message", new MessageVo("log_user_roles_not_selected", MessageType.ERROR));
            return false;
        }
        return true;
    }
}
