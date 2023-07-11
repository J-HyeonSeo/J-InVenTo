package com.jhsfully.inventoryManagement.viewcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShowAndManageViewController {

    //purchase
    @GetMapping("/page/purchase-show")
    public String getPurchaseShow(){
        return "purchase-show";
    }
    @GetMapping("/page/purchase-manage")
    public String getPurchaseManage(){
        return "purchase-manage";
    }
    @GetMapping("/page/purchase-chart")
    public String getPurchaseChart(){
        return "purchase-chart";
    }


    //inbound
    @GetMapping("/page/inbound-show")
    public String getInboundShow(){
        return "inbound-show";
    }
    @GetMapping("/page/inbound-manage")
    public String getInboundManage(){
        return "inbound-manage";
    }


    //outbound
    @GetMapping("/page/outbound-show")
    public String getOutboundShow(){
        return "outbound-show";
    }
    @GetMapping("/page/outbound-manage")
    public String getOutboundManage(){
        return "outbound-manage";
    }
    @GetMapping("/page/outbound-chart")
    public String getOutboundChart(){
        return "outbound-chart";
    }

    //plan
    @GetMapping("/page/plan-show")
    public String getPlanShow(){
        return "plan-show";
    }
    @GetMapping("/page/plan-manage")
    public String getPlanManage(){
        return "plan-manage";
    }

    //stocks
    @GetMapping("/page/stocks-show")
    public String getStocksShow(){
        return "stocks-show";
    }

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

    //

}
