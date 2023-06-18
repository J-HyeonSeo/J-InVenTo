const idArea = document.getElementById("username");
const passwordArea = document.getElementById("password");

const loginButton = document.getElementById("login");

function handleKey(event){
    if (event.key === "Enter") {
        event.preventDefault(); // 기본 동작(페이지 새로고침)을 막음
        loginExecute();
    }
}

function loginExecute(){

    const username = idArea.value;
    const password = passwordArea.value;

    body = {
        'username' : username,
        'password' : password
    }

    requestExecute('/auth/signin', 'post', body).then(response => {
        //로그인 성공
        localStorage.setItem("accessToken", 'Bearer ' + response.accessToken);
        localStorage.setItem("refreshToken", 'Bearer ' + response.refreshToken);
        
        alert("성공적으로 로그인하였습니다.");

        window.location.assign("/main.html");
    }).catch(response => {
        alert(response.errorMessage);
    });

}

loginButton.addEventListener('click', loginExecute);
passwordArea.addEventListener('keydown', handleKey);

const pwChangeButton = document.getElementById("password-change");