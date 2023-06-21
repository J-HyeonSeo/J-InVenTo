//서버에서 데이터를 가져옴.
import {requestExecute} from "../authenticate/request.js";

export class PurchaseDataLoader{

    constructor(){
        this.startCalendar = document.getElementById("startDate");
        this.endCalendar = document.getElementById("endDate");
    }

    async loadPurchaseData(){

        const startDate = this.startCalendar.value.replace(/-/g, '');
        const endDate = this.endCalendar.value.replace(/-/g, '');
    
        if(startDate == null || startDate.trim() == ""){
            throw("날짜가 지정되지 않았습니다.");
        }
    
        if(endDate == null || endDate.trim() == ""){
            throw("날짜가 지정되지 않았습니다.");
        }
        
        try{
            const response = await requestExecute("/purchase?startDate=" + startDate + "&endDate=" + endDate, "get", null);

            // //구매 금액 추가
            // response.forEach(item => {
            //     item.purchasePrice = item.price * item.amount;
            // });

            let modified = response;

            modified.forEach(item => {
                item.purchasePrice = item.price * item.amount;
            })

            return modified;

        }catch(error){
            throw error;
        }

    }

}
