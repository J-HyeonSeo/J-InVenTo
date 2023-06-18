const tableElement = document.getElementById("table");
const EMPTY_COLS = 30;

function table_initiallize(cols){

    const tableHeader = tableElement.firstElementChild.firstElementChild;

    for(let i = 0; i < cols.length; i++){
        const headerTemp = document.createElement("th");
        headerTemp.textContent = cols[i];
        tableHeader.appendChild(headerTemp);
    }
    set_table_content(cols.length, null);
}

function set_table_content(colSize, setDatas, colNames, numCols = []){

    const tableBody = tableElement.children[1];
    tableBody.innerHTML = "";

    if(setDatas != null){
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