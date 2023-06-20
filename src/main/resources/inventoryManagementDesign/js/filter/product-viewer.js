import{TableManager, TableOnClickSet} from '../manager/tablemanager.js';
import{loadAllProductData} from '../loader/productloader.js';

class ProductViewer{

    constructor(){
        this.productTable = document.getElementById("product-table");
        this.productTableManager = null;
        this.is_edit = document.getElementById('page-type').value == "manage" ? true : false;
        this.filter = document.getElementById("filter");
        this.searchElement = document.getElementById("search");
        this.productDatas = null;
    }

    viewerInitailize(){
        if(this.is_edit){
            this.productTableManager = new TableManager(this.productTable, 
                ["id", "name", "company", "price", "spec", "enabled"], 
                ["품목 ID" , "품목명", "거래처명", "단가", "규격", "사용여부"], 
                [3],
                new TableOnClickSet('clickUpdateProductBtn', null));
        }else{
            this.productTableManager = new TableManager(this.productTable, 
                ["id", "name", "company", "price", "spec", "enabled"], 
                ["품목 ID" , "품목명", "거래처명", "단가", "규격", "사용여부"], 
                [3]);
        }
        this.productTableManager.table_initiallize();
        this.searchElement.addEventListener("input", this.filterByInput.bind(this));

        loadAllProductData()
        .then(response => {
            this.productDatas = response;
            this.filterByInput();
        }).catch(error => {
            alert("데이터를 가져오는 도중에 문제가 발생하였습니다.");
        })

    }

    filterByInput(){

        const searchVal = this.searchElement.value;
        const filterName = this.filter.value;
    
        const filteredDatas = filterName == "productName" ? 
        doSearchFilter(this.productDatas, "name", searchVal) : 
        doSearchFilter(this.productDatas, "company", searchVal);
    
        this.productTableManager.set_table_content(filteredDatas);

    }

}

window.addEventListener('load', function(){
    const productViewer = new ProductViewer();
    productViewer.viewerInitailize();
});

// const productTable = document.getElementById("product-table");
// const is_edit = document.getElementById('page-type').value == "manage" ? true : false;
// let productTableManager = null;


// if(is_edit){
//     productTableManager = new TableManager(productTable, 
//         ["id", "name", "company", "price", "spec", "enabled"], 
//         ["품목 ID" , "품목명", "거래처명", "단가", "규격", "사용여부"], 
//         [3],
//         new TableOnClickSet('clickUpdateProductBtn', null));
// }else{
//     productTableManager = new TableManager(productTable, 
//         ["id", "name", "company", "price", "spec", "enabled"], 
//         ["품목 ID" , "품목명", "거래처명", "단가", "규격", "사용여부"], 
//         [3]);
// }

// productTableManager.table_initiallize("productTableManager");

// const filter = document.getElementById("filter");
// const searchElement = document.getElementById("search");

// function filterByInput(){

//     const searchVal = document.getElementById("search").value;
//     const filterName = filter.value;

//     filteredDatas = filterName == "productName" ? doSearchFilter(productDatas, "name", searchVal) : doSearchFilter(productDatas, "company", searchVal);

//     productTableManager.set_table_content(filteredDatas);
// }

// searchElement.addEventListener("input", filterByInput);