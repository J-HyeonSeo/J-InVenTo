//서버에서 데이터를 가져옴..
import { requestExecute } from "../authenticate/request.js";

export async function loadAllStockData(){

  try{
    const response = await requestExecute("/stocks", "get", null);

    for(let i = 0; i < response.length; i++){
      if(response[i].lackDate == null){
        response[i].lackDate = '';
      }
    }

    return response;
  }catch(error){
    throw error;
  }

}
