import { LogDataLoader } from "../loader/log-loader.js";
import { TableManager, TableOnClickSet } from "../manager/table-manager.js";
import {doSearchFilter} from "../viewer/mainfilter.js";

class LogViewer{

    constructor(){
        this.logTable = document.getElementById('log-table');
        this.logDataLoader = new LogDataLoader();
        this.logTableManager = null;
        this.filter = document.getElementById("filter");
        this.searchElement = document.getElementById("search");
        this.logDatas = null;
        this.loadBtn = document.getElementById('log-load-btn');
        this.resetSortBtn = document.getElementById('reset-sort-btn');
    }

    viewerInitailize(){

        this.logTableManager = new TableManager(this.logTable,
            ["id", "username", "signature", "requestUrl", "method", "elapsed", "at", "_success"],
            ["로그번호", "아이디", "실행된메소드", "요청URL", "요청방식", "소요시간(ms)", "요청시각", "수행여부"],
            []);    
        
        this.logTableManager.table_initiallize();
        this.searchElement.addEventListener("input", this.filterByInput.bind(this));
        this.loadBtn.addEventListener('click', this.clickLoadBtn.bind(this));
        this.resetSortBtn.addEventListener('click', this.logTableManager.resetOrder.bind(this.logTableManager));
    }

    clickLoadBtn(){
        this.logDataLoader.loadLogData()
        .then(response => {
            this.logDatas = response;
            this.filterByInput();
        }).catch(error => {
            alert(error);
        });
    }

    filterByInput(){
    
        const searchVal = this.searchElement.value;
        const filterName = this.filter.value;
    
        const filteredDatas = doSearchFilter(this.logDatas, filterName, searchVal);
    
        this.logTableManager.set_table_content(filteredDatas);
    }

}

window.addEventListener('load', function(){
    const logViewer = new LogViewer();
    logViewer.viewerInitailize();
})
