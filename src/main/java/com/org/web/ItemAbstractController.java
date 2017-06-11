package com.org.web;
import com.org.entity.ItemAbstract;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/itemabstracts")
@Controller
@RooWebScaffold(path = "itemabstracts", formBackingObject = ItemAbstract.class)
public class ItemAbstractController {
}
