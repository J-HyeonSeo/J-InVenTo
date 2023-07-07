import { OutboundDataLoader } from "../loader/outbound-loader.js";
import { ChartManager, ChartData } from "../manager/chart-manager.js";

class OutboundChart{

    constructor(){
        this.outboundDataLoader = new OutboundDataLoader();
        this.outboundChart = new ChartManager('outbound-chart');
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
            let outboundData = await this.outboundDataLoader.loadOutboundData(optionDate);
            
            this.outboundChart.initailize();
            const chartdata = new ChartData();
            chartdata.setDatas(outboundData, 'at', 'outboundPrice', optionDate, optionPrice);
            chartdata.setDivisors();
            this.outboundChart.dataView(chartdata);

        }catch(error){
            alert(error);
            alert("출고 데이터를 불러오는 도중에 오류가 발생했습니다.");
        }

    }

}


window.addEventListener('load', async function(){

    const outboundChart = new OutboundChart();
    outboundChart.initailize();

});