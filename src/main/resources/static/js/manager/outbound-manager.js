import { OutboundDetailModalViewer } from "../modals/outbound-detail-modal-viewer.js";
import { EnableProductModalViewer } from "../modals/enableproduct-modal-viewer.js";
import { BomLeafModalViewer } from "../modals/bomleaf-modal-viewer.js";
import { requestExecute } from "../authenticate/request.js";

export class OutboundManager{

    constructor(){

        //requires modal viewer
        this.outboundDetailModalViewer = new OutboundDetailModalViewer();
        this.enableProductModalViewer = new EnableProductModalViewer();
        this.bomLeafModalViewer = new BomLeafModalViewer();

        this.deleteTargetId = null;
        this.outboundDeleteBtn = document.getElementById('all-delete-btn');
        this.openOutboundBtn = document.getElementById('open-add-outbound');
    }

    initialize(){
        this.openOutboundBtn.addEventListener('click', this.openProductModal.bind(this));
    }

    //조회 및 삭제를 위한 모달열기
    openDetailModal(id){
        this.deleteTargetId = id;
        this.outboundDetailModalViewer.initailize('outbound-detail-modal', this.clickDeleteOutboundDetail.bind(this), id);
        this.outboundDeleteBtn.addEventListener('click', this.clickDeleteOutbound.bind(this));
    }

    //출고 수행을 위한 모달열기 product(선택 후 증발) => bomleaf => outboundExecuteModal
    openProductModal(){
        this.enableProductModalViewer.initailize('product-modal', this.openBomLeafModal.bind(this));
    }

    openBomLeafModal(event){

        let amount;

        while(true){
            amount = prompt("출고 수량을 입력하세요 : ", '1');

            if(amount == null){
                return;
            }

            if(!isNaN(amount)){
                break;
            }else{
                alert("잘못된 값입니다. 다시 입력해주세요.");
            }
        }

        hideModal('product-modal');
        const productId = event.target.parentNode.children[0].textContent;
        this.bomLeafModalViewer.initailize('bom-leaf-modal', this.executeOutbound.bind(this), productId, amount);
    }

    clickDeleteOutbound(event){
        event.stopPropagation();
        if(confirm("해당 출고를 삭제하시겠습니까?")){
            this.deleteOutbound(this.deleteTargetId);
        }
    }

    clickDeleteOutboundDetail(event){
        event.stopPropagation();
        if(confirm("해당 출고 상세를 삭제하시겠습니까?")){
            const targetTr = event.target.parentNode;
            const id = targetTr.children[0].textContent;
            this.deleteOutboundDetail(id);
        }
    }


    //================== Communication Back-end Server ==========================

    executeOutbound(body){
        requestExecute('/outbound', 'post', body)
        .then(response => {
            alert('성공적으로 출고를 수행하였습니다.');
            window.location.assign('');
        }).catch(error => {
            alert(error);
        })
    }

    deleteOutbound(id){
        requestExecute('/outbound/' + id, 'delete', null)
        .then(response => {
            alert("해당 출고를 삭제하였습니다.");
            window.location.assign('');
        }).catch(error => {
            alert(error);
        })
    }

    deleteOutboundDetail(id){
        requestExecute('/outbound/detail/' + id, 'delete', null)
        .then(response => {
            alert("해당 출고 상세를 삭제하였습니다.");
            window.location.assign('');
        }).catch(error => {
            alert(error);
        })
    }

}