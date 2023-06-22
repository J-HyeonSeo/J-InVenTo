export class ChartData{

    constructor(){
        this.divisors = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
        this.dates = [];
        this.values = [];

        this.maxValue = 100000000;
    }

    setDates(dates){
        this.dates = dates; 
    }

    setValues(values){
        this.values = values;
    }

    setDivisors(){
        this.maxValue = Math.max(...this.values);

        const maxValue = this.maxValue;
        const chartScale = Math.ceil(maxValue / 10); // 차트 눈금 계산
        
        const divisors = [];
        for (let i = 0; i <= 10; i++) {
            divisors.push(i * chartScale);
        }
        
        this.divisors = divisors;
    }

}

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

    async initailize(){
        this.ctx.font = '15px GmarketSansMedium';

        //draw x axis
        this.drawLine(this.width * 0.1, this.height * 0.9, this.width * 0.9, this.height * 0.9, 500);

        //draw y axis
        this.drawLine(this.width * 0.1, this.height * 0.9, this.width * 0.1, this.height * 0.1, 500);

        //draw y axis div
        
        let startWidth = this.width * 0.1 - 5;
        let endWidth = this.width * 0.1 + 5;

        let rangeHeight = this.height * 0.9 - this.height * 0.1 - 40;
        let divHeight = rangeHeight / 10;
        let nowHeight = this.height * 0.9 - 20;
        for(let i = 0; i < 11; i++){
            // this.ctx.beginPath();
            // this.ctx.moveTo(startWidth, nowHeight);
            // this.ctx.lineTo(endWidth, nowHeight);
            // this.ctx.stroke();

            await this.drawLine(startWidth, nowHeight, endWidth, nowHeight, 15);

            this.ctx.fillStyle = 'white';
            this.ctx.fillText(10000000 * i, startWidth - 100, nowHeight);
            nowHeight -= divHeight;
        }

        this.ctx.fillText('날짜', this.width * 0.075, this.height * 0.95);
    }

    async dataView(chartDatas){

        const startWidth = this.width * 0.1 + 40;
        const endWidth = this.width * 0.9 - 40;

        const startHeight = this.height * 0.9 - 20;
        const endHeight = this.height * 0.1 + 20;

        this.ctx.fillStyle = 'rgba(255, 192, 203, 0.5)';
        this.ctx.fillRect(startWidth, endHeight, endWidth - startWidth, startHeight - endHeight);

        if(chartDatas.values.length == 1){

        }else{

            const div = (endWidth - startWidth) / (chartDatas.values.length - 1);

            let nowWidth = startWidth;

            let prevWidth = 0;
            let prevHeight = 0;

            for(let i = 0; i < chartDatas.values.length; i++){
                const nowRatio = chartDatas.values[i] / chartDatas.maxValue;

                console.log(nowRatio);

                const nowHeight = Math.max(...[(startHeight - endHeight) * (1 - nowRatio), endHeight]);

                if(i == 0){
                    this.ctx.moveTo(nowWidth, nowHeight);
                }else{
                    // this.ctx.lineTo(nowWidth, nowHeight);
                    await this.drawLine(prevWidth, prevHeight, nowWidth, nowHeight, 500/chartDatas.values.length);

                    this.ctx.beginPath();
                    this.ctx.fillStyle = 'rgba(135, 206, 250, 0.2)';
                    this.ctx.moveTo(prevWidth, startHeight);
                    this.ctx.lineTo(prevWidth, prevHeight);
                    this.ctx.lineTo(nowWidth, nowHeight);
                    this.ctx.lineTo(nowWidth, startHeight);
                    this.ctx.closePath();
                    this.ctx.fill();

                    this.ctx.fillStyle = 'red';
                    this.ctx.beginPath();
                    this.ctx.arc(prevWidth, prevHeight, 5, 0, 2 * Math.PI); // 원을 그리는 arc 메서드 사용
                    this.ctx.fill(); // 채우기로 점 그리기
                }
                prevWidth = nowWidth;
                prevHeight = nowHeight;
                
                this.ctx.fillStyle = 'red';
                this.ctx.beginPath();
                this.ctx.arc(nowWidth, nowHeight, 5, 0, 2 * Math.PI); // 원을 그리는 arc 메서드 사용
                this.ctx.fill(); // 채우기로 점 그리기

                nowWidth += div;
            }
            // this.ctx.stroke();

        }

    }


    //======================== Draw Utils ===========================================

    async drawLine(startX, startY, endX, endY, time) {
        const startTime = performance.now();

        let prevX = startX;
        let prevY = startY;
      
        return new Promise(resolve => {
          const animate = (currentTime) => {
            const elapsedTime = currentTime - startTime;
            const progress = Math.min(elapsedTime / time, 1); // 진행 상태 (0 ~ 1)

            // 진행 상태에 따른 현재 좌표 계산
            const currentX = startX + (endX - startX) * progress;
            const currentY = startY + (endY - startY) * progress;
      
            // 선 그리기
            this.ctx.beginPath();
            this.ctx.moveTo(prevX, prevY);
            this.ctx.lineTo(currentX, currentY);

            this.ctx.strokeStyle = 'white';
            this.ctx.lineWidth = 2; // 라인의 굵기

            this.ctx.stroke();

            prevX = currentX;
            prevY = currentY;
      
            if (progress < 1) {
              // 애니메이션이 완료되지 않았으면 계속 호출
              requestAnimationFrame(animate);
            } else {
              resolve(); // 애니메이션이 완료되면 프로미스를 해결(resolve)
            }
          }
      
          // 애니메이션 시작
          requestAnimationFrame(animate);
        });
      }

}