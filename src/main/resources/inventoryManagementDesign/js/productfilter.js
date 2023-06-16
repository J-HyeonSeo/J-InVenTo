const tbody = document.getElementById("product-content");
const colNames = ["id", "name", "company", "price", "spec"];
table_initiallize(["품목 ID" , "품목명", "거래처명", "단가", "규격"]);

function addProductsFromDatas(datas){
    tbody.innerHTML = "";

    if(is_loaded){

        if(document.getElementById("page-type").value == "manage"){
            manageHTML = "<td><button>수정</button>  <button>삭제</button></td>"
        }

        for(let i = 0; i < datas.length; i++){
            const trElement = document.createElement('tr')
            trElement.innerHTML = "<td>"+ datas[i].id +"</td>" +
                                "<td>"+ datas[i].name + "</td>"+
                                "<td>"+ datas[i].company + "</td>"+
                                "<td>"+ datas[i].price.toLocaleString() + "</td>"+
                                "<td>"+ datas[i].spec + "</td>" + manageHTML;
            tbody.appendChild(trElement);
        }
    }
}

//필터링 동작을 인식하여, 데이터를 필터링 -> 테이블매니저로 데이터 표시 요청.

const filter = document.getElementById("filter");
const searchElement = document.getElementById("search");

function filterByInput(){
    if(!is_loaded){
        alert("불러온 데이터가 없습니다.");
        return;
    }

    //console.log(event.target.value);
    const searchVal = document.getElementById("search").value;
    const filterName = filter.value;

    var filteredDatas = [];

    for(let i = 0; i < productDatas.length; i++){
        switch(filterName){
            case "productName":
                if(productDatas[i].name.includes(searchVal)){
                    filteredDatas.push(productDatas[i])
                }
                break;
            case "companyName":
                if(productDatas[i].company.includes(searchVal)){
                    filteredDatas.push(productDatas[i])
                }
                break;
        }
    }
    set_table_content(colNames.length, filteredDatas);
}

searchElement.addEventListener("input", filterByInput);