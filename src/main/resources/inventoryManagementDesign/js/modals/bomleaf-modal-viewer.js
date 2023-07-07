import { Parsing } from "../util/parsing.js";
import { loadBomLeafDatas } from "../loader/bomleaf-loader.js";
import { SelectLotModalViewer } from "./select-lot-modal-viewer.js";
import { TableManager, TableOnClickSet } from "../manager/table-manager.js";

export class BomLeafModalViewer{

    constructor(){
        this.bomLeafDatas = null;
        this.selectLotModalViewer = new SelectLotModalViewer();

        this.destinationInput = document.getElementById('destination');
        this.noteInput = document.getElementById('note');
        this.executeBtn = document.getElementById('execute-outbonud-btn');

        this.lots = {};
    }

    initailize(modalID, callback, productId, amount){

        this.productId = productId;
        this.callback = callback;
        this.amount = amount; //출고 필요 수량

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
                item.cost *= Parsing.parseDouble(amount);
            });

            this.bomLeafDatas = response;
            this.bomLeafTableManager.set_table_content(response);
        }).catch(error => {
            alert(error.errorMessage);
            return;
        })

        //이벤트 추가
        this.executeBtn.addEventListener('click', this.clickExecuteBtn.bind(this));
    }

    openSelectLotModal(event){

        const targetTr = event.target.parentNode;
        const productId = targetTr.children[0].textContent;
        const requires = targetTr.children[2].textContent;

        this.selectLotModalViewer.initialize('select-lot-modal', this.selectedLotAfter.bind(this), productId, requires);
    }

    selectedLotAfter(productId, seleted){

        hideModal('select-lot-modal');

        //현재 데이터에서, 해당되는 값 변경해야함.

        this.bomLeafDatas.forEach(item => {
            if(item.productId == productId){
                item.selectedCost = item.cost;
            }
        })

        this.bomLeafTableManager.set_table_content(this.bomLeafDatas);

        const data = []

        //서버에 맞게 데이터 변환 수행
        seleted.forEach(item => {
            data.push({
                stockId: parseInt(item.id),
                amount: Parsing.parseDouble(item.amount)
            })
        });

        //productId를 기준으로, 그룹화 해서 데이터 보관.
        this.lots[productId] = data;

    }

    clickExecuteBtn(event){

        event.stopPropagation();

        if(this.destinationInput.value.trim() == ''){
            alert('출고처를 입력해주세요.');
            return;
        }

        for(let i = 0; i < this.bomLeafDatas.length; i++){
            if(this.bomLeafDatas[i].cost != this.bomLeafDatas[i].selectedCost){
                alert("필요 수량과 선택 수량이 일치하지 않아 출고가 불가능합니다.");
                return;
            }
        }

        const stocks = [];

        Object.values(this.lots).forEach(list => {
            list.forEach(item => {
                stocks.push(item);
            })
        })

        const body = {
            productId: parseInt(this.productId),
            amount: Parsing.parseDouble(this.amount),
            destination: this.destinationInput.value,
            note: this.noteInput.value,
            stocks: stocks
        };

        this.callback(body);
    }

}