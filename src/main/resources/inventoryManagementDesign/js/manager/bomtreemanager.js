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

    //현재 ProductName, Cost, tbody에 출력

    const nowTr = document.createElement("tr");

    if(editMode){
        //구현 보류
    }else{
        //자식을 가지고 있다면 맨 앞에 '+'가 필요함.
        const extensionTd = document.createElement('td');
        extensionTd.textContent = '-';
        extensionTd.className = 'bom-cursor-click';
        extensionTd.setAttribute('onclick', 'collapseTree(event)');

        const productNameTd = document.createElement('td');
        productNameTd.textContent = bomTreeData.productName;

        const costTd = document.createElement('td');
        costTd.textContent = bomTreeData.cost;

        nowTr.appendChild(extensionTd);
        nowTr.appendChild(productNameTd);
        nowTr.appendChild(costTd);
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

        nextTd.colSpan = 4;
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

function collapseTree(event){
    const collapseTarget = event.target.parentNode.nextElementSibling;

    if(collapseTarget == null){
        return;
    }

    if(collapseTarget.firstElementChild.colSpan != 4){
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