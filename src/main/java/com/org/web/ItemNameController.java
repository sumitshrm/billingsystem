package com.org.web;
import com.org.entity.ItemName;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/itemnames")
@Controller
@RooWebScaffold(path = "itemnames", formBackingObject = ItemName.class)
public class ItemNameController {
}
