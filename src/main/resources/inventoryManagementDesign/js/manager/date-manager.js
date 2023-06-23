export class DateManager{

    constructor(startCalendarId, endCalendarId){
        this.startCalendar = document.getElementById(startCalendarId);
        this.endCalendar = document.getElementById(endCalendarId);
    }

    getDateRange(option = 'day'){

        let startDate = this.startCalendar.value.replace(/-/g, '');
        let endDate = this.endCalendar.value.replace(/-/g, '');
        
        if(startDate == null || startDate.trim() == ""){
            throw("날짜가 지정되지 않았습니다.");
        }
    
        if(endDate == null || endDate.trim() == ""){
            throw("날짜가 지정되지 않았습니다.");
        }

        if(option == 'month'){

            const startDateSplit = this.extractDateString(startDate);
            startDate = this.concatDateString(new Date(startDateSplit.year, startDateSplit.month, 1));

            const endDateSplit = this.extractDateString(endDate);
            endDate = this.concatDateString(new Date(endDateSplit.year, endDateSplit.month + 1, 0));

        }else if(option == 'year'){

            const startDateSplit = this.extractDateString(startDate);
            startDate = this.concatDateString(new Date(startDateSplit.year, 0, 1));

            const endDateSplit = this.extractDateString(endDate);
            endDate = this.concatDateString(new Date(endDateSplit.year, 11, 31));

        }

        return {
            start: startDate,
            end: endDate
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