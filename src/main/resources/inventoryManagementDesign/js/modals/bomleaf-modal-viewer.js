import { loadBomLeafDatas } from "../loader/bomleaf-loader.js";
import { SelectLotModalViewer } from "./select-lot-modal-viewer.js";
import { TableManager, TableOnClickSet } from "../manager/table-manager.js";

export class BomLeafModalViewer{

    constructor(){
        this.bomLeafDatas = null;
        this.selectLotModalViewer = new SelectLotModalViewer();

        this.lots = {};
    }

    initailize(modalID, callback, productId, amount){

        this.productId = productId;
        this.callback = callback;

        //모달폼 오픈 => 테이블 초기화
        openModal(modalID);
        const bomLeafTable = document.getElementById('bom-leaf-table');
        bomLeafTable.firstElementChild.firstElementChild.innerHTML = '';
        bomLeafTable.lastElementChild.innerHTML = '';

        //테이블 매니저 생성
        this.bomLeafTableManager = new TableManager(
            bomLeafTable,
            ["productId", "productName", "cost", "selectedCost"],
            ["품목 ID", "품목명", "필요 소요 수량", "선택 소요 수량"],
            [4],
            new TableOnClickSet(this.openSelectLotModal.bind(this)));
        //테이블 초기화
        this.bomLeafTableManager.table_initiallize();
        
        loadBomLeafDatas(productId).then(response => {
            //데이터 테이블에 할당함.

            response.forEach(item => {
                item.cost *= parseInt(amount);
            });

            this.bomLeafDatas = response;
            this.bomLeafTableManager.set_table_content(response);
        }).catch(error => {
            alert(error.errorMessage);
            return;
        })
    }

    openSelectLotModal(event){

        const targetTr = event.target.parentNode;
        const productId = targetTr.children[0].textContent;
        const requires = targetTr.children[2].textContent;

        this.selectLotModalViewer.initialize('select-lot-modal', this.selectedLotAfter.bind(this), productId, requires);
    }

    selectedLotAfter(productId, seleted){

        const data = []

        seleted.forEach(item => {
            data.push({
                stockId: item.id,
                amount: item.amount
            })
        });

        this.lots.productId = data;

    }

    clickExecuteBtn(){

    }

}