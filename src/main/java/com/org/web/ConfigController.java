package com.org.web;
import com.org.domain.Config;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/configs")
@Controller
@RooWebScaffold(path = "configs", formBackingObject = Config.class)
public class ConfigController {
}
