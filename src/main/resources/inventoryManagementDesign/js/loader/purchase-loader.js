//서버에서 데이터를 가져옴.
import {requestExecute} from "../authenticate/request.js";

export class PurchaseDataLoader{

    constructor(){
        this.startCalendar = document.getElementById("startDate");
        this.endCalendar = document.getElementById("endDate");
    }

    async loadPurchaseData(option = 'day'){

        let startDate = this.startCalendar.value.replace(/-/g, '');
        let endDate = this.endCalendar.value.replace(/-/g, '');
        
        if(option == 'day'){
            if(startDate == null || startDate.trim() == ""){
                throw("날짜가 지정되지 않았습니다.");
            }
        
            if(endDate == null || endDate.trim() == ""){
                throw("날짜가 지정되지 않았습니다.");
            }
        }else if(option == 'month'){

            const startDateSplit = this.extractDateString(startDate);
            startDate = this.concatDateString(new Date(startDateSplit.year, startDateSplit.month, 1));

            const endDateSplit = this.extractDateString(endDate);
            endDate = this.concatDateString(new Date(endDateSplit.year, endDateSplit.month + 1, 0));

        }else if(option == 'year'){

            const startDateSplit = this.extractDateString(startDate);
            startDate = this.concatDateString(new Date(startDateSplit.year, 0, 1));

            const endDateSplit = this.extractDateString(endDate);
            endDate = this.concatDateString(new Date(endDateSplit.year, 11, 31));

        }else{
            throw("허용되지 않은 옵션입니다.");
        }
        
        try{
            const response = await requestExecute("/purchase?startDate=" + startDate + "&endDate=" + endDate, "get", null);

            let modified = response;

            modified.forEach(item => {
                item.purchasePrice = item.price * item.amount;
            })

            return modified;

        }catch(error){
            throw error;
        }

    }

    extractDateString(dateString){
        const year = Number(dateString.slice(0, 4));
        const month = Number(dateString.slice(4, 6)) - 1;
        const day = Number(dateString.slice(6, 8));
        return {
            year: year,
            month: month,
            day: day
        }
    }

    concatDateString(dateObject){
        const year = dateObject.getFullYear();
        const month = String(dateObject.getMonth() + 1).padStart(2, "0");
        const day = String(dateObject.getDate()).padStart(2, "0");
      
        return `${year}${month}${day}`;
    }

}
