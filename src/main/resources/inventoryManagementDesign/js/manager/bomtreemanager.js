class BomRequest{
    constructor(methodType, id, pid, cid, cost){
        this.methodType = methodType;
        this.id = id;
        this.pid = pid;
        this.cid = cid;
        this.cost = cost;
    }

    async request(){
        if(this.methodType == "post"){
            const body = {
                pid: this.pid,
                cid: this.cid,
                cost: this.cost
            }

            try{
                await requestExecute("/bom", "post", body);
            }catch(error){
                throw error;
            }
        }else if(this.methodType == "put"){
            const body = {
                id: this.id,
                cost: this.cost
            }

            try{
                await requestExecute("/bom", "put", body);
            }catch(error){
                throw error;
            }
        }else if(this.methodType == "node-delete"){
            try{
                await requestExecute("/bom/node/" + this.id, "delete");
            }catch(error){
                throw error;
            }
            
        }else if(this.methodType == "tree-delete"){
            try{
                await requestExecute("/bom/tree/" + this.pid, "delete");
            }catch(error){
                throw error;
            }
        }
    }
}


const bomTreeContainer = document.querySelector(".bom-container");

function showBomTree(bomTreeData, editMode){
    bomTreeContainer.innerHTML = "";

    const initailTable = document.createElement('table');
    const initailThead = document.createElement('thead');
    const initailTbody = document.createElement('tbody');

    initailTable.appendChild(initailThead);
    initailTable.appendChild(initailTbody);

    displayRecursive(bomTreeData, initailTable, editMode);

    bomTreeContainer.appendChild(initailTable);

}

function displayRecursive(bomTreeData, treeTable, editMode){
    //productName과 cost를 테이블에 찍어야함.

    //thead가져오기
    const nowThead = treeTable.firstElementChild;

    //tbody가져오기
    const nowTbody = treeTable.lastElementChild;

    //thead에 제목열 추가
    if(nowThead.childElementCount == 0){
        var colNames = null;
        if(editMode){
            //구현 보류
            colNames = ['', '품목명', '수량', '추가', '삭제'];
        }else{
            colNames = ['', '품목명', '수량'];
        }
        const nowTr = document.createElement("tr");
        colNames.forEach(item => {
            const nowTh = document.createElement("th");
            nowTh.textContent = item;
            nowTr.appendChild(nowTh);
        });
        nowThead.appendChild(nowTr);
    }

    const nowTr = document.createElement("tr");

    //tr에 현재 데이터들을 첨부함.
    if(editMode){
        nowTr.setAttribute('data-id', bomTreeData.id);
        nowTr.setAttribute('data-productId', bomTreeData.productId);
        nowTr.setAttribute('data-cost', bomTreeData.cost);
    }

    //자식을 가지고 있다면 맨 앞에 '+'가 필요함.
    const extensionTd = document.createElement('td');
    extensionTd.textContent = '-';
    extensionTd.className = 'bom-cursor-click';
    extensionTd.setAttribute('onclick', 'collapseTree(event)');

    //품목명을 넣어주고 있음.
    const productNameTd = document.createElement('td');
    productNameTd.textContent = bomTreeData.productName;

    //수량을 넣어주고 있음.
    const costTd = document.createElement('td');
    costTd.textContent = bomTreeData.cost;

    if(editMode){ //edit모드일 경우에는 수량을 클릭하면 인풋창과 버튼을 만들어드림.
        costTd.setAttribute('onclick', 'createUpdateCostInput(event)');
    }

    nowTr.appendChild(extensionTd);
    nowTr.appendChild(productNameTd);
    nowTr.appendChild(costTd);

    if(editMode){
        const addTd = document.createElement('td');
        addTd.textContent = '+';
        addTd.className = 'bom-cursor-click';
        //추가 메소드 셋팅.
        addTd.setAttribute('onclick', 'clickAddBomBtn(event)');

        const deleteTd = document.createElement('td');
        deleteTd.textContent = '-';
        deleteTd.className = 'bom-cursor-click';
        //삭제 메소드 셋팅. (단, 최상단 객체일 경우, tree를 지우는 걸로 셋팅해야함.)

        nowTr.appendChild(addTd);
        nowTr.appendChild(deleteTd);
    }
    nowTbody.appendChild(nowTr);

    //자식 데이터 순회.
    var nextTable = null;
    if(bomTreeData.children != null && bomTreeData.children.length > 0){
        nextTable = document.createElement('table');
        const nextThead = document.createElement('thead');
        const nextTbody = document.createElement('tbody');

        //tr을 하나 더 만들어서 테이블을 추가해주어야함.
        const nextTr = document.createElement('tr');
        const nextTd = document.createElement('td');

        if(editMode){
            nextTd.colSpan = 6;
        }else{
            nextTd.colSpan = 4;
        }
        nextTd.className = 'bom-cover-border';

        //DOM LINK
        nowTbody.appendChild(nextTr);

        nextTr.appendChild(nextTd);
        nextTd.appendChild(nextTable);

        nextTable.appendChild(nextThead);
        nextTable.appendChild(nextTbody);
    }

    bomTreeData.children.forEach(tree => {
        displayRecursive(tree, nextTable, editMode);
    })
}


