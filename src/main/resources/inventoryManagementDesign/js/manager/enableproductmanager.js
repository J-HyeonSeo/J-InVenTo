//모달 폼을 입력으로 받아서, 테이블 Content에 넣고 띄워줌.

var enableProductTableManager = null;
var enableProductTable = null;
var enableProductModal = null;

function initalizeProductModal(modalID){

    enableProductModal = openModal(modalID);
    enableProductTable = enableProductModal.firstElementChild.firstElementChild;
    // enableProductTable.style.height = '200px';

    enableProductTableManager = new TableManager(
        enableProductTable,
        ["id", "name", "company", "spec"],
        ["품목 ID", "품목명", "거래처명", "규격"],
        []);
    
    enableProductTableManager.table_initiallize("enableProductTableManager");

    var enableProducts = null;
    
    loadEnableProducts().then(response => {
        enableProducts = response;

        enableProductTableManager.set_table_content(enableProducts);

    }).catch(error => {
        alert(error.errorMessage);
        return;
    })

}
