const tbody = document.getElementById("product-content");
const colNames = ["id", "productId", "productName", "at", "amount", "price", "company", "note"];
const numCols = [4, 5];
table_initiallize(["구매번호", "품목번호", "품목명", "구매일시", "수량", "단가", "거래처명", "비고"]);



function filterByInput(){
    set_table_content(colNames.length, purchaseDatas, colNames, numCols);
}
