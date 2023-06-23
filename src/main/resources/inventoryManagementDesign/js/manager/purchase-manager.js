//구매 추가와 삭제를 담당하는 친구.
import { EnableProductModalViewer } from "../modals/enableproduct-modal-viewer.js";
import { requestExecute } from "../authenticate/request.js";

export class PurchaseManager{

    constructor(){
        this.addProductId = null;
        this.deletePurchaseId = null;
        this.enableProductModalViewer = new EnableProductModalViewer();

        this.openAddPurchase = document.getElementById('open-add-purchase');
        this.addPurchaseBtn = document.getElementById('add-purchase-btn');
        this.productNameInput = document.getElementById('productName');

        this.deletePurchaseBtn = document.getElementById('delete-purchase-btn');
        this.cancelDeletePurchaseBtn = document.getElementById('cancel-delete-purchase-btn');
    }

    initailize(){
        this.openAddPurchase.addEventListener('click', this.openAddPurchaseModal);
        this.addPurchaseBtn.addEventListener('click', this.addPurchase.bind(this));
        this.productNameInput.addEventListener('click', this.showProductSelector.bind(this));

        this.deletePurchaseBtn.addEventListener('click', this.deletePurchase.bind(this));
        this.cancelDeletePurchaseBtn.addEventListener('click', this.closeDeletePurchaseModal);
    }

    openAddPurchaseModal(){
        openModal('purchase-add-modal');
    }
    
    openDeletePurchaseModal(id){
        this.deletePurchaseId = id;
        openModal('purchase-delete-modal');
    }
    
    closeDeletePurchaseModal(){
        document.getElementById('purchase-delete-modal').style.display = 'none';
    }
    
    showProductSelector(){
        this.enableProductModalViewer.initailize('product-modal', this.selectedProductAfter.bind(this));
    }
    
    selectedProductAfter(event){
        
        const trElement = event.target.parentNode;
    
        this.addProductId = parseInt(trElement.children[0].textContent);
        const productName = trElement.children[1].textContent;
        const company = trElement.children[2].textContent;
        const price = trElement.children[4].textContent;
    
        //input요소에 할당하도록함.
        document.getElementById('productName').value = productName;
        document.getElementById('company').value = company;
        document.getElementById('price').value = price;
    
        document.getElementById('product-modal').style.display = 'none';
    
    }
    
    
    //==================== Communication Backend-Server =======================
    
    addPurchase(){
    
        const company = document.getElementById('company').value;
        const price = Parsing.parseDouble(document.getElementById('price').value);
        const amount = Parsing.parseDouble(document.getElementById('amount').value);
        const note = document.getElementById('note').value;
    
        const body = {
            productId: this.addProductId,
            company: company,
            price: price,
            amount: amount,
            note: note
        }
    
        requestExecute('/purchase', 'post', body)
        .then(response => {
            alert('구매가 성공적으로 추가되었습니다.');
            window.location.assign('');
        }).catch(error => {
            alert(error);
        });
    
    }
    
    deletePurchase(){
        requestExecute('/purchase/' + this.deletePurchaseId, 'delete', null)
        .then(response => {
            alert('해당 구매를 성공적으로 삭제하였습니다.');
            window.location.assign('');
        }).catch(error => {
            alert(error.errorMessage);
            document.getElementById('purchase-delete-modal').style.display = 'none'
        })
    
    }

}