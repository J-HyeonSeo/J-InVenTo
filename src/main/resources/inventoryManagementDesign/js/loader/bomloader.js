var is_loaded = false;
var bomTopDatas;

requestExecute("/bom", "get", null)
.then(response => {
    bomTopDatas = response;
  is_loaded = true;
  filterByInput();
}).catch(error => {
  alert(error);
  alert("데이터를 가져오는 중에 오류가 발생하였습니다.");
});