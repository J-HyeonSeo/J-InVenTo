const tbody = document.getElementById("product-content");

function addProductsFromDatas(datas){
    tbody.innerHTML = "";

    var manageHTML = "";

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

//검색 필터링

const filter = document.getElementById("filter");
const searchElement = document.getElementById("search");

searchElement.addEventListener("input", function(event){

    if(!is_loaded){
        alert("불러온 데이터가 없습니다.");
        return;
    }

    //console.log(event.target.value);
    const searchVal = event.target.value;
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
    addProductsFromDatas(filteredDatas);
});