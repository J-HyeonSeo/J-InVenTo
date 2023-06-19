const is_edit = document.getElementById('page-type').value == "manage" ? true : false;

function loadBomTreeData(productId){
    requestExecute("/bom/" + productId, "get", null)
    .then(response =>{
        bomTreeData = response;
        showBomTree(bomTreeData, is_edit);
        openModal('bom-modal');
    })/*.catch(error => {
        alert(error);
    });*/

}