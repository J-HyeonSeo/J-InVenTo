package com.jhsfully.inventoryManagement.viewcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShowAndManageViewController {

    //product
    @GetMapping("/page/product-show")
    public String getProductShow(){
        return "product-show";
    }
    @GetMapping("/page/product-manage")
    public String getProductManage(){
        return "product-manage";
    }

    //bom
    @GetMapping("/page/bom-show")
    public String getBomShow(){
        return "bom-show";
    }
    @GetMapping("/page/bom-manage")
    public String getBomManage(){
        return "bom-manage";
    }


}
