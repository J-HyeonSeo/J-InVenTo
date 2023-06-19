const bomTable = document.getElementById("bomTable");

const bomTableManager = new TableManager(
    bomTable,
    ["productId", "productName"],
    ["품목 번호" , "품목명"],
    [],
    new TableOnClickSet('loadBomTreeData', 'productId')
);

bomTableManager.table_initiallize('bomTableManager');

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
    bomTopDatas.forEach(item => {
        switch(filterName){
            case "productName":
                if(item.productName.includes(searchVal)){
                    filteredDatas.push(item);
                }
                break;
        }
    });

    bomTableManager.set_table_content(filteredDatas);
}

searchElement.addEventListener("input", filterByInput);