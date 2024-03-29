//서버에서 데이터를 가져옴.
import {requestExecute} from "../authenticate/request.js";
import { DateManager } from "../manager/date-manager.js";

export class PurchaseDataLoader{

    constructor(startCalendarId, endCanlendarId){
        this.dateManager = new DateManager(startCalendarId, endCanlendarId);
    }

    async loadPurchaseData(option = 'day'){

        try{
            const date = this.dateManager.getDateRange(option);

            const response = await requestExecute("/purchase?startDate=" + date.start + "&endDate=" + date.end, "get", null);

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
