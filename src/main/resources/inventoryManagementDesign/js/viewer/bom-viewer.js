import { TableManager, TableOnClickSet } from "../manager/table-manager.js";
import { loadBomTopDatas } from "../loader/bom-loader.js";
import { doSearchFilter } from "../viewer/mainfilter.js";
import { BomManager } from "../manager/bomtree-manager.js";

class BomViewer{

    constructor(){
        this.bomTable = document.getElementById("bomTable");
        this.searchElement = document.getElementById("search");
        this.bomTopDatas = null;
        this.bomManager = new BomManager();
    }

    viewerInitailize(){
        this.bomTableManager = new TableManager(
            bomTable,
            ["productId", "productName"],
            ["품목 번호" , "품목명"],
            [],
            new TableOnClickSet(this.bomManager.initailize.bind(this.bomManager), 'productId')
        );

        this.bomTableManager.table_initiallize();
        this.searchElement.addEventListener("input", this.filterByInput.bind(this));

        loadBomTopDatas()
        .then(response => {
            this.bomTopDatas = response;
            this.filterByInput();
        }).catch(error => {
            alert(error)
            alert("BOM 데이터를 가져오는 중에 오류가 발생했습니다.");
        })
    }

    filterByInput(){
    
        const searchVal = this.searchElement.value;
    
        const filteredDatas = doSearchFilter(this.bomTopDatas, "productName", searchVal);
    
        this.bomTableManager.set_table_content(filteredDatas);
    }

}

window.addEventListener('load', function(){
    const bomViewer = new BomViewer();
    bomViewer.viewerInitailize();

})

