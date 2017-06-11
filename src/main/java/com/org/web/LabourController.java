package com.org.web;
import com.org.entity.Labour;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/labours")
@Controller
@RooWebScaffold(path = "labours", formBackingObject = Labour.class)
public class LabourController {
}
