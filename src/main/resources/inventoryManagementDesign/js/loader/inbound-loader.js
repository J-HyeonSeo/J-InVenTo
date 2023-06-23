//서버에서 데이터를 가져옴.
import {requestExecute} from "../authenticate/request.js";
import { DateManager } from "../manager/date-manager.js";

export class InboundDataLoader{

    constructor(){
        this.dateManager = new DateManager();
    }

    async loadInboundData(option = 'day'){

        try{
            const date = this.dateManager.getDateRange(option);

            const response = await requestExecute("/inbound?startDate=" + date.start + "&endDate=" + date.end, "get", null);

            return response;

        }catch(error){
            throw error;
        }

    }

}