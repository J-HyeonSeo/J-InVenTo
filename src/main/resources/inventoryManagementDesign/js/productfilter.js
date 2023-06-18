const tbody = document.getElementById("product-content");
const colNames = ["id", "name", "company", "price", "spec", "enabled"];
table_initiallize(["품목 ID" , "품목명", "거래처명", "단가", "규격", "사용여부"]);

//필터링 동작을 인식하여, 데이터를 필터링 -> 테이블매니저로 데이터 표시 요청.

const filter = document.getElementById("filter");
const searchElement = document.getElementById("search");

//checkbox
const productAsc = document.getElementById("productAsc");
const companyAsc = document.getElementById("companyAsc");
const priceAsc = document.getElementById("priceAsc");
const priceDesc = document.getElementById("priceDesc");

//checkbox events
productAsc.addEventListener('change', filterByInput);
companyAsc.addEventListener('change', filterByInput);

priceAsc.addEventListener('change', function(){
    if(priceAsc.checked){
        priceDesc.checked = false;
    }
    filterByInput();
})

priceDesc.addEventListener('change', function(){
    if(priceDesc.checked){
        priceAsc.checked = false;
    }
    filterByInput();
})

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

    //ascending or descending filtering
    if(productAsc.checked){
        filteredDatas.sort((a, b) => a.name.localeCompare(b.name));
    }

    if(companyAsc.checked){
        filteredDatas.sort((a, b) => a.company.localeCompare(b.company));
    }

    if(priceAsc.checked){
        filteredDatas.sort((a, b) => a.price - b.price);
    }

    if(priceDesc.checked){
        filteredDatas.sort((a, b) => b.price - a.price);
    }

    set_table_content(colNames.length, filteredDatas, colNames, [3]);
}

searchElement.addEventListener("input", filterByInput);