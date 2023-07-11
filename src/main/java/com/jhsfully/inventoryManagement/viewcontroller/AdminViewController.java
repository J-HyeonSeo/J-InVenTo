package com.jhsfully.inventoryManagement.viewcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminViewController {

    //admin-log
    @GetMapping("/admin/page/admin-log")
    public String getLogPage(){
        return "admin-log";
    }

    //admin-register
    @GetMapping("/admin/page/admin-register")
    public String getRegisterPage(){
        return "admin-register";
    }

    //admin-modify
    @GetMapping("/admin/page/admin-modify")
    public String getModifyPage(){
        return "admin-modify";
    }

}
