async function loadEnableProducts(){
    try{
        const response = await requestExecute("/product/enable", "get", null);
        return response;
    }catch(error){
        throw error;
    }
}