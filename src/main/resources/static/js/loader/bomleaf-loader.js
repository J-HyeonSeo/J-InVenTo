import {requestExecute} from "../authenticate/request.js";

export async function loadBomLeafDatas(productId){

  try{
    const response = await requestExecute("/bom/leaf/" + productId , "get", null);

    response.forEach(item => {
      item.selectedCost = 0;
    });

    return response;
  }catch(error){
    throw error;
  }

}