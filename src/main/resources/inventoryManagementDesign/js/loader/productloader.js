//서버에서 데이터를 가져옴.

var is_loaded = false;
var productDatas;

requestExecute("/product/all", "get", null)
.then(response => {
  productDatas = response;
  is_loaded = true;
  filterByInput();
  }).catch(error => {
  alert(error);
  alert("데이터를 가져오는 중에 오류가 발생하였습니다.");
});