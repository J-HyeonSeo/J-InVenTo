const idArea = document.getElementById("username");
const passwordArea = document.getElementById("password");

const loginButton = document.getElementById("login");

loginButton.addEventListener('click', function(){

    const username = idArea.value;
    const password = passwordArea.value;

    body = {
        'username' : username,
        'password' : password
    }

    requestExecute('/auth/signin', 'post', body).then(response => {
        //로그인 성공
        localStorage.setItem("accessToken", response.accessToken);
        localStorage.setItem("refreshToken", response.refreshToken);
        
        alert("성공적으로 로그인하였습니다.");

        window.location.assign("/main.html");
    }).catch(response => {
        alert(response.errorMessage);
    });

});

const pwChangeButton = document.getElementById("password-change");