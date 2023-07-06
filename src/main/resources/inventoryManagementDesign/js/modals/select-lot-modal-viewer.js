import { loadStocksLotByProductId } from "../loader/stocks-lot-loader.js";
import { TableManager, TableOnClickSet } from "../manager/table-manager.js";

export class SelectLotModalViewer{

    constructor(){
        this.lotDatas = null;
        this.selected = [];

        this.completeBtn = document.getElementById('select-complete-btn');
    }

    initialize(modalID, callback, productId, requires){
        this.selected = [];
        this.requires = parseInt(requires);
        this.nowCount = 0;
        this.productId = productId;
        this.callback = callback;
        //모달 오픈
        openModal(modalID);

        //테이블 정의 및 초기화
        const lotTable = document.getElementById('lot-table');
        lotTable.firstElementChild.firstElementChild.innerHTML = '';
        lotTable.lastElementChild.innerHTML = '';

        const selectedTable = document.getElementById('selected-table');
        selectedTable.firstElementChild.firstElementChild.innerHTML = '';
        selectedTable.lastElementChild.innerHTML = '';

        //테이블 매니저 생성 및 초기화
        this.lotTableManager = new TableManager(lotTable,
            ["id", "productName", "amount", "price", "lot", "company"],
            ["재고 ID", "품목명", "수량", "단가", "로트", "거래처명"],
            [2, 3],
            new TableOnClickSet(this.selectLot.bind(this)));
        this.lotTableManager.table_initiallize();
        
        this.selectedTableManager = new TableManager(selectedTable,
            ["id", "productName", "amount", "price", "lot", "company"],
            ["재고 ID", "품목명", "수량", "단가", "로트", "거래처명"],
            [2, 3],
            new TableOnClickSet(this.cancelLot.bind(this)));
        this.selectedTableManager.table_initiallize();
        
        //Lot 데이터 가져오기
        loadStocksLotByProductId(productId)
        .then(response => {
            this.lotDatas = response;
            this.lotTableManager.set_table_content(response);
        }).catch(error => {
            alert(error);
        });

        //이벤트 추가
        this.completeBtn.addEventListener('click', this.clickComplete.bind(this));
    }

    //우측으로 로트 이동
    selectLot(event){
        const targetTr = event.target.parentNode;

        const id = targetTr.children[0].textContent;
        const productName = targetTr.children[1].textContent;
        const amount = targetTr.children[2].textContent;
        const price = targetTr.children[3].textContent;
        const lot = targetTr.children[4].textContent;
        const company = targetTr.children[5].textContent;

        if(amount == 0){
            alert("해당 로트에서는 더 이상 출고가 불가능합니다.");
            return;
        }
        let inputAmount;
        while(true){
            inputAmount = prompt("몇 개를 출고 하시겠습니까?", amount);

            if(inputAmount == null){
                return;
            }

            if(!isNaN(inputAmount)){
                break;
            }else{
                alert("허용되지 않은 값입니다.");
            }
        }

        if(parseInt(inputAmount) <= 0){
            alert("0이하의 수량은 출고가 불가능합니다.");
            return;
        }

        if(parseInt(inputAmount) > parseInt(amount)){
            alert("출고 수량이 재고 수량을 넘어설 수 없습니다.");
            return;
        }

        if(this.requires < this.nowCount + parseInt(inputAmount)){
            alert("필요 수량을 초과하였습니다.");
            return;
        }

        this.selected.push(
            {
                id: id,
                productName: productName,
                amount: inputAmount,
                price: price,
                lot: lot,
                company: company
            }
        )
        this.nowCount += parseInt(inputAmount);
        
        this.lotDatas.forEach(item => {
            if(item.id == id){
                item.amount -= parseInt(inputAmount);
            }
        });

        this.lotTableManager.set_table_content(this.lotDatas);
        this.selectedTableManager.set_table_content(this.selected);
    }

    //좌측으로 로트 이동
    cancelLot(event){

        const targetTr = event.target.parentNode;
        const targetTbody = targetTr.parentNode;

        const eventIndex = Array.prototype.indexOf.call(targetTbody.children, targetTr);

        const id = this.selected[eventIndex].id;
        const amount = this.selected[eventIndex].amount;

        //우측의 데이터 삭제
        this.selected.splice(eventIndex, 1);
        this.selectedTableManager.set_table_content(this.selected)

        //좌측 데이터 수정
        this.lotDatas.forEach(item => {
            if(item.id == id){
                item.amount += parseInt(amount);
            }
        });

        this.nowCount -= parseInt(amount);
        this.lotTableManager.set_table_content(this.lotDatas);
    }

    clickComplete(){

        if(this.requires != this.nowCount){
            alert("수량이 맞지 않아 출고할 수 없습니다.");
            return;
        }

        this.callback(this.productId, this.selected);

    }

}