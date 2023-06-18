const purchaseTable = document.getElementById("purchaseTable");

const purchaseTableManager = new TableManager(purchaseTable,
    ["id", "productId", "productName", "at", "amount", "price", "purchasePrice", "company", "note"],
    ["구매번호", "품목번호", "품목명", "구매일시", "수량", "단가", "구매금액", "거래처명", "비고"],
    [4, 5, 6]);

purchaseTableManager.table_initiallize("purchaseTable");

const filter = document.getElementById("filter");
const searchElement = document.getElementById("search");

function filterByInput(){
    
    const searchVal = document.getElementById("search").value;
    const filterName = filter.value;

    var filteredDatas = [];

    //input filtering
    purchaseDatas.forEach(item => {
        switch(filterName){
            case "productName":
                if(item.productName.includes(searchVal)){
                    filteredDatas.push(item);
                }
                break;
            case "companyName":
                if(item.company.includes(searchVal)){
                    filteredDatas.push(item);
                }
                break;
        }
    });

    purchaseTableManager.set_table_content(filteredDatas);
}

searchElement.addEventListener("input", filterByInput);
