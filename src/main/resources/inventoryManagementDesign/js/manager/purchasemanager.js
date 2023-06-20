//구매 추가와 삭제를 담당하는 친구.

var addProductId = null;
var deletePurchaseId = null;

function openAddPurchaseModal(){
    openModal('purchase-add-modal');
}


function openDeletePurchaseModal(id){
    deletePurchaseId = id;
    openModal('purchase-delete-modal');
}

function closeDeletePurchaseModel(){
    document.getElementById('purchase-delete-modal').style.display = 'none';
}

function showProductSelector(){
    initalizeProductModal('product-modal', 'selectedProductAfter');
}

function selectedProductAfter(event){
    
    const trElement = event.target.parentNode;

    addProductId = parseInt(trElement.children[0].textContent);
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

function addPurchase(){

    const company = document.getElementById('company').value;
    const price = Parsing.parseDouble(document.getElementById('price').value);
    const amount = Parsing.parseDouble(document.getElementById('amount').value);
    const note = document.getElementById('note').value;

    body = {
        productId: addProductId,
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

function deletePurchase(){

    requestExecute('/purchase/' + deletePurchaseId, 'delete', null)
    .then(response => {
        alert('해당 구매를 성공적으로 삭제하였습니다.');
        window.location.assign('');
    }).catch(error => {
        alert(error.errorMessage);
        document.getElementById('purchase-delete-modal').style.display = 'none'
    })

}