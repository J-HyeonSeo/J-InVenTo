const productTable = document.getElementById("product-table");
const is_edit = document.getElementById('page-type').value == "manage" ? true : false;
var productTableManager = null;


if(is_edit){
    productTableManager = new TableManager(productTable, 
        ["id", "name", "company", "price", "spec", "enabled"], 
        ["품목 ID" , "품목명", "거래처명", "단가", "규격", "사용여부"], 
        [3],
        new TableOnClickSet('clickUpdateProductBtn', null));
}else{
    productTableManager = new TableManager(productTable, 
        ["id", "name", "company", "price", "spec", "enabled"], 
        ["품목 ID" , "품목명", "거래처명", "단가", "규격", "사용여부"], 
        [3]);
}


productTableManager.table_initiallize("productTableManager");

//필터링 동작을 인식하여, 데이터를 필터링 -> 테이블매니저로 데이터 표시 요청.

const filter = document.getElementById("filter");
const searchElement = document.getElementById("search");

function filterByInput(){
    if(!is_loaded){
        alert("불러온 데이터가 없습니다.");
        return;
    }

    const searchVal = document.getElementById("search").value;
    const filterName = filter.value;

    filteredDatas = filterName == "productName" ? doSearchFilter(productDatas, "name", searchVal) : doSearchFilter(productDatas, "company", searchVal);

    productTableManager.set_table_content(filteredDatas);
}

searchElement.addEventListener("input", filterByInput);