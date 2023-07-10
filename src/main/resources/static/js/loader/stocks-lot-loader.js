import { requestExecute } from "../authenticate/request.js";

export async function loadStocksLotByProductId(productId){
    try{
        const response = await requestExecute("/stocks/" + productId, "get", null);
        return response;
    }catch(error){
        throw error;
    }
}