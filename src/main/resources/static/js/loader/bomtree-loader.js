// const is_edit = document.getElementById('page-type').value == "manage" ? true : false;

import { requestExecute } from "../authenticate/request.js";

export class BomTreeDataLoader{
    constructor(){
        this.latestProductId = null;
    }

    async loadBomTreeData(productId){
        if(productId == null){
            productId = this.latestProductId;
        }else{
            this.latestProductId = productId;
        }

        try{
            const response = await requestExecute("/bom/" + productId, "get", null);
            return response;
        }catch(error){
            throw error;
        }
    }
}
