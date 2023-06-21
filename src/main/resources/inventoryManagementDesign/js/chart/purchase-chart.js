import { PurchaseDataLoader } from "../loader/purchase-loader.js";
import { ChartManager } from "../manager/chart-manager.js";

window.addEventListener('load', function(){

    const purchaseDataLoader = new PurchaseDataLoader();

    this.document.getElementById('daily-chart').addEventListener('click', () => {
        purchaseDataLoader.loadPurchaseData('year');
    })

    const purchaseChart = new ChartManager('purchase-chart');
    purchaseChart.initailize();

    // purchaseDataLoader.loadPurchaseData('month');

});