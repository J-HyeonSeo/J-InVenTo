import { doSearchFilter } from "../viewer/mainfilter.js";
import { TableManager, TableOnClickSet } from "../manager/table-manager.js";
import { PurchaseDataLoader } from "../loader/purchase-loader.js";

export class PurchaseModalViewer{

    constructor(){
        this.searchElement = document.getElementById("purchase-search");
        this.purchaseDatas = null;
        this.purchaseDataLoader = new PurchaseDataLoader('purchase-startDate', 'purchase-endDate');
        this.purchaseLoadBtn = document.getElementById('purchase-load-btn');
    }

    initailize(modalID, callback){

        openModal(modalID);
        const purchaseTable = document.getElementById('purchase-table');
        purchaseTable.firstElementChild.firstElementChild.innerHTML = '';
        purchaseTable.lastElementChild.innerHTML = '';

        //테이블 매니저 생성
        this.purchaseTableManager = new TableManager(
            purchaseTable,
            ["id", "productName", "amount", "canAmount", "at"],
            ["구매번호", "품목명", "구매수량", "입고가능수량", "구매일시"],
            [],
            new TableOnClickSet(callback, null));
        //테이블 초기화
        this.purchaseTableManager.table_initiallize();

        //검색 이벤트 추가
        this.searchElement.addEventListener("input", this.filterByInput.bind(this));

        //구매 데이터 로드 이벤트 추가

        this.purchaseLoadBtn.addEventListener('click', () => {
            this.purchaseDataLoader.loadPurchaseData().then(response => {
                //데이터 테이블에 할당함.
                this.purchaseDatas = response;
                this.purchaseTableManager.set_table_content(response);
            }).catch(error => {
                alert(error);
                return;
            })
        })

    }

    filterByInput(){
        const searchVal = this.searchElement.value;
        const filteredDatas = doSearchFilter(this.purchaseDatas, "productName", searchVal);
        this.purchaseTableManager.set_table_content(filteredDatas);
    }

}