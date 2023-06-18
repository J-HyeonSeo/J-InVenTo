const productTable = document.getElementById("product-table");

const productTableManager = new TableManager(productTable, 
    ["id", "name", "company", "price", "spec", "enabled"], 
    ["품목 ID" , "품목명", "거래처명", "단가", "규격", "사용여부"], 
    [3]);

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

    var filteredDatas = [];

    //input filtering
    productDatas.forEach(item => {
        switch(filterName){
            case "productName":
                if(item.name.includes(searchVal)){
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

    productTableManager.set_table_content(filteredDatas);
}

searchElement.addEventListener("input", filterByInput);