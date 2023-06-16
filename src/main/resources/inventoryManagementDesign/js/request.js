let at = "";
let rt = "";

async function requestExecute(url, methodType, body){
    
    const headers = {
        'AccessToken' : at,
        'RefreshToken' : "",
        'Content-type' : 'application/json'
    }

    try{
        let response = await fetch(BASE_URL + url, {method: methodType, headers, body: JSON.stringify(body)});

        if(response.ok){ //정상 응답.
            const data = await response.json();
            return data;
        }else{ //forbidden error logic 추가해야함.
            alert(response.status);
            const data = await response.json();
            throw(data);
        }


    }catch(error){
        throw(error);
    }



}