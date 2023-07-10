import { PlanDataLoader } from "../loader/plan-loader.js";
import { TableManager, TableOnClickSet } from "../manager/table-manager.js";
import { doSearchFilter} from "../viewer/mainfilter.js";
import { PlanManager } from "../manager/plan-manager.js";

class PlanViewer{

    constructor(){
        this.planDataLoader = new PlanDataLoader('startDate', 'endDate');
        this.planLoadBtn = document.getElementById('plan-load-btn');

        this.planTable = document.getElementById('plan-table');
        this.planTableManager = null;

        this.planDatas = [];

        this.filter = document.getElementById("filter");
        this.searchElement = document.getElementById("search");
        this.resetSortBtn = document.getElementById('reset-sort-btn');
    }

    viewerInitailize(){

        this.planLoadBtn.addEventListener('click', this.clickLoadBtn.bind(this));

        if(document.getElementById('page-type').value == "manage" ? true : false){

            this.planManager = new PlanManager();
            this.planManager.initailize();

            this.planTableManager = new TableManager(this.planTable,
                ["id", "productName", "amount", "destination", "due"],
                ["계획번호", "품목명", "수량", "출고처", "출고예정일"],
                [2],
                new TableOnClickSet(this.planManager.openUpdatePlanModal.bind(this.planManager), null));

        }else{

            this.planTableManager = new TableManager(this.planTable,
                ["id", "productName", "amount", "destination", "due"],
                ["계획번호", "품목명", "수량", "출고처", "출고예정일"],
                [2]);

        }

        this.planTableManager.table_initiallize();
        this.resetSortBtn.addEventListener('click', this.planTableManager.resetOrder.bind(this.planTableManager));
        this.searchElement.addEventListener('input', this.filterByInput.bind(this));
    }

    clickLoadBtn(){

        this.planDataLoader.loadPlanData()
        .then(response => {
            this.planDatas = response;
            this.filterByInput();
        }).catch(error => {
            alert(error);
        });

    }

    filterByInput(){
        const searchVal = this.searchElement.value;
        const filterName = this.filter.value;
    
        const filteredDatas = doSearchFilter(this.planDatas, filterName, searchVal);
    
        this.planTableManager.set_table_content(filteredDatas);
    }

}

window.addEventListener('load', function(){

    const planViewer = new PlanViewer();
    planViewer.viewerInitailize();

});