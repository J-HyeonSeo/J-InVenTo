package com.jhsfully.inventoryManagement.viewcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainViewController {

    @GetMapping("/")
    public String getMainPage(){
        return "redirect:/page/main";
    }

    @GetMapping("/page/main")
    public String mainPage(Principal principal){
        return "main";
//        if(principal == null || principal.getName() == null || principal.getName().trim().equals("")){
//            return "login.html";
//        }
//        return "main.html";
    }

    @GetMapping("/login")
    public String loginPage(){
        System.out.println("/login/132입장함.");
        return "login";
    }

}
