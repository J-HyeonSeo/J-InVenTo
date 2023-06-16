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
        }else if(response.status == 403){ //forbidden error logic 추가해야함.
            throw("권한을 소유하고 있지 않습니다.");
        }else if(response.status == 401){
            alert("로그인이 필요합니다.");
            //window.location.assign("/login.html");
        }
    }catch(error){
        throw(error);
    }



}