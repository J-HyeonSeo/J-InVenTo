//모달 폼을 입력으로 받아서, 테이블 Content에 넣고 띄워줌.
import { loadStocksLotByProductId } from "../loader/stocks-lot-loader.js";
import { TableManager, TableOnClickSet } from "../manager/table-manager.js";

export class StocksLotModalViewer{

    constructor(){
        this.lotDatas = null;
    }

    initailize(modalID, productId, callback){
        //모달폼 오픈 => 테이블 초기화
        openModal(modalID);
        const stocksLotTable = document.getElementById('stocks-lot-table');
        stocksLotTable.firstElementChild.firstElementChild.innerHTML = '';
        stocksLotTable.lastElementChild.innerHTML = '';

        //테이블 매니저 생성
        this.stocksLotTableManager = new TableManager(
            stocksLotTable,
            ["id", "productName", "amount", "price", "lot", "company"],
            ["재고 ID", "품목명", "수량", "단가", "로트", "거래처명"],
            [2, 3],
            new TableOnClickSet(callback, null));
        //테이블 초기화
        this.stocksLotTableManager.table_initiallize();
        
        loadStocksLotByProductId(productId).then(response => {
            //데이터 테이블에 할당함.
            this.lotDatas = response;
            this.stocksLotTableManager.set_table_content(response);
        }).catch(error => {
            alert(error.errorMessage);
            return;
        })


    }

}