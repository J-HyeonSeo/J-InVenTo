import { requestExecute } from "../authenticate/request.js";

//product의 추가, 수정, 삭제를 관리한다.

export class ProductManager{

    constructor(){
        this.addProductBtn = document.getElementById('add-product-btn');
        this.updateProductBtn = document.getElementById('update-product-btn');
        this.enableProductBtn = document.getElementById('enable-product-btn');
        this.disableProductBtn = document.getElementById('disable-product-btn');
        this.deleteProductBtn = document.getElementById('delete-product-btn');
        this.targetProductId = null;
    }

    managerInitailize(){ //이벤트 리스너 등록한다.
        document.getElementById('new-product-btn')
            .addEventListener('click', this.clickAddProductBtn.bind(this));
        this.addProductBtn.addEventListener('click', this.addProduct);

        this.updateProductBtn.addEventListener('click', this.updateProduct.bind(this));
        this.enableProductBtn.addEventListener('click', this.enableProduct.bind(this));
        this.disableProductBtn.addEventListener('click', this.disableProduct.bind(this));
        this.deleteProductBtn.addEventListener('click',this.deleteProduct.bind(this));
    }

    allDisableBtn(){
        this.addProductBtn.style.display = 'none';
        this.updateProductBtn.style.display = 'none';
        this.enableProductBtn.style.display = 'none';
        this.disableProductBtn.style.display = 'none';
        this.deleteProductBtn.style.display = 'none';
    }

    //User Contact Button Function
    clickAddProductBtn(){
        document.getElementById('product-details').style.display = 'block';
    
        //제목 바꾸기
        document.getElementById('modal-title').textContent = "새 품목";
    
        //내용 비우기
        document.getElementById('name').value = '';
        document.getElementById('company').value = '';
        document.getElementById('price').value = '';
        document.getElementById('spec').value = '';
    
        this.allDisableBtn();
        this.addProductBtn.style.display = 'inline';
    }
    clickUpdateProductBtn(event){
        document.getElementById('product-details').style.display = 'block';
    
        //제목 바꾸기
        document.getElementById('modal-title').textContent = "품목 수정";
    
        this.targetProductId = parseInt(event.target.parentNode.children[0].textContent);

        const name = event.target.parentNode.children[1].textContent;
        const company = event.target.parentNode.children[2].textContent;
        const price = Parsing.parseDouble(event.target.parentNode.children[3].textContent);
        const spec = event.target.parentNode.children[4].textContent;
        const enabled = event.target.parentNode.children[5].textContent;
    
        document.getElementById('name').value = name;
        document.getElementById('company').value = company
        document.getElementById('price').value = price;
        document.getElementById('spec').value = spec;
    
        this.allDisableBtn();
        this.updateProductBtn.style.display = 'inline';
    
        if(enabled == true || enabled == 'true'){
            this.disableProductBtn.style.display = 'inline';
        }else{
            this.enableProductBtn.style.display = 'inline';
        }
    
        this.deleteProductBtn.style.display = 'inline';
    }


    //=================== Communication to Backend-Server. =========================
    addProduct(){

        //extract data from form.
        const name = document.getElementById('name').value;
        const company = document.getElementById('company').value;
        const price = parseFloat(document.getElementById('price').value);
        const spec = document.getElementById('spec').value;

        const body = {
            name: name,
            company: company,
            price: price,
            spec: spec
        };

        requestExecute('/product', 'post', body)
        .then(response => {
            const resName = response.name;
            alert(resName + ' 가 성공적으로 추가되었습니다.');
            window.location.assign('');
        }).catch(error => {
            alert(error.errorMessage);
        });
    }

    updateProduct(){

        const id = this.targetProductId;

        //extract data from form.
        const name = document.getElementById('name').value;
        const company = document.getElementById('company').value;
        const price = parseFloat(document.getElementById('price').value);
        const spec = document.getElementById('spec').value;
    
        const body = {
            id: id,
            name: name,
            company: company,
            price: price,
            spec: spec
        };
    
        requestExecute('/product', 'put', body)
        .then(response => {
            const resName = response.name;
            alert(resName + ' 가 성공적으로 수정되었습니다.');
            window.location.assign('');
        }).catch(error => {
            alert(error.errorMessage);
        });

    }

    enableProduct(){

        const id = this.targetProductId;

        const body = {
            id: id,
            enabled: true
        };

        requestExecute('/product', 'put', body)
        .then(response => {
            const resName = response.name;
            alert("성공적으로 활성화 되었습니다.");
            window.location.assign('');
        }).catch(error => {
            alert(error.errorMessage);
        });
    }

    disableProduct(){

        const id = this.targetProductId;

        requestExecute('/product/' + id, 'PATCH', null)
        .then(response => {
            alert('성공적으로 비활성화 되었습니다.');
            window.location.assign('');
        }).catch(error => {
            alert(error.errorMessage);
        });

    }

