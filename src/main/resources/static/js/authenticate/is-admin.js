import { requestExecute } from "./request.js";

export async function isAdmin(){

    requestExecute("/auth/admin/ping", "get", null)
    .then(response => {
    }).catch(error => {
        alert(error);
        alert("경고! 관리자가 아닙니다.");
        window.location.href = "/";
    })

}