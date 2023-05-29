package com.jhsfully.inventoryManagement.viewcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PurchaseViewController {

    @GetMapping("/")
    public String getMainPage(){
        return "main.html";
    }

}
