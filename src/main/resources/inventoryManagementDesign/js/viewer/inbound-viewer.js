import { InboundDataLoader } from "../loader/inbound-loader.js";
import { TableManager, TableOnClickSet } from "../manager/table-manager.js";
import {doSearchFilter} from "../viewer/mainfilter.js";
import { InboundManager } from "../manager/inbound-manager.js";

class InboundViewer{

    constructor(){
        this.inboundTable = document.getElementById('inboundTable');
        this.inboundDataLoader = new InboundDataLoader();
        this.inboundTableManager = null;

        this.filter = document.getElementById("filter");
        this.searchElement = document.getElementById("search");
        this.inboundDatas = null;
        this.loadBtn = document.getElementById('inbound-load-btn');

        this.resetSortBtn = document.getElementById('reset-sort-btn');
    }

    viewerInitailize(){

        if(document.getElementById('page-type').value == "manage" ? true : false){

            this.inboundManager = new InboundManager();
            this.inboundManager.initailize();

            this.inboundTableManager = new TableManager(this.inboundTable,
                ["id", "purchaseId", "productName", "inboundAt", "purchasedAt", "amount", "price", "company", "note"],
                ["입고번호", "구매번호", "품목명", "입고일시", "구매일시", "수량", "단가", "거래처명", "비고"],
                [4, 5, 6],
                new TableOnClickSet(this.inboundManager.openDeleteInboundModal.bind(this.inboundManager), 'id'));    
        }else{
            this.inboundTableManager = new TableManager(this.inboundTable,
                ["id", "purchaseId", "productName", "inboundAt", "purchasedAt", "amount", "price", "company", "note"],
                ["입고번호", "구매번호", "품목명", "입고일시", "구매일시", "수량", "단가", "거래처명", "비고"],
                [4, 5, 6]);     
        }
        
        this.inboundTableManager.table_initiallize();

        //이벤트 추가
        this.searchElement.addEventListener("input", this.filterByInput.bind(this));
        this.loadBtn.addEventListener('click', this.clickLoadBtn.bind(this));
        this.resetSortBtn.addEventListener('click', this.inboundTableManager.resetOrder.bind(this.inboundTableManager));
    }

    clickLoadBtn(){
        this.inboundDataLoader.loadInboundData()
        .then(response => {
            this.inboundDatas = response;
            this.filterByInput();
        }).catch(error => {
            alert(error);
        });
    }

    filterByInput(){
    
        const searchVal = this.searchElement.value;
        const filterName = this.filter.value;
    
        const filteredDatas = doSearchFilter(this.inboundDatas, filterName == "companyName" ? "company" : "productName", searchVal);
    
        this.inboundTableManager.set_table_content(filteredDatas);
    }

}

window.addEventListener('load', function(){
    const inboundViewer = new InboundViewer();
    inboundViewer.viewerInitailize();
})