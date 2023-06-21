//서버에서 데이터를 가져옴..
import { requestExecute } from "../authenticate/request.js";

export async function loadAllProductData(){

  try{
    const response = requestExecute("/product/all", "get", null);
    return response;
  }catch(error){
    throw error;
  }

  // requestExecute("/product/all", "get", null)
  // .then(response => {
  //     return response;
  //   }).catch(error => {
  //     alert(error.errorMessage);
  // });
}
