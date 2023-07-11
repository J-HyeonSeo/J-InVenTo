import { requestExecute } from "./request.js";
import { isAdmin } from "./is-admin.js";

window.addEventListener('load', function(){

    isAdmin();

    const unsettedPermissionList = document.getElementById("unsetted-permissions");
    const settedPermissionList = document.getElementById("setted-permissions");
    const registerButton = document.getElementById("register");

    //미적용 권한에 이벤트 추가
    for(let item of unsettedPermissionList.children){
        item.addEventListener('click', setRole, {once: true});
    }

    //회원 가입 버튼에 이벤트 추가
    registerButton.addEventListener('click', requestRegister);

    function setRole(event){
        event.target.addEventListener('click', unSetRole, {once: true});
        settedPermissionList.appendChild(event.target);
    }

    function unSetRole(event){
        event.target.addEventListener('click', setRole, {once: true});
        unsettedPermissionList.appendChild(event.target);
    }

    function getRoles(){
        var res = []
        for(var ele of settedPermissionList.children){
            res.push(ele.getAttribute("data-role"));
        }
        return res;
    }


    function requestRegister(){
        var username = document.getElementById("username").value;
        var password = document.getElementById("password").value;
        var name = document.getElementById("name").value;
        var department = document.getElementById("department").value;

        if(username.trim() == ""){
            alert("아이디가 비어있습니다.");
            return;
        }
        if(password.trim() == ""){
            alert("비밀번호가 비어있습니다.");
            return;
        }
        if(name.trim() == ""){
            alert("이름이 비어있습니다.");
            return;
        }
        if(department.trim() == ""){
            alert("소속이 비어있습니다.");
            return;
        }

        const roles = getRoles();

        const body = {
            'username' : username,
            'password' : password,
            'name' : name,
            'department' : department,
            'roles' : roles
        };

        requestExecute("/auth/signup", "post", body).then(response => {
            alert("회원이 성공적으로 추가되었습니다.");
        }).catch(error =>{
                alert(error.errorMessage);
            }
        )
    }

});





