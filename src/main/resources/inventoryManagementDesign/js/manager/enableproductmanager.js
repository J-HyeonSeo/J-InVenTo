//모달 폼을 입력으로 받아서, 테이블 Content에 넣고 띄워줌.

var enableProductTableManager = null;
var enableProductTable = null;
var enableProductModal = null;

function initalizeProductModal(modalID, callbackString){

    //모달폼 오픈 => 테이블 초기화
    enableProductModal = openModal(modalID);
    enableProductTable = enableProductModal.firstElementChild.lastElementChild.firstElementChild;
    enableProductTable.firstElementChild.firstElementChild.innerHTML = '';
    enableProductTable.lastElementChild.innerHTML = '';

    enableProductTableManager = new TableManager(
        enableProductTable,
        ["id", "name", "company", "spec"],
        ["품목 ID", "품목명", "거래처명", "규격"],
        [],
        new TableOnClickSet(callbackString, 'id'));
    
    enableProductTableManager.table_initiallize("enableProductTableManager");

    var enableProducts = null;
    
    loadEnableProducts().then(response => {

        //데이터 테이블에 할당함.
        enableProducts = response;
        enableProductTableManager.set_table_content(enableProducts);

    }).catch(error => {
        alert(error.errorMessage);
        return;
    })

    //input이벤트 셋팅.
    const filter = document.getElementById("product-filter");
    const searchElement = document.getElementById("product-search");

    searchElement.addEventListener("input", function(){
        const searchVal = document.getElementById("search").value;
        const filterName = filter.value;

        filteredDatas = filterName == "productName" ? doSearchFilter(enableProducts, "name", searchVal) : doSearchFilter(enableProducts, "company", searchVal);

        enableProductTableManager.set_table_content(filteredDatas);
    });

}


