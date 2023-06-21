//모달 폼을 입력으로 받아서, 테이블 Content에 넣고 띄워줌.
import { loadEnableProducts } from "../loader/enableproduct-loader.js";
import { doSearchFilter } from "../viewer/mainfilter.js";
import { TableManager, TableOnClickSet } from "./table-manager.js";

export class EnableProductManager{

    constructor(){
        this.filter = document.getElementById("product-filter");
        this.searchElement = document.getElementById("product-search");
        this.enableProducts = null;
    }

    initailize(modalID, callback){
        //모달폼 오픈 => 테이블 초기화
        openModal(modalID);
        const enableProductTable = document.getElementById('product-table');
        enableProductTable.firstElementChild.firstElementChild.innerHTML = '';
        enableProductTable.lastElementChild.innerHTML = '';

        //테이블 매니저 생성
        this.enableProductTableManager = new TableManager(
            enableProductTable,
            ["id", "name", "company", "spec", "price"],
            ["품목 ID", "품목명", "거래처명", "규격", "단가"],
            [4],
            new TableOnClickSet(callback, null));
        //테이블 초기화
        this.enableProductTableManager.table_initiallize();
        
        loadEnableProducts().then(response => {
            //데이터 테이블에 할당함.
            this.enableProducts = response;
            this.enableProductTableManager.set_table_content(response);
        }).catch(error => {
            alert(error.errorMessage);
            return;
        })

        //검색
        this.searchElement.addEventListener("input", this.filterByInput.bind(this));
    }

    filterByInput(){
        const searchVal = this.searchElement.value;
        const filterName = this.filter.value;

        const filteredDatas = filterName == "productName" ? doSearchFilter(this.enableProducts, "name", searchVal) : doSearchFilter(this.enableProducts, "company", searchVal);

        this.enableProductTableManager.set_table_content(filteredDatas);
    }

}


