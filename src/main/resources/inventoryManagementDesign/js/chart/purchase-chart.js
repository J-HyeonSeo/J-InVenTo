import { PurchaseDataLoader } from "../loader/purchase-loader.js";
import { ChartManager, ChartData } from "../manager/chart-manager.js";

class PurchaseChart{

    constructor(){
        this.purchaseDataLoader = new PurchaseDataLoader('startDate', 'endDate');
        this.purchaseChart = new ChartManager('purchase-chart');
        this.optionPrice = document.getElementById('option-price');
    }

    initailize(){
        document.getElementById('daily-chart').addEventListener('click', () => {
            const optionPrice = this.optionPrice.value;
            this.loadDataAndViewChart('day', optionPrice);
        });

        document.getElementById('monthly-chart').addEventListener('click', () => {
            const optionPrice = this.optionPrice.value;
            this.loadDataAndViewChart('month', optionPrice);
        });

        document.getElementById('yearly-chart').addEventListener('click', () => {
            const optionPrice = this.optionPrice.value;
            this.loadDataAndViewChart('year', optionPrice);
        });
    }

    async loadDataAndViewChart(optionDate, optionPrice){

        try{
            let purchaseData = await this.purchaseDataLoader.loadPurchaseData(optionDate, optionPrice);
            
            this.purchaseChart.initailize();
            const chartdata = new ChartData();
            chartdata.setDatas(purchaseData, 'at', 'purchasePrice', optionDate, optionPrice);
            chartdata.setDivisors();
            this.purchaseChart.dataView(chartdata);

        }catch(error){
            alert(error);
            alert("구매 데이터를 불러오는 도중에 오류가 발생했습니다.");
        }

    }

}


window.addEventListener('load', async function(){

    const purchaseChart = new PurchaseChart();
    purchaseChart.initailize();

});