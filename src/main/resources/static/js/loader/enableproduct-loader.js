import { requestExecute } from "../authenticate/request.js";

export async function loadEnableProducts(){
    try{
        const response = await requestExecute("/product/enable", "get", null);
        return response;
    }catch(error){
        throw error;
    }
}