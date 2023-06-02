const tableElement = document.getElementById("table");

function table_initiallize(cols){

    const tableHeader = tableElement.firstElementChild.firstElementChild;

    for(let i = 0; i < cols.length; i++){
        const headerTemp = document.createElement("th");
        headerTemp.textContent = cols[i];
        tableHeader.appendChild(headerTemp);
    }
    set_table_content(cols.length);
}

function set_table_content(colSize){

    const tableBody = tableElement.children[1];

    //일단 빈공간이라도 채워야함.
    for(let row = 0; row < 30; row++){
        const trTemp = document.createElement("tr");
        for(let col = 0; col < colSize; col++){
            const tdTemp = document.createElement("td");
            tdTemp.innerHTML = "&nbsp;";
            trTemp.appendChild(tdTemp);
        }
        tableBody.appendChild(trTemp);
    }
}

table_initiallize(["품목 ID" , "품목명", "거래처명", "단가", "규격"]);