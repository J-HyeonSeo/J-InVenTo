import{TableManager, TableOnClickSet} from '../manager/table-manager.js';
import{loadAllProductData} from '../loader/product-loader.js';
import { doSearchFilter } from './mainfilter.js';
import { ProductManager } from '../manager/product-manager.js';

class ProductViewer{

    constructor(){
        this.productTable = document.getElementById("product-table");
        this.filter = document.getElementById("filter");
        this.searchElement = document.getElementById("search");
        this.productDatas = null;

        this.resetSortBtn = document.getElementById('reset-sort-btn');
    }

    viewerInitailize(){
        if(document.getElementById('page-type').value == "manage" ? true : false){

            this.productManager = new ProductManager();
            this.productManager.managerInitailize();

            this.productTableManager = new TableManager(this.productTable, 
                ["id", "name", "company", "price", "spec", "enabled"], 
                ["품목 ID" , "품목명", "거래처명", "단가", "규격", "사용여부"], 
                [3],
                new TableOnClickSet(this.productManager.clickUpdateProductBtn.bind(this.productManager), null));
        }else{
            this.productTableManager = new TableManager(this.productTable, 
                ["id", "name", "company", "price", "spec", "enabled"], 
                ["품목 ID" , "품목명", "거래처명", "단가", "규격", "사용여부"], 
                [3]);
        }
        this.productTableManager.table_initiallize();
        this.searchElement.addEventListener("input", this.filterByInput.bind(this));
        this.resetSortBtn.addEventListener('click', this.productTableManager.resetOrder.bind(this.productTableManager));

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
