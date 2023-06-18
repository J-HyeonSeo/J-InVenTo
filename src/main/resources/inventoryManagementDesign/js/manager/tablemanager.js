const tableElement = document.getElementById("table");
const EMPTY_COLS = 30;
var orderList = []; //해당 배열에 값이 쌓이면 들어온 데이터에 정렬을 수행함.

function table_initiallize(cols){

    const tableHeader = tableElement.firstElementChild.firstElementChild;
    orderList = [];

    for(let i = 0; i < cols.length; i++){
        const headerTemp = document.createElement("th");
        headerTemp.textContent = cols[i];
        headerTemp.setAttribute('data-col', i);
        headerTemp.setAttribute('onclick', 'addOrder(event)');
        tableHeader.appendChild(headerTemp);
    }
    set_table_content(cols.length, null);
}

function resetOrder(){
    const theads = tableElement.firstElementChild.firstElementChild;

    for(let i = 0; i < theads.children.length; i++){
        theads.children[i].textContent = displayColNames[i];
    }

    orderList = [];
    filterByInput();
}

function addOrder(event){

    const nowName = event.target.textContent;
    const nowCol = event.target.dataset.col;

    // alert(event.target.dataset.col);
    // alert(event.target.textContent);

    //우선적으로 orderList에 존재하면 지울거임.
    for(let i = 0; i < orderList.length; i++){
        if(orderList[i][0] == nowCol){
            orderList.splice(i, 1);
            break;
        }
    }

    //끝에 ▲, ▼가 없다면, 오름차순 준비.

    if(nowName == displayColNames[nowCol]){
        event.target.textContent = displayColNames[nowCol] + "▲";
        orderList.push([nowCol, 1]);
    }else if(nowName == displayColNames[nowCol] + "▲"){
        event.target.textContent = displayColNames[nowCol] + "▼";
        orderList.push([nowCol, -1]);
    }else{
        event.target.textContent = displayColNames[nowCol];
    }
    filterByInput();
}

function dataOrdering(orderingDatas){
    if(orderingDatas == null){
        return;
    }

    if(orderList.length == 0){
        return;
    }

    orderList.forEach(item =>{

        //item[0] 정렬하려는 열, item[1] 은 정렬유형 1이면 오름차, -1이면 내림차

        //자료형 파악
        if(typeof orderingDatas[0][colNames[item[0]]] == "number" || 
        typeof orderingDatas[0][colNames[item[0]]] == "boolean"){
            if(item[1] == 1){
                orderingDatas.sort((a, b) => a[colNames[item[0]]] - b[colNames[item[0]]]);
            }else{
                orderingDatas.sort((a, b) => b[colNames[item[0]]] - a[colNames[item[0]]]);
            }
        }else{
            if(item[1] == -1){
                orderingDatas.sort((a, b) => a[colNames[item[0]]].localeCompare(b[colNames[item[0]]]));
            }else{
                orderingDatas.sort((a, b) => b[colNames[item[0]]].localeCompare(a[colNames[item[0]]]));
            }
        }

    });
}

function set_table_content(colSize, setDatas, colNames, numCols = []){

    const tableBody = tableElement.children[1];
    tableBody.innerHTML = "";

    if(setDatas != null){

        dataOrdering(setDatas);

        for(let row = 0; row < setDatas.length; row++){
            const trTemp = document.createElement("tr");
            for(let col = 0; col < colSize; col++){
                const tdTemp = document.createElement("td");
                
                nowData = setDatas[row][colNames[col]];

                if(numCols.includes(col)){
                    nowData = nowData.toLocaleString();
                }
                tdTemp.textContent = nowData;
                trTemp.appendChild(tdTemp);
            }
            tableBody.appendChild(trTemp);
        }
    }

    //데이터가 30개 미만이면, 나머지는 공백으로 채움.
    if(setDatas == null || EMPTY_COLS - setDatas.length > 0){
        diff = setDatas == null ? 0 : setDatas.length;
        for(let row = 0; row < EMPTY_COLS - diff; row++){
            const trTemp = document.createElement("tr");
            for(let col = 0; col < colSize; col++){
                const tdTemp = document.createElement("td");
                tdTemp.innerHTML = "&nbsp;";
                trTemp.appendChild(tdTemp);
            }
            tableBody.appendChild(trTemp);
        }
    }
}