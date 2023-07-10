//서버에서 데이터를 가져옴..
import { requestExecute } from "../authenticate/request.js";

export async function loadOutboundDetailData(outboundId){

  try{
    const response = await requestExecute("/outbound/" + outboundId, "get", null);
    return response;
  }catch(error){
    throw error;
  }

}