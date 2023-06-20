const is_edit = document.getElementById('page-type').value == "manage" ? true : false;
let latestProductId = null;

function loadBomTreeData(productId = null){

    if(productId == null){
        productId = latestProductId;
    }

    requestExecute("/bom/" + productId, "get", null)
    .then(response =>{
        latestProductId = productId;
        bomTreeData = response;
        showBomTree(bomTreeData, is_edit);
        openModal('bom-modal');
    }).catch(error => {
        latestProductId = null;
        alert(error);
    });

}