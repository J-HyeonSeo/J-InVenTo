import { TableManager, TableOnClickSet } from "../manager/table-manager.js";
import { loadOutboundDetailData } from "../loader/outbound-detail-loader.js";

export class OutboundDetailModalViewer{

    constructor(){
        this.outboundDetailDatas = null;
    }

    initailize(modalID, callback, outboundId){
        //모달폼 오픈 => 테이블 초기화
        openModal(modalID);
        const outboundDetailTable = document.getElementById('outbound-detail-table');
        outboundDetailTable.firstElementChild.firstElementChild.innerHTML = '';
        outboundDetailTable.lastElementChild.innerHTML = '';

        //테이블 매니저 생성
        this.outboundDetailTableManager = new TableManager(
            outboundDetailTable,
            ["id", "stockId", "productName", "company", "amount", "price"],
            ["상세 ID", "재고 ID", "품목명", "거래처명", "수량", "단가"],
            [4, 5],
            new TableOnClickSet(callback, null));
        //테이블 초기화
        this.outboundDetailTableManager.table_initiallize();
        
        loadOutboundDetailData(outboundId).then(response => {
            //데이터 테이블에 할당함.
            this.outboundDetailDatas = response;
            this.outboundDetailTableManager.set_table_content(response);
        }).catch(error => {
            alert(error.errorMessage);
            return;
        })

    }

}