import { OutboundDataLoader } from "../loader/outbound-loader.js";
import { TableManager, TableOnClickSet } from "../manager/table-manager.js";
import {doSearchFilter} from "../viewer/mainfilter.js";
import { OutboundManager } from "../manager/outbound-manager.js";
import { OutboundDetailModalViewer } from "../modals/outbound-detail-modal-viewer.js";

class OutboundViewer{

    constructor(){
        this.outboundTable = document.getElementById('outbound-table');
        this.outboundDataLoader = new OutboundDataLoader();
        this.outboundTableManager = null;

        this.filter = document.getElementById("filter");
        this.searchElement = document.getElementById("search");
        this.outboundDatas = null;
        this.loadBtn = document.getElementById('outbound-load-btn');

        this.resetSortBtn = document.getElementById('reset-sort-btn');
    }

    viewerInitailize(){

        if(document.getElementById('page-type').value == "manage" ? true : false){

            this.outboundManager = new OutboundManager();
            this.outboundManager.initialize();

            this.outboundTableManager = new TableManager(this.outboundTable,
                ["id", "productName", "destination", "amount", "price", "outboundPrice", "at", "note"],
                ["출고 ID", "품목명", "출고처", "출고수량", "출고단가", "출고금액", "출고일", "비고"],
                [3, 4, 5],
                new TableOnClickSet(this.outboundManager.openDetailModal.bind(this.outboundManager), 'id'));    
        }else{

            this.outboundDetailModalViewer = new OutboundDetailModalViewer();

            this.outboundTableManager = new TableManager(this.outboundTable,
                ["id", "productName", "destination", "amount", "price", "outboundPrice", "at", "note"],
                ["출고 ID", "품목명", "출고처", "출고수량", "출고단가", "출고금액", "출고일", "비고"],
                [3, 4, 5],
                new TableOnClickSet(this.showOnly.bind(this), 'id'));      
        }
        
        this.outboundTableManager.table_initiallize();
        this.searchElement.addEventListener("input", this.filterByInput.bind(this));
        this.loadBtn.addEventListener('click', this.clickLoadBtn.bind(this));
        this.resetSortBtn.addEventListener('click', this.outboundTableManager.resetOrder.bind(this.outboundTableManager));
    }

    showOnly(id){
        this.outboundDetailModalViewer.initailize('outbound-detail-modal', null, id);
    }

    clickLoadBtn(){
        this.outboundDataLoader.loadOutboundData()
        .then(response => {
            this.outboundDatas = response;
            this.filterByInput();
        }).catch(error => {
            alert(error);
        });
    }

    filterByInput(){
    
        const searchVal = this.searchElement.value;
        const filterName = this.filter.value;
    
        const filteredDatas = doSearchFilter(this.outboundDatas, filterName, searchVal);
    
        this.outboundTableManager.set_table_content(filteredDatas);
    }

}

window.addEventListener('load', function(){
    const outboundViewer = new OutboundViewer();
    outboundViewer.viewerInitailize();
})