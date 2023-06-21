import { BASE_URL } from "../main.js";

export async function requestExecute(url, methodType, bodyContent){
    
    var options;

    if(methodType == "get"){
        options = {
            method: "GET",
            headers: {
                AccessToken : localStorage.getItem('accessToken'),
                RefreshToken : localStorage.getItem('refreshToken'),
                'Content-type' : 'application/json'
            }
        }
    }else{
        options = {
            method: methodType,
            headers: {
                AccessToken : localStorage.getItem('accessToken'),
                RefreshToken : localStorage.getItem('refreshToken'),
                'Content-type' : 'application/json'
            },
            body: JSON.stringify(bodyContent)
        }
    }

    const headers = {
        'AccessToken' : localStorage.getItem('accessToken'),
        'RefreshToken' : localStorage.getItem('refreshToken'),
        'Content-type' : 'application/json'
    }

    

    try{
        // let response = await fetch(BASE_URL + url, {method: methodType, headers, body: JSON.stringify(body)});

        const response = await fetch(BASE_URL + url, options);

        if(response.ok){ //정상 응답.
            const data = await response.json().catch(() => {return response});
            return data;
        }else if(response.status == 403){
            throw("권한을 소유하고 있지 않습니다.");
        }else if(response.status == 401){
            alert("로그인이 필요합니다.");
            window.location.assign("/login.html");
        }else{
            const data = await response.json();
            throw(data);
        }
    }catch(error){
        throw(error);
    }



}