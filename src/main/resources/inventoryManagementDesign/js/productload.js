var is_loaded = false;
var productDatas;

fetch(BASE_URL + "/product")
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json();
  })
  .then(data => {
    productDatas = data;
    is_loaded = true;
    addProductsFromDatas(productDatas);
  })
  .catch(error => {
    alert("데이터를 가져오는 중에 오류가 발생하였습니다.");
  });