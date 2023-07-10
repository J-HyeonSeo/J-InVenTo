import { TableManager, TableOnClickSet } from "../manager/table-manager.js";
import { doSearchFilter } from "../viewer/mainfilter.js";
import { loadUserDatas } from "../loader/user-loader.js";
import { requestExecute } from "./request.js";

class UserModifier{

    constructor(){

        //related user modal
        this.userModal = document.getElementById('user-modal');
        this.userTable = document.getElementById('user-table');
        this.searchElement = document.getElementById('search');
        this.filter = document.getElementById('filter');
        this.userDatas = null;

        //related edit form input
        this.usernameInput = document.getElementById('username');
        this.nameInput = document.getElementById('name');
        this.departmentInput = document.getElementById('department');
        this.submitBtn = document.getElementById('submit');
        this.passwordInitializeBtn = document.getElementById('password-initialize');


        this.unsettedPermissions = document.getElementById('unsetted-permissions');
        this.settedPermissions = document.getElementById('setted-permissions');
    }

    initialize(){

        this.usernameInput.addEventListener('click', this.openUserModal.bind(this));
        
        this.userTableManager = new TableManager(this.userTable,
            ['username', 'name', 'department'],
            ['아이디', '이름', '부서'],
            [],
            new TableOnClickSet(this.clickUserAfter.bind(this), 'username'));
        
        this.userTableManager.table_initiallize();

        //이벤트 추가
        this.searchElement.addEventListener('input', this.filterByInput.bind(this));
        this.submitBtn.addEventListener('click', this.editSubmit.bind(this));
        this.passwordInitializeBtn.addEventListener('click', this.passwordInitialize.bind(this));
    }

    permission_initialize(){
        //permission initialize

        const permissions = [
                "ROLE_PRODUCT_READ",
                "ROLE_BOM_READ",
                "ROLE_PURCHASE_READ",
                "ROLE_INBOUND_READ",
                "ROLE_STOCKS_READ",
                "ROLE_OUTBOUND_READ",
                "ROLE_PLAN_READ",
                "ROLE_PRODUCT_MANAGE",
                "ROLE_BOM_MANAGE",
                "ROLE_PURCHASE_MANAGE",
                "ROLE_INBOUND_MANAGE",
                "ROLE_STOCKS_MANAGE",
                "ROLE_OUTBOUND_MANAGE",
                "ROLE_PLAN_MANAGE",
                "ROLE_ADMIN"];
    
        const displayNames =[
                '품목 조회',
                'BOM 조회',
                '구매 조회',
                '입고 조회',
                '재고 조회',
                '출고 조회',
                '출고 계획 조회',
                '품목 관리',
                'BOM 관리',
                '구매 관리',
                '입고 관리',
                '재고 관리',
                '출고 관리',
                '출고 계획 관리',
                '관리자'];
        
        this.unsettedPermissions.innerHTML = '';

        for(let i = 0; i < permissions.length; i++){
            const p = permissions[i];
            const n = displayNames[i];

            const li = document.createElement('li');

            li.setAttribute('data-role', p);
            li.textContent = n;

            this.unsettedPermissions.appendChild(li);
        }

        this.settedPermissions.innerHTML = '';
    }

    //권한 부여
    setPermission(event){
        event.stopPropagation();
        
        this.settedPermissions.appendChild(event.target);
        event.target.addEventListener('click', this.releasePermission.bind(this), {once: true});
    }

    //권한 해제
    releasePermission(event){
        event.stopPropagation();
        
        this.unsettedPermissions.appendChild(event.target);
        event.target.addEventListener('click', this.setPermission.bind(this), {once: true});
    }

    clickUserAfter(username){
        var targetUser = null;

        this.userDatas.forEach(item => {
            if(item.username == username){
                targetUser = item;
            }
        });

        if(targetUser == null){
            alert('존재하지 않는 유저입니다.');
            return;
        }

        //모달폼 닫기
        hideModal('user-modal');

        //입력값 셋팅
        this.usernameInput.value = targetUser.username;
        this.nameInput.value = targetUser.name;
        this.departmentInput.value = targetUser.department;

        //권한 셋팅
        this.permission_initialize();

        const settedLi = [];

        targetUser.roles.forEach(item => {
            for(let i = 0; i < this.unsettedPermissions.childElementCount; i++){
                const tagName = this.unsettedPermissions.children[i].dataset.role;
                if(item == tagName){
                    settedLi.push(this.unsettedPermissions.children[i]);
                    break;
                }
            }
        })

        settedLi.forEach(item => {
            //선택 권한 이벤트 추가
            item.addEventListener('click', this.releasePermission.bind(this), {once: true});
            this.settedPermissions.appendChild(item);
        })

        //미선택 권한 이벤트 추가

        for(let item of this.unsettedPermissions.children){
            item.addEventListener('click', this.setPermission.bind(this), {once: true});
        }

    }

    openUserModal(){
        openModal('user-modal');

        loadUserDatas()
        .then(response => {
            this.userDatas = response;
            this.userTableManager.set_table_content(response);
        }).catch(error => {
            alert(error);
        })
    }

    //user modal filtering
    filterByInput(){

        const searchVal = this.searchElement.value;
        const filterName = this.filter.value;
    
        const filteredDatas = doSearchFilter(this.userDatas, filterName, searchVal);
    
        this.userTableManager.set_table_content(filteredDatas);

    }



    //==================== Communucation Back-end Server =========================

    editSubmit(){
        const username = this.usernameInput.value;
        const name = this.nameInput.value;
        const department = this.departmentInput.value;

        if(username.trim() == ""){
            alert('수정할 유저가 선택되지 않았습니다.');
            return;
        }

        if(name.trim() == ""){
            alert("이름이 비어있습니다.");
            return;
        }

        if(department.trim() == ""){
            alert("소속이 비어있습니다.");
            return;
        }

        if(confirm("회원을 수정하시겠습니까?")){

            const roles = [];

            //권한 추출
            for(let item of this.settedPermissions.children){
                const role = item.dataset.role;
                roles.push(role);
            }


            const body = {
                username: username,
                name: name,
                department, department,
                roles: roles
            };

            requestExecute('/auth/admin/update/profile', 'put', body)
            .then(response => {
                alert('성공적으로 회원정보가 수정되었습니다.');
            }).catch(error => {
                alert(error);
            })

        }
    }

    passwordInitialize(){
        const username = this.usernameInput.value;

        if(username.trim() == ""){
            alert('초기화 할 대상이 선택되지 않았습니다.');
            return;
        }

        const password = prompt("초기화 할 비밀번호를 입력해주세요.");

        if(password == null){
            return;
        }

        const body = {
            username: username,
            password: password
        }

        requestExecute('/auth/admin/update/password', 'put', body)
        .then(response => {
            alert("성공적으로 해당 유저의 비밀번호가 초기화되었습니다.");
        }).catch(error => {
            alert(error);
        })
    }

}

window.addEventListener('load', function() {

    const userModifier = new UserModifier();
    userModifier.initialize();

});