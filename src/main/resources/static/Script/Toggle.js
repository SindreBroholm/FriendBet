function showNotification(ele) {
    let buttonNode = ele;
    let notifyDropDown = document.getElementById("NotificationList")
    buttonNode.addEventListener('click', function () {
        if (notifyDropDown.style.display === 'none') {
            notifyDropDown.style.display = 'block';
            gotoBottom('NotificationList');
        } else {
            notifyDropDown.style.display = 'none';
        }
    });
}

function showDropDownMenu(ele) {
    let buttonNode = ele;
    let menuDropDown = document.getElementById("dropDownLinks")
    buttonNode.addEventListener('click', function () {
        if (menuDropDown.style.display === 'none') {
            menuDropDown.style.display = 'block';
        } else {
            menuDropDown.style.display = 'none';
        }
    });
}
function toggleFriendMenu(element) {
    let divNode = document.getElementById(element.id);
    let formNode = divNode.lastElementChild;

    if (formNode.style.display === 'none') {
        formNode.style.display = 'block';
    } else {
        formNode.style.display = 'none';
    }
}