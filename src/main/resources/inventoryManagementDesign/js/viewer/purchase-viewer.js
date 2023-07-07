import { PurchaseDataLoader } from "../loader/purchase-loader.js";
import { TableManager, TableOnClickSet } from "../manager/table-manager.js";
import {doSearchFilter} from "../viewer/mainfilter.js";
import { PurchaseManager } from "../manager/purchase-manager.js";

class PurchaseViewer{

    constructor(){
        this.purchaseTable = document.getElementById('purchaseTable');
        this.purchaseDataLoader = new PurchaseDataLoader('startDate', 'endDate');
        this.purchaseTableManager = null;
        this.filter = document.getElementById("filter");
        this.searchElement = document.getElementById("search");
        this.purchaseDatas = null;
        this.loadBtn = document.getElementById('purchase-load-btn');
    }

    viewerInitailize(){

        if(document.getElementById('page-type').value == "manage" ? true : false){

            this.purchaseManager = new PurchaseManager();
            this.purchaseManager.initailize();

            this.purchaseTableManager = new TableManager(this.purchaseTable,
                ["id", "productId", "productName", "at", "amount", "canAmount", "price", "purchasePrice", "company", "note"],
                ["구매번호", "품목번호", "품목명", "구매일시", "수량", "입고가능수량", "단가", "구매금액", "거래처명", "비고"],
                [4, 5, 6, 7],
                new TableOnClickSet(this.purchaseManager.openDeletePurchaseModal.bind(this.purchaseManager), 'id'));    
        }else{
            this.purchaseTableManager = new TableManager(purchaseTable,
                ["id", "productId", "productName", "at", "amount", "canAmount", "price", "purchasePrice", "company", "note"],
                ["구매번호", "품목번호", "품목명", "구매일시", "수량", "입고가능수량", "단가", "구매금액", "거래처명", "비고"],
                [4, 5, 6, 7]);    
        }
        
        this.purchaseTableManager.table_initiallize();
        this.searchElement.addEventListener("input", this.filterByInput.bind(this));
        this.loadBtn.addEventListener('click', this.clickLoadBtn.bind(this));
    }

    clickLoadBtn(){
        this.purchaseDataLoader.loadPurchaseData()
        .then(response => {
            this.purchaseDatas = response;
            this.filterByInput();
        }).catch(error => {
            alert(error);
        });
    }

    filterByInput(){
    
        const searchVal = this.searchElement.value;
        const filterName = this.filter.value;
    
        const filteredDatas = doSearchFilter(this.purchaseDatas, filterName == "companyName" ? "company" : "productName", searchVal);
    
        this.purchaseTableManager.set_table_content(filteredDatas);
    }

}

window.addEventListener('load', function(){
    const purchaseViewer = new PurchaseViewer();
    purchaseViewer.viewerInitailize();
})
