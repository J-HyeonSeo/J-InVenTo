//서버에서 데이터를 가져옴.
import {requestExecute} from "../authenticate/request.js";
import { DateManager } from "../manager/date-manager.js";

export class OutboundDataLoader{

    constructor(){
        this.dateManager = new DateManager('startDate', 'endDate');
    }

    async loadOutboundData(option = 'day'){

        try{
            const date = this.dateManager.getDateRange(option);

            const response = await requestExecute("/outbound?startDate=" + date.start + "&endDate=" + date.end, "get", null);
            
            response.forEach(item => {
                item.outboundPrice = item.price * item.amount;
            });

            return response;

        }catch(error){
            throw error;
        }

    }

}