const purchaseTable = document.getElementById("purchaseTable");

const is_edit = document.getElementById('page-type').value == "manage" ? true : false;

//delete를 넣어 드려야 함.

var purchaseTableManager = null;

if(is_edit){
    purchaseTableManager = new TableManager(purchaseTable,
        ["id", "productId", "productName", "at", "amount", "price", "purchasePrice", "company", "note"],
        ["구매번호", "품목번호", "품목명", "구매일시", "수량", "단가", "구매금액", "거래처명", "비고"],
        [4, 5, 6],
        new TableOnClickSet('openDeletePurchaseModal', 'id'));    
}else{
    purchaseTableManager = new TableManager(purchaseTable,
        ["id", "productId", "productName", "at", "amount", "price", "purchasePrice", "company", "note"],
        ["구매번호", "품목번호", "품목명", "구매일시", "수량", "단가", "구매금액", "거래처명", "비고"],
        [4, 5, 6]);    
}


purchaseTableManager.table_initiallize("purchaseTable");

const filter = document.getElementById("filter");
const searchElement = document.getElementById("search");

function filterByInput(){
    
    const searchVal = document.getElementById("search").value;
    const filterName = filter.value;

    var filteredDatas = doSearchFilter(purchaseDatas, filterName == "companyName" ? "company" : "productName", searchVal);

    purchaseTableManager.set_table_content(filteredDatas);
}

searchElement.addEventListener("input", filterByInput);
