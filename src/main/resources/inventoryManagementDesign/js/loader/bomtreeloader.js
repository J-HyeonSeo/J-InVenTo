//test
loadBomTreeData(10);

function loadBomTreeData(productId){
    requestExecute("/bom/" + productId, "get", null)
    .then(response =>{
        bomTreeData = response;
        showBomTree(bomTreeData, false);
        openModal('bom-modal');
    })/*.catch(error => {
        alert(error);
    });*/

}