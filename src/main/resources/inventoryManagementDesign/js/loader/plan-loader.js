//서버에서 데이터를 가져옴.
import {requestExecute} from "../authenticate/request.js";
import { DateManager } from "../manager/date-manager.js";

export class PlanDataLoader{

    constructor(startCalendarId, endCanlendarId){
        this.dateManager = new DateManager(startCalendarId, endCanlendarId);
    }

    async loadPlanData(option = 'day'){

        try{
            const date = this.dateManager.getDateRange(option);

            const response = await requestExecute("/plan?startDate=" + date.start + "&endDate=" + date.end, "get", null);

            return response;

        }catch(error){
            throw error;
        }

    }

}