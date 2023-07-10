import { Parsing } from "../util/parsing.js";
import { requestExecute } from "../authenticate/request.js";
import { EnableProductModalViewer } from "../modals/enableproduct-modal-viewer.js";

export class PlanManager{
    constructor(){
        this.openAddPlanBtn = document.getElementById('open-add-plan-btn');
        this.addPlanBtn = document.getElementById('add-plan-btn');
        this.updatePlanBtn = document.getElementById('update-plan-btn');
        this.deletePlanBtn = document.getElementById('delete-plan-btn');

        this.modalTitle = document.getElementById('modal-title');

        this.productNameInput = document.getElementById("productName");
        this.amountInput = document.getElementById("amount");
        this.destinationInput = document.getElementById("destination");
        this.dueInput = document.getElementById("due");

        this.enableProductModalViewer = new EnableProductModalViewer();
        this.addProductId = null;
        this.selectedPlanId = null;
        this.mode = 'add';
    }

    initailize(){
        this.openAddPlanBtn.addEventListener('click', this.openAddPlanModal.bind(this));
        this.productNameInput.addEventListener('click', this.openSelectProductModal.bind(this));

        this.addPlanBtn.addEventListener('click', this.addPlan.bind(this));
        this.updatePlanBtn.addEventListener('click', this.updatePlan.bind(this));
        this.deletePlanBtn.addEventListener('click', this.deletePlan.bind(this));
    }

    inputInitailize(){
        this.addPlanBtn.style.display = 'none';
        this.updatePlanBtn.style.display = 'none';
        this.deletePlanBtn.style.display = 'none';

        this.productNameInput.value = '';
        this.amountInput.value = '';
        this.destinationInput.value = '';
        this.dueInput.value = '';
    }

    openAddPlanModal(){
        
        //content setting
        this.modalTitle.textContent = '새 계획';
        this.mode = 'add';

        this.inputInitailize();
        this.addPlanBtn.style.display = 'inline';

        openModal('plan-modal');
    }

    openSelectProductModal(){
        if(this.mode == 'update')return;
        this.enableProductModalViewer.initailize('product-modal', this.selectedProductAfter.bind(this));
    }

    selectedProductAfter(event){
        hideModal('product-modal');

        const targetTr = event.target.parentNode;
        this.addProductId = parseInt(targetTr.children[0].textContent);
        this.productNameInput.value = targetTr.children[1].textContent;
    }

    openUpdatePlanModal(event){
        this.mode = 'update';
        this.modalTitle.textContent = '계획 수정';
        this.inputInitailize();

        const targetTr = event.target.parentNode;

        const id = parseInt(targetTr.children[0].textContent);
        const productName = targetTr.children[1].textContent;
        const amount = Parsing.parseDouble(targetTr.children[2].textContent);
        const destination = targetTr.children[3].textContent;
        const due = targetTr.children[4].textContent;

        this.selectedPlanId = id;
        this.productNameInput.value = productName;
        this.amountInput.value = amount;
        this.destinationInput.value = destination;
        this.dueInput.value = due;

        this.updatePlanBtn.style.display = 'inline';
        this.deletePlanBtn.style.display = 'inline';

        openModal('plan-modal');
    }

    //====================== Communication Back-end Server =========================
    addPlan(){

        const amount = Parsing.parseDouble(this.amountInput.value);
        const destination = this.destinationInput.value;
        const due = this.dueInput.value;

        const body = {
            productId: this.addProductId,
            amount: amount,
            destination: destination,
            due: due
        }

        requestExecute('/plan', 'post', body)
        .then(response => {
            alert('계획이 성공적으로 추가되었습니다.');
            window.location.assign('');
        }).catch(error => {
            alert(error.errorMessage);
        });

    }

    updatePlan(){

        const amount = Parsing.parseDouble(this.amountInput.value);
        const destination = this.destinationInput.value;
        const due = this.dueInput.value;

        const body = {
            id: this.selectedPlanId,
            amount: amount,
            destination: destination,
            due: due
        }

        requestExecute('/plan', 'put', body)
        .then(response => {
            alert('계획이 성공적으로 수정되었습니다.');
            window.location.assign('');
        }).catch(error => {
            alert(error.errorMessage);
        });
    }

    deletePlan(){
        requestExecute('/plan/' + this.selectedPlanId , 'delete', null)
        .then(response => {
            alert('계획이 성공적으로 삭제되었습니다.');
            window.location.assign('');
        }).catch(error => {
            alert(error.errorMessage);
        });
    }


}