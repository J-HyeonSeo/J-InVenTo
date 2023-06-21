//필터링할 데이터, 속성, 필터링 값을 받아서 Filtering된 값을 주는 함수임.

/** dataset : 원본데이터, filterProperty : 필터링 할 속성, inputValue : 필터링값 */
export function doSearchFilter(dataset, filterProperty, inputValue){

    const filtered = []

    dataset.forEach(item => {
        
        if(item[filterProperty].includes(inputValue)){
            filtered.push(item);
        }

    });

    return filtered;
}