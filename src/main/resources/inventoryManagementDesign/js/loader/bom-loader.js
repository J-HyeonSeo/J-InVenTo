import {requestExecute} from "../authenticate/request.js";

export async function loadBomTopDatas(){

  try{
    const response = await requestExecute("/bom", "get", null);
    return response;
  }catch(error){
    throw error;
  }

}
