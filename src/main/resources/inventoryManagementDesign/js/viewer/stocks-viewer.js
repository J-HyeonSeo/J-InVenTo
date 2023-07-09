import{TableManager, TableOnClickSet} from '../manager/table-manager.js';
import { StocksLotModalViewer } from '../modals/stocks-lot-modal-viewer.js';
import { loadAllStockData } from '../loader/stocks-loader.js';
import { doSearchFilter } from './mainfilter.js';

class StocksViewer{

    constructor(){
        this.stocksTable = document.getElementById("stocks-table");
        this.filter = document.getElementById("filter");
        this.searchElement = document.getElementById("search");
        this.stockDatas = null;

        this.stocksLotModalViewer = new StocksLotModalViewer();
        this.resetSortBtn = document.getElementById('reset-sort-btn');
    }

    viewerInitailize(){

        this.stocksTableManager = new TableManager(this.stocksTable, 
            ["productId", "productName", "spec", "amount", "price", "lackAmount", "lackDate"], 
            ["품목 ID" , "품목명", "규격", "현재고", "단가", "부족수량", "재고소진일"], 
            [3, 4, 5],
            new TableOnClickSet(this.openLotModal.bind(this), "productId"));

        this.stocksTableManager.table_initiallize();
        this.searchElement.addEventListener("input", this.filterByInput.bind(this));
        this.resetSortBtn.addEventListener('click', this.stocksTableManager.bind(this.stocksTableManager));

        loadAllStockData()
        .then(response => {
            this.stockDatas = response;
            this.filterByInput();
        }).catch(error => {
            alert("데이터를 가져오는 도중에 문제가 발생하였습니다.");
        })

    }

    openLotModal(productId){
        this.stocksLotModalViewer.initailize("stocks-lot-modal", productId, null);
    }

    filterByInput(){

        const searchVal = this.searchElement.value;
        const filterName = this.filter.value;
    
        const filteredDatas = doSearchFilter(this.stockDatas, "productName", searchVal);
    
        this.stocksTableManager.set_table_content(filteredDatas);

    }

}

window.addEventListener('load', function(){
    const stocksViewer = new StocksViewer();
    stocksViewer.viewerInitailize();
});
