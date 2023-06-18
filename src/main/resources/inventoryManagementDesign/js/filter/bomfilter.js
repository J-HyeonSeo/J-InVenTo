const tbody = document.getElementById("product-content");
const colNames = ["productId", "productName"];
const displayColNames = ["품목 번호" , "품목명"];
table_initiallize(displayColNames);

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

    set_table_content(colNames.length, filteredDatas, colNames, []);
}

searchElement.addEventListener("input", filterByInput);