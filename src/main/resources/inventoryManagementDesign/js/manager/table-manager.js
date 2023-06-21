export class TableOnClickSet{
    constructor(method, args){
        this.method = method;
        this.args = args;
    }
}

export class TableManager{
    constructor(table, colNames, displayColNames, numCols, tableOnClickSet = null){
        this.tableElement = table;
        this.colSize = colNames.length;
        this.colNames = colNames;
        this.displayColNames = displayColNames;
        this.numCols = numCols;
        this.orderList = [];
        this.EMPTY_COLS = 30;
        this.tableContents = null;
        this.tableOnClickSet = tableOnClickSet;
    }

    table_initiallize(){

        const tableHeader = this.tableElement.firstElementChild.firstElementChild;
        this.orderList = [];
    
        for(let i = 0; i < this.displayColNames.length; i++){
            const headerTemp = document.createElement("th");
            headerTemp.textContent = this.displayColNames[i];
            headerTemp.setAttribute('data-col', i);
            headerTemp.addEventListener('click', this.addOrder.bind(this));
            tableHeader.appendChild(headerTemp);
        }
        this.set_table_content(null);
    }

    resetOrder(){
        const theads = this.tableElement.firstElementChild.firstElementChild;
    
        for(let i = 0; i < theads.children.length; i++){
            theads.children[i].textContent = this.displayColNames[i];
        }
    
        this.orderList = [];
        this.set_table_content(null);
    }

    addOrder(event){

        console.log(this.orderList);

        const nowName = event.target.textContent;
        const nowCol = event.target.dataset.col;
    
        //우선적으로 orderList에 존재하면 지울거임.
        for(let i = 0; i < this.orderList.length; i++){
            if(this.orderList[i][0] == nowCol){
                this.orderList.splice(i, 1);
                break;
            }
        }
    
        //끝에 ▲, ▼가 없다면, 오름차순 준비.
    
        if(nowName == this.displayColNames[nowCol]){
            event.target.textContent = this.displayColNames[nowCol] + "▲";
            this.orderList.push([nowCol, 1]);
        }else if(nowName == this.displayColNames[nowCol] + "▲"){
            event.target.textContent = this.displayColNames[nowCol] + "▼";
            this.orderList.push([nowCol, -1]);
        }else{
            event.target.textContent = this.displayColNames[nowCol];
        }
        this.set_table_content(null);
    }

    dataOrdering(orderingDatas){
        if(orderingDatas == null){
            return;
        }
    
        if(this.orderList.length == 0){
            return;
        }
    
        this.orderList.forEach(item =>{
    
            //item[0] 정렬하려는 열, item[1] 은 정렬유형 1이면 오름차, -1이면 내림차
    
            //자료형 파악
            if(typeof orderingDatas[0][this.colNames[item[0]]] == "number" || 
            typeof orderingDatas[0][this.colNames[item[0]]] == "boolean"){
                if(item[1] == 1){
                    orderingDatas.sort((a, b) => a[this.colNames[item[0]]] - b[this.colNames[item[0]]]);
                }else{
                    orderingDatas.sort((a, b) => b[this.colNames[item[0]]] - a[this.colNames[item[0]]]);
                }
            }else{
                if(item[1] == -1){
                    orderingDatas.sort((a, b) => a[this.colNames[item[0]]].localeCompare(b[this.colNames[item[0]]]));
                }else{
                    orderingDatas.sort((a, b) => b[this.colNames[item[0]]].localeCompare(a[this.colNames[item[0]]]));
                }
            }
    
        });
    }

    set_table_content(setDatas){

        const tableBody = this.tableElement.children[1];
        tableBody.innerHTML = "";

        if(setDatas == null){
            setDatas = []

            if(this.tableContents != null){
                this.tableContents.forEach(item => {
                    setDatas.push(item);
                });
            }

        }else{
            this.tableContents = setDatas;
        }
    
        if(setDatas != null){
    
            this.dataOrdering(setDatas);
    
            for(let row = 0; row < setDatas.length; row++){

                //tr태그에 onclick를 사용해야함.
                const trTemp = document.createElement("tr");

                if(this.tableOnClickSet != null){ //할당이 되었다.

                    if(this.tableOnClickSet.args == null){
                        trTemp.addEventListener('click', this.tableOnClickSet.method);
                    }else{
                        trTemp.addEventListener('click', () => {
                            this.tableOnClickSet.method(setDatas[row][this.tableOnClickSet.args]);
                        })
                    }

                    // trTemp.setAttribute('onclick', 
                    //     this.tableOnClickSet.methodString + 
                    //     '(' + 
                    //     (this.tableOnClickSet.argsString != null ? setDatas[row][this.tableOnClickSet.argsString] : 'event') + 
                    //     ')'
                    // );
                }

                for(let col = 0; col < this.colSize; col++){
                    const tdTemp = document.createElement("td");
                    
                    var nowData = setDatas[row][this.colNames[col]];
    
                    if(this.numCols.includes(col)){
                        nowData = nowData.toLocaleString();
                    }
                    tdTemp.textContent = nowData;
                    trTemp.appendChild(tdTemp);
                }
                tableBody.appendChild(trTemp);
            }
        }
    
        //데이터가 30개 미만이면, 나머지는 공백으로 채움.
        if(setDatas == null || this.EMPTY_COLS - setDatas.length > 0){
            var diff = setDatas == null ? 0 : setDatas.length;
            for(let row = 0; row < this.EMPTY_COLS - diff; row++){
                const trTemp = document.createElement("tr");
                for(let col = 0; col < this.colSize; col++){
                    const tdTemp = document.createElement("td");
                    tdTemp.innerHTML = "&nbsp;";
                    trTemp.appendChild(tdTemp);
                }
                tableBody.appendChild(trTemp);
            }
        }
    }
}