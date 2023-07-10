import { requestExecute } from "../authenticate/request.js";

export async function loadUserDatas(){
    try{
        const response = await requestExecute("/auth/admin" , "get", null);
        return response;
    }catch(error){
        throw error;
    }
}