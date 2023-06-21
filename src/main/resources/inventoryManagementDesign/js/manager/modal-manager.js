function hideModal(event){

    if(event.target.className == 'modal-container'){
        event.target.style.display = 'none';
    }
}

function openModal(modalID){
    const targetModal = document.getElementById(modalID);
    targetModal.style.display = 'block';
    return targetModal;
}