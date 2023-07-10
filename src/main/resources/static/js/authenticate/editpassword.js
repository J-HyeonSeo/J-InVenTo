const idArea = document.getElementById("username");
const originPasswordArea = document.getElementById("origin-password");
const newPasswordArea = document.getElementById("new-password");

const editButton = document.getElementById("edit-password-btn");

function handleKey(event){
    if (event.key === "Enter") {
        event.preventDefault(); // 기본 동작(페이지 새로고침)을 막음
        editPasswordExecute();
    }
}

function editPasswordExecute(){

    const username = idArea.value;
    const originPassword = originPasswordArea.value;
    const newPassword = newPasswordArea.value;

    body = {
        'username' : username,
        'originPassword' : originPassword,
        'newPassword' : newPassword
    }

    requestExecute('/auth/user/update/password', 'put', body).then(response => {
        alert("비밀번호가 성공적으로 변경되었습니다.");
        window.location.assign("/login.html");
    }).catch(response => {
        alert(response);
    });

}

editButton.addEventListener('click', editPasswordExecute);
newPasswordArea.addEventListener('keydown', handleKey);

const pwChangeButton = document.getElementById("password-change");