    deleteProduct(){

        const id = this.targetProductId;

        requestExecute('/product/' + id, 'delete', null)
        .then(response => {
            const resName = response.name;
            alert('성공적으로 삭제 되었습니다.');
            window.location.assign('');
        }).catch(error => {
            alert(error.errorMessage);
        });
    }

}

// const addProductBtn = document.getElementById('add-product-btn');
// const updateProductBtn = document.getElementById('update-product-btn');
// const enableProductBtn = document.getElementById('enable-product-btn');
// const disableProductBtn = document.getElementById('disable-product-btn');
// const deleteProductBtn = document.getElementById('delete-product-btn');

// function allDisableBtn(){
//     addProductBtn.style.display = 'none';
//     updateProductBtn.style.display = 'none';
//     enableProductBtn.style.display = 'none';
//     disableProductBtn.style.display = 'none';
//     deleteProductBtn.style.display = 'none';
// }

//User Contact Button Function
// function clickAddProductBtn(){
//     openModal('product-details');

//     //제목 바꾸기
//     document.getElementById('modal-title').textContent = "새 품목";

//     //내용 비우기
//     document.getElementById('name').value = '';
//     document.getElementById('company').value = '';
//     document.getElementById('price').value = '';
//     document.getElementById('spec').value = '';

//     allDisableBtn();
//     addProductBtn.style.display = 'inline';
// }

// function clickUpdateProductBtn(event){
//     openModal('product-details');

//     //제목 바꾸기
//     document.getElementById('modal-title').textContent = "품목 수정";

//     const id = parseInt(event.target.parentNode.children[0].textContent);
//     const name = event.target.parentNode.children[1].textContent;
//     const company = event.target.parentNode.children[2].textContent;
//     const price = Parsing.parseDouble(event.target.parentNode.children[3].textContent);
//     const spec = event.target.parentNode.children[4].textContent;
//     const enabled = event.target.parentNode.children[5].textContent;

//     document.getElementById('name').value = name;
//     document.getElementById('company').value = company
//     document.getElementById('price').value = price;
//     document.getElementById('spec').value = spec;

//     allDisableBtn();
//     updateProductBtn.style.display = 'inline';
//     updateProductBtn.setAttribute('onclick', 'updateProduct(' + id + ')');

//     if(enabled == true || enabled == 'true'){
//         disableProductBtn.style.display = 'inline';
//         disableProductBtn.setAttribute('onclick', 'disableProduct(' + id + ')');
//     }else{
//         enableProductBtn.style.display = 'inline';
//         enableProductBtn.setAttribute('onclick', 'enableProduct(' + id + ')');
//     }

//     deleteProductBtn.style.display = 'inline';
//     deleteProductBtn.setAttribute('onclick', 'deleteProduct(' + id + ')');
// }


//=================== Communication to Backend-Server. =========================
// function addProduct(){

//     //extract data from form.
//     const name = document.getElementById('name').value;
//     const company = document.getElementById('company').value;
//     const price = parseFloat(document.getElementById('price').value);
//     const spec = document.getElementById('spec').value;

//     body = {
//         name: name,
//         company: company,
//         price: price,
//         spec: spec
//     };

//     requestExecute('/product', 'post', body)
//     .then(response => {
//         const resName = response.name;
//         alert(resName + ' 가 성공적으로 추가되었습니다.');
//         window.location.assign('');
//     }).catch(error => {
//         alert(error.errorMessage);
//     });
// }

// function updateProduct(id){

//         //extract data from form.
//         const name = document.getElementById('name').value;
//         const company = document.getElementById('company').value;
//         const price = parseFloat(document.getElementById('price').value);
//         const spec = document.getElementById('spec').value;
    
//         body = {
//             id: id,
//             name: name,
//             company: company,
//             price: price,
//             spec: spec
//         };
    
//         requestExecute('/product', 'put', body)
//         .then(response => {
//             const resName = response.name;
//             alert(resName + ' 가 성공적으로 수정되었습니다.');
//             window.location.assign('');
//         }).catch(error => {
//             alert(error.errorMessage);
//         });

// }

// function enableProduct(id){

//     body = {
//         id: id,
//         enabled: true
//     };

//     requestExecute('/product', 'put', body)
//     .then(response => {
//         const resName = response.name;
//         alert("성공적으로 활성화 되었습니다.");
//         window.location.assign('');
//     }).catch(error => {
//         alert(error.errorMessage);
//     });
// }

// function disableProduct(id){
//     requestExecute('/product/' + id, 'PATCH', null)
//     .then(response => {
//         alert('성공적으로 비활성화 되었습니다.');
//         window.location.assign('');
//     }).catch(error => {
//         alert(error.errorMessage);
//     });

// }

// function deleteProduct(id){
//     requestExecute('/product/' + id, 'delete', null)
//     .then(response => {
//         const resName = response.name;
//         alert('성공적으로 삭제 되었습니다.');
//         window.location.assign('');
//     }).catch(error => {
//         alert(error.errorMessage);
//     });
// }