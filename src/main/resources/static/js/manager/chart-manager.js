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

    setDatas(data, dateString, valueString, optionDate='day', optionPrice = 'normal'){

        //초기화
        this.dates = [];
        this.shortDates = [];
        this.values = [];

        const group = {};
        let endSclice = 10;

        if(optionDate == 'day'){
            endSclice = 10;            
        }else if(optionDate == 'month'){
            endSclice = 7; 
        }else if(optionDate == 'year'){
            endSclice = 4;
        }

        data.forEach(item => {
            const nowDay = item[dateString].slice(0, endSclice);

            console.log(nowDay);

            if(group[nowDay]){
                group[nowDay] += item[valueString];
            }else{
                group[nowDay] = item[valueString];
            }
        });

        const sortedArray = Object.entries(group)
            .sort((a, b) => a[0].localeCompare(b[0])) // 키(key)를 오름차순으로 정렬
            .map(([date, value]) => ({ date, value }));

        let sumValue = 0;

        sortedArray.forEach(item => {
            this.dates.push(item.date);

            //makeShortDate
            const date = new Date(item.date);
            const year = date.getFullYear();
            const month = date.getMonth() + 1;
            const day = date.getDate();

            if(optionDate == 'day'){
                this.shortDates.push(`${month}/${day}`);
            }else if(optionDate == 'month'){
                this.shortDates.push(`${year}/${month}`);
            }else if(optionDate == 'year'){
                this.shortDates.push(`${year}`);
            }

            if(optionPrice == 'sum'){
                sumValue += item.value;
                this.values.push(sumValue);
            }else{
                this.values.push(item.value);
            }
            
        });

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

        //width, height 설정 및 레티나 디스플레이를 위한 스케일 설정.
        const devicePixelRatio = window.devicePixelRatio || 1;
        const parent = this.canvas.parentNode;

        this.width = parent.clientWidth;
        this.height = parent.clientHeight;

        this.canvas.width = this.width;
        this.canvas.height = this.height;
        this.ctx.scale(devicePixelRatio, devicePixelRatio);

        this.eventPoses = [];
        this.chartDatas = null;

        this.canvas.addEventListener('mousemove', this.hoverEffect.bind(this));

        this.chartDetail = document.getElementById('chart-detail');

        this.resizeTimeOut;

        window.addEventListener('resize', () => {

            clearTimeout(this.resizeTimeOut);

            this.resizeTimeOut = setTimeout(() => {
                const parent = this.canvas.parentNode;

                this.width = parent.clientWidth;
                this.height = parent.clientHeight;
        
                this.canvas.width = this.width;
                this.canvas.height = this.height;
                
                if(this.chartDatas != null)
                    this.initailize();
                this.dataView(null);
            }, 200);

        });
    }

    async initailize(){
        this.ctx.clearRect(0, 0, this.width, this.height);
        this.ctx.font = '15px GmarketSansMedium';

        // draw x axis
        this.drawLine(this.width * 0.1, this.height * 0.9, this.width * 0.9, this.height * 0.9, 250)
        .then(res => {
            this.drawLine(this.width * 0.9, this.height * 0.9, this.width * 0.9 - 5, this.height * 0.9 - 5, 10);
            this.drawLine(this.width * 0.9, this.height * 0.9, this.width * 0.9 - 5, this.height * 0.9 + 5, 10);
        });
        

        // draw y axis
        this.drawLine(this.width * 0.1, this.height * 0.9, this.width * 0.1, this.height * 0.1, 250)
        .then(res => {
            this.drawLine(this.width * 0.1, this.height * 0.1, this.width * 0.1 - 5, this.height * 0.1 + 5, 10);
            this.drawLine(this.width * 0.1, this.height * 0.1, this.width * 0.1 + 5, this.height * 0.1 + 5, 10);
        })

    }

    async dataView(chartDatas){

        if(chartDatas != null){
            this.chartDatas = chartDatas;
        }else if(this.chartDatas == null){
            return;
        }else{
            chartDatas = this.chartDatas;
        }
        this.eventPoses = [];


        //y축 다시 그리기
        this.drawYdivisor(chartDatas.divisors);


        const startWidth = this.width * 0.1 + 40; //draw 시작 width
        const endWidth = this.width * 0.9 - 40; //draw 끝 width

        const startHeight = this.height * 0.9 - 20; //draw 시작 height
        const endHeight = this.height * 0.1 + 20; //draw 종료 height

        //핑크색으로 배경 칠하기.
        this.ctx.fillStyle = 'rgba(255, 192, 203, 0.5)';
        this.ctx.fillRect(startWidth, endHeight, endWidth - startWidth, startHeight - endHeight);

        if(chartDatas.values.length == 1){
            let nowWidth = startWidth + (endWidth - startWidth) / 2;

            // this.drawDot(nowWidth, endHeight, 'red');
            await this.drawLine(nowWidth, endHeight, nowWidth, startHeight, 100);
            this.drawDot(nowWidth, endHeight, 'red');
            this.eventPoses.push(new Pos(nowWidth, endHeight));

            //현재 X축 눈금 그리기
            await this.drawLine(nowWidth, this.height * 0.9 - 5, nowWidth, this.height * 0.9 + 5, 10);
            this.ctx.fillStyle = 'white';
            this.ctx.font = '10px GmarketSansMedium';
            const text = chartDatas.shortDates[0];
            const textWidth = this.ctx.measureText(text).width;
            this.ctx.fillText(text, nowWidth - textWidth / 2, this.height * 0.95);
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
                this.ctx.font = '10px GmarketSansMedium';

                const text = chartDatas.shortDates[i];
                const textWidth = this.ctx.measureText(text).width;
                this.ctx.fillText(text, nowWidth - textWidth / 2, this.height * 0.95);

                // this.ctx.fillText(chartDatas.shortDates[i], nowWidth - 5, this.height * 0.95); //눈금 정중에 표시되게 해주세요.

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
            this.ctx.font = '15px GmarketSansMedium';
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

        // console.log('originX : ' + startX + ', originY : ' + startY);

        let prevX = startX;
        let prevY = startY;
      
        return new Promise(resolve => {
          const animate = (currentTime) => {
            const elapsedTime = Math.max(currentTime - startTime, 1);
            const progress = Math.min(elapsedTime / time, 1); // 진행 상태 (0 ~ 1)

            // 진행 상태에 따른 현재 좌표 계산
            const currentX = startX + (endX - startX) * progress;
            const currentY = startY + (endY - startY) * progress;
      
            // 선 그리기
            this.ctx.beginPath();
            this.ctx.moveTo(prevX, prevY);
            this.ctx.lineTo(currentX, currentY);

            // console.log('startX : ' + prevX + ', startY : ' + prevY + ', endX : ' + currentX + ', endY : ' + currentY);

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