export class ChartManager{

    constructor(canvasString){
        this.canvas = document.getElementById(canvasString);
        this.ctx = this.canvas.getContext('2d');

        const parent = this.canvas.parentNode;

        this.width = parent.clientWidth;
        this.height = parent.clientHeight;

        this.canvas.width = this.width;
        this.canvas.height = this.height;
    }

    initailize(){
        this.ctx.font = '15px GmarketSansMedium';

        //draw x axis
        this.ctx.beginPath();
        this.ctx.moveTo(this.width * 0.1, this.height * 0.9);
        this.ctx.lineTo(this.width * 0.9, this.height * 0.9);
        this.ctx.stroke();

        //draw y axis
        this.ctx.beginPath();
        this.ctx.moveTo(this.width * 0.1, this.height * 0.1);
        this.ctx.lineTo(this.width * 0.1, this.height * 0.9);
        this.ctx.stroke();

        //draw y axis div
        let startWidth = this.width * 0.1 - 5;
        let endWidth = this.width * 0.1 + 5;

        let rangeHeight = this.height * 0.9 - this.height * 0.1 - 40;
        let divHeight = rangeHeight / 10;
        let nowHeight = this.height * 0.9 - 20;
        for(let i = 0; i < 11; i++){
            this.ctx.beginPath();
            this.ctx.moveTo(startWidth, nowHeight);
            this.ctx.lineTo(endWidth, nowHeight);
            this.ctx.stroke();
            this.ctx.fillText(10000000 * i, startWidth - 100, nowHeight);
            nowHeight -= divHeight;
        }

        // this.ctx.font = '20px GmarketSansMedium';
        this.ctx.fillText('날짜', this.width * 0.075, this.height * 0.95); // 텍스트 쓰기
    }

    dataView(){

    }

}