package com.jhsfully.inventoryManagement.viewcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainViewController {

    //Main View

    @GetMapping("/")
    public String getMainPage(){
        return "main";
    }

    //Login View

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/edit-password")
    public String editPasswordPage(){
        return "edit-password";
    }

}
