import { Parsing } from "../util/parsing.js";
import { PurchaseModalViewer } from "../modals/purchase-modal-viewer.js";
import { requestExecute } from "../authenticate/request.js";

export class InboundManager{

    constructor(){
        this.openAddInboundBtn = document.getElementById('open-add-inbound-btn');
        this.purchaseIdInput = document.getElementById('purchaseId');
        this.productNameInput = document.getElementById('productName');
        this.canAmountLabel = document.getElementById('can-amount-label');

        this.amountInput = document.getElementById('amount');
        this.noteInput = document.getElementById('note');
        this.addInboundBtn = document.getElementById('add-inbound-btn');
        
        this.deleteInboundBtn = document.getElementById('delete-inbound-btn');
        this.cancelDeleteInboundBtn = document.getElementById('cancel-delete-inbound-btn');

        this.purchaseModalViewer = new PurchaseModalViewer();

        this.targetPurchaseId = null;
        this.deleteInboundId = null;
    }

    initailize(){
        this.openAddInboundBtn.addEventListener('click', this.openAddInboundModal.bind(this));
        this.purchaseIdInput.addEventListener('click', this.openSelectPurchaseModal.bind(this));
        this.addInboundBtn.addEventListener('click', this.addInbound.bind(this));

        this.deleteInboundBtn.addEventListener('click', this.deleteInbound.bind(this));
        this.cancelDeleteInboundBtn.addEventListener('click', this.closeDeleteInboundModal);

    }

    openAddInboundModal(){
        openModal('inbound-add-modal');
    }

    openSelectPurchaseModal(){
        this.purchaseModalViewer.initailize('purchase-modal', this.selectedPurchaseAfter.bind(this));
    }

    selectedPurchaseAfter(event){
        hideModal('purchase-modal');

        const targetTr = event.target.parentNode;
        
        this.targetPurchaseId = parseInt(targetTr.children[0].textContent);

        this.purchaseIdInput.value = targetTr.children[0].textContent;
        this.productNameInput.value = targetTr.children[1].textContent;

        this.canAmountLabel.textContent = '입고 수량(가능 수량 : ' + targetTr.children[3].textContent + '개)';
    }

    openDeleteInboundModal(id){
        this.deleteInboundId = id;
        openModal('inbound-delete-modal');
    }

    closeDeleteInboundModal(){
        hideModal('inbound-delete-modal');
    }

    //============================== Communication Back-end Server =================================
    addInbound(){

        const amount = Parsing.parseDouble(this.amountInput.value);
        const note = this.noteInput.value;

        const body = {
            purchaseId: this.targetPurchaseId,
            amount: amount,
            note: note
        }


        requestExecute('/inbound', 'post', body)
        .then(response => {
            alert("성공적으로 입고가 수행되었습니다.");
            window.location.assign('');
        }).catch(error => {
            alert(error.errorMessage);
        })

    }

    deleteInbound(){
        requestExecute('/inbound/' + this.deleteInboundId , 'delete', null)
        .then(response => {
            alert("성공적으로 입고가 취소되었습니다.");
            window.location.assign('');
        }).catch(error => {
            alert(error.errorMessage);
        })
    }

}