//서버에서 데이터를 가져옴.
import {requestExecute} from "../authenticate/request.js";
import { DateManager } from "../manager/date-manager.js";

export class LogDataLoader{

    constructor(){
        this.dateManager = new DateManager('startDate', 'endDate');
    }

    async loadLogData(option = 'day'){

        try{
            const date = this.dateManager.getDateRange(option);

            const response = await requestExecute("/admin/log?startDate=" + date.start + "&endDate=" + date.end, "get", null);

            return response;

        }catch(error){
            throw error;
        }

    }

}