//==================== UTILS ============================

//=================== TREE COLLAPSE AND EXPAND ========================
function collapseTree(event){
    const collapseTarget = event.target.parentNode.nextElementSibling;

    if(collapseTarget == null){
        return;
    }

    if(collapseTarget.firstElementChild.colSpan != 4 &&
        collapseTarget.firstElementChild.colSpan != 6){
        return;
    }

    collapseTarget.style.display = 'none';
    event.target.textContent = '+';
    event.target.setAttribute('onclick', 'expandTree(event)');
}

function expandTree(event){
    const collapseTarget = event.target.parentNode.nextElementSibling;

    if(collapseTarget == null){
        return;
    }
    collapseTarget.style.display = 'table-row';
    event.target.textContent = '-';
    event.target.setAttribute('onclick', 'collapseTree(event)');
}

//========================== 인풋 및 버튼 생성 ===========================================

function createUpdateCostInput(event){
    const originText = event.target.textContent;

    event.target.textContent = '';

    const costInput = document.createElement('input');
    costInput.type = 'text';
    costInput.value = originText;
    costInput.style.padding = '0';
    costInput.style.textAlign = 'center';
    costInput.style.width = '100px';

    const applyButton = document.createElement('button');
    applyButton.type = 'button';
    applyButton.textContent = '적용';
    applyButton.style.padding = '0 0.2rem';
    applyButton.setAttribute('onclick', 'clickUpdateBtn(event)');

    const cancelButton = document.createElement('button');
    cancelButton.type = 'button';
    cancelButton.textContent = '취소';
    cancelButton.style.padding = '0 0.2rem';
    cancelButton.setAttribute('onclick', 'clickUpdateCancelBtn(event)');

    event.target.appendChild(costInput);
    event.target.appendChild(applyButton);
    event.target.appendChild(cancelButton);

    event.target.setAttribute('onclick', '');
}
//==========================================================================


//==================== Button Click Event Methods ========================
function clickUpdateBtn(event){ //bom id에 cost를 넣어 보내는거임.
    const targetTr = event.target.parentNode.parentNode;
    const inputElement = event.target.parentNode.firstElementChild;
    const targetTd = event.target.parentNode;

    const id = targetTr.dataset.id;
    const cost = parseInt(inputElement.value);

    if(id == null || id == "null"){
        alert("최상단 품목의 수량은 변경할 수 없습니다.");
        return;
    }

    updateBom(id, cost).then(res => {
        targetTd.innerHTML = '';
        targetTd.textContent = cost;
        targetTd.setAttribute('onclick', 'createUpdateCostInput(event)');
    }).catch(error => {
        alert(error.errorMessage);
    })

}

function clickUpdateCancelBtn(event){
    const targetTr = event.target.parentNode.parentNode;
    const targetTd = event.target.parentNode;
    targetTd.innerHTML = '';

    targetTd.textContent = targetTr.dataset.cost;
    targetTd.setAttribute('onclick', 'createUpdateCostInput(event)');

}

function clickAddBomBtn(event){
    initalizeProductModal('product-modal');
}

//=========================== 실제 통신으로 넘어가는 메소드 ====================================
async function addBom(pid, cid, cost){
    
    try{
        await new BomRequest('post', null, pid, cid, cost).request();
    }catch(error){
        throw error;
    }
    
}

async function updateBom(id, cost){
    try{
        await new BomRequest('put', id, null, null, cost).request();
    }catch(error){
        throw error;
    }

}

async function deleteBomNode(id){
    try{
        await new BomRequest('node-delete', id, null, null, null).request();
    }catch(error){
        throw error;
    }
}

async function deleteBomTree(pid){
    try{
        await new BomRequest('tree-delete', null, pid, null, null).request();
    }catch(error){
        throw error;
    }
}