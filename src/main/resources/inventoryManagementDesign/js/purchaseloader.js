//서버에서 데이터를 가져옴.

var is_loaded = false;
var purchaseDatas;

const startCalendar = document.getElementById("startDate");
const endCalendar = document.getElementById("endDate");

function loadPurchaseData(){

    const startDate = startCalendar.value.replace(/-/g, '');
    const endDate = endCalendar.value.replace(/-/g, '');

    if(startDate == null || startDate.trim() == ""){
        alert("날짜가 지정되지 않았습니다.");
        return;
    }

    if(endDate == null || endDate.trim() == ""){
        alert("날짜가 지정되지 않았습니다.");
        return;
    }

    requestExecute("/purchase?startDate=" + startDate + "&endDate=" + endDate, "get", null)
    .then(response => {
    purchaseDatas = response;
    is_loaded = true;
    filterByInput();
    }).catch(error => {
    alert(error);
    alert("데이터를 가져오는 중에 오류가 발생하였습니다.");
    });
}