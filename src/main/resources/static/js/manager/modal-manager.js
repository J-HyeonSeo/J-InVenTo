window.addEventListener('click', function(event){

    if(event.target.className == 'modal-container'){
        event.target.style.display = 'none';
    }

})

function hideModal(modalID){
    document.getElementById(modalID).style.display = 'none';
}

function openModal(modalID){
    const targetModal = document.getElementById(modalID);
    targetModal.style.display = 'block';
    return targetModal;
}