class Pos{
    constructor(x, y){
        this.x = x;
        this.y = y;
    }
}

export class ChartData{

    constructor(){
        this.divisors = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
        this.dates = [];
        this.shortDates = [];
        this.values = [];

        this.maxValue = 100000000;
    }

    setDates(dates, shortDates){
        this.dates = dates;
        this.shortDates = shortDates;
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

        this.eventPoses = [];
        this.chartDatas = null;

        this.canvas.addEventListener('mousemove', this.hoverEffect.bind(this));

        this.chartDetail = document.getElementById('chart-detail');
    }

    async initailize(){
        this.ctx.font = '15px GmarketSansMedium';

        //draw x axis
        this.drawLine(this.width * 0.1, this.height * 0.9, this.width * 0.9, this.height * 0.9, 500)
        .then(res => {
            this.drawLine(this.width * 0.9, this.height * 0.9, this.width * 0.9 - 5, this.height * 0.9 - 5, 10);
            this.drawLine(this.width * 0.9, this.height * 0.9, this.width * 0.9 - 5, this.height * 0.9 + 5, 10);
        });
        

        //draw y axis
        this.drawLine(this.width * 0.1, this.height * 0.9, this.width * 0.1, this.height * 0.1, 500)
        .then(res => {
            this.drawLine(this.width * 0.1, this.height * 0.1, this.width * 0.1 - 5, this.height * 0.1 + 5, 10);
            this.drawLine(this.width * 0.1, this.height * 0.1, this.width * 0.1 + 5, this.height * 0.1 + 5, 10);
        })

    }

    async dataView(chartDatas){

        //y축 다시 그리기
        this.drawYdivisor(chartDatas.divisors);

        this.chartDatas = chartDatas;
        this.eventPoses = [];

        const startWidth = this.width * 0.1 + 40; //draw 시작 width
        const endWidth = this.width * 0.9 - 40; //draw 끝 width

        const startHeight = this.height * 0.9 - 20; //draw 시작 height
        const endHeight = this.height * 0.1 + 20; //draw 종료 height

        //핑크색으로 배경 칠하기.
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

                const nowHeight = Math.max(...[startHeight - (startHeight - endHeight) * (nowRatio), endHeight]);

                if(i == 0){
                    this.ctx.moveTo(nowWidth, nowHeight);
                }else{
                    // this.ctx.lineTo(nowWidth, nowHeight);
                    await this.drawLine(prevWidth, prevHeight, nowWidth, nowHeight, 500/chartDatas.values.length);

                    //영역 칠하기
                    this.ctx.beginPath();
                    this.ctx.fillStyle = 'rgba(135, 206, 250, 0.2)';
                    this.ctx.moveTo(prevWidth, startHeight);
                    this.ctx.lineTo(prevWidth, prevHeight);
                    this.ctx.lineTo(nowWidth, nowHeight);
                    this.ctx.lineTo(nowWidth, startHeight);
                    this.ctx.closePath();
                    this.ctx.fill();

                    //이전 좌표 점 다시 그리기.
                    this.drawDot(prevWidth, prevHeight, 'red');
                }
                prevWidth = nowWidth;
                prevHeight = nowHeight;
                
                //현재 좌표 점 그리기.
                this.drawDot(nowWidth, nowHeight, 'red');

                //이벤트 리스너를 위한 좌표 채우기
                this.eventPoses.push(new Pos(nowWidth, nowHeight));

                //현재 X축 눈금 그리기
                await this.drawLine(nowWidth, this.height * 0.9 - 5, nowWidth, this.height * 0.9 + 5, 10);
                this.ctx.fillStyle = 'white';
                this.ctx.fillText(chartDatas.shortDates[i], nowWidth - 5, this.height * 0.95);

                nowWidth += div;
            }
            // this.ctx.stroke();

        }

    }

    //======================== Event Function ======================================

    hoverEffect(event){
        const rect = this.canvas.getBoundingClientRect();
        const nowX = event.clientX - rect.left;
        const nowY = event.clientY - rect.top;

        let is_keep_detail = false;

        for(let i = 0; i < this.eventPoses.length; i++){

            const x = this.eventPoses[i].x;
            const y = this.eventPoses[i].y;

            if(x > nowX - 5 && x < nowX + 5 && y > nowY - 5 && y < nowY + 5){
                this.drawDot(x, y, 'white');

                //상세정보 위치 설정
                this.chartDetail.style.left = event.clientX + 'px';
                this.chartDetail.style.top = event.clientY + 'px';

                //상세정보 내용 변경
                const price = this.chartDetail.children[1];
                const date = this.chartDetail.children[4];

                price.textContent = this.chartDatas.values[i].toLocaleString();
                date.textContent = this.chartDatas.dates[i];

                this.chartDetail.style.display = 'block';
                is_keep_detail = true;
            }else{
                this.drawDot(x, y, 'red');
            }

        }

        if(!is_keep_detail){
            this.chartDetail.style.display = 'none';
        }
    }

    //======================== Draw Util ===========================================

    async drawYdivisor(divisors){

        //먼저 지우고 시작함.
        this.ctx.clearRect(0, 0, this.width * 0.1, this.height);

        let startWidth = this.width * 0.1 - 5;
        let endWidth = this.width * 0.1 + 5;

        let rangeHeight = this.height * 0.9 - this.height * 0.1 - 40;
        let divHeight = rangeHeight / 10;
        let nowHeight = this.height * 0.9 - 20;
        for(let i = 0; i <= 10; i++){
            await this.drawLine(startWidth, nowHeight, endWidth, nowHeight, 15);
            console.log('f');
            this.ctx.fillStyle = 'white';
            this.ctx.fillText(divisors[i].toLocaleString(), startWidth - 100, nowHeight);
            nowHeight -= divHeight;
        }

        this.ctx.fillText('금액(원)', startWidth - 100, this.height * 0.05);
        this.ctx.fillText('날짜', this.width * 0.9 + 10, this.height * 0.95);
    }

    drawDot(x, y, colorString){
        this.ctx.fillStyle = colorString;
        this.ctx.beginPath();
        this.ctx.arc(x, y, 5, 0, 2 * Math.PI); // 원을 그리는 arc 메서드 사용
        this.ctx.fill(); // 채우기로 점 그리기
    }

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