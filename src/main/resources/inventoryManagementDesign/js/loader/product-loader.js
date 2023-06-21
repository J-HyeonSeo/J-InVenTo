//서버에서 데이터를 가져옴..
import { requestExecute } from "../authenticate/request.js";

export async function loadAllProductData(){

  try{
    const response = await requestExecute("/product/all", "get", null);
    return response;
  }catch(error){
    throw error;
  }

}
