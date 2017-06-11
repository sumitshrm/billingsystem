package com.org.web;
import com.org.entity.LabourSupplier;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/laboursuppliers")
@Controller
@RooWebScaffold(path = "laboursuppliers", formBackingObject = LabourSupplier.class)
public class LabourSupplierController {
}
