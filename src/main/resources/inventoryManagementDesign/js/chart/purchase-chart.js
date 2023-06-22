import { PurchaseDataLoader } from "../loader/purchase-loader.js";
import { ChartManager, ChartData } from "../manager/chart-manager.js";

window.addEventListener('load', function(){

    const purchaseDataLoader = new PurchaseDataLoader();

    this.document.getElementById('daily-chart').addEventListener('click', () => {
        purchaseDataLoader.loadPurchaseData('year');
    })

    const purchaseChart = new ChartManager('purchase-chart');
    purchaseChart.initailize();

    const chartdata = new ChartData();

    chartdata.setDates(['2023-01-01', '2023-03-07', '2023-06-05', '2023-06-06', '2023-07-07', '2023-07-08']);
    chartdata.setValues([1000, 7000, 50000, 80000, 12000, 30000, 7000, 6000, 2000, 1000, 5000, 40000, 50000, 40000, 35000]);
    // chartdata.setValues([1000, 7000, 50000, 1000]);
    chartdata.setDivisors();

    purchaseChart.dataView(chartdata);

    // purchaseDataLoader.loadPurchaseData('month');

});