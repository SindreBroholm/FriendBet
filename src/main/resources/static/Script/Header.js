'use strict';

let socket = new SockJS('/websocketApp');
let stompClient = Stomp.over(socket);
stompClient.connect({}, connectionSuccess);
let notificationCount = 0;
let url = location.href.split("/");
function connectionSuccess() {
    if (url[3] === 'room'){
        stompClient.subscribe('/topic/chat', onMessageReceived);
    }
    stompClient.subscribe('/topic/notification', onNotificationReceived);
}

//get message from backend and present it to view.
function onNotificationReceived(payload) {
    let NotificationAlertNode = document.getElementById('notificationBox');
    NotificationAlertNode.style.background = '#DB7C88';
    notificationCount++;



    let notification = JSON.parse(payload.body);
    let notificationElement = document.createElement('li');

    notificationElement.classList.add('notification-data');
    let notificationDiv = document.createElement('div');
    notificationDiv.classList.add('recipientUser');
    let notificationContentNode = document.createElement('p');
    notificationContentNode.style.background = '#DB7C88';

    let notificationContent = document.createTextNode(notification.content);
    notificationContentNode.appendChild(notificationContent);

    notificationDiv.appendChild(notificationContentNode);
    notificationElement.appendChild(notificationDiv);

    document.querySelector('#NotificationList').appendChild(notificationElement);



    notificationContentNode.addEventListener('click', function (event) {
        notificationCount--;
        notificationContentNode.style.background = '#1D9F91';
    });

    NotificationAlertNode.addEventListener('click', function (){
        NotificationAlertNode.style.background = '#1D9F91';
        document.querySelector('#NotificationList').scrollTop = document
            .querySelector('#NotificationList').scrollHeight;

    })
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

function showNotification(ele) {
    let buttonNode = ele;
    let notifyDropDown = document.getElementById("NotificationList")
    buttonNode.addEventListener('click', function () {
        if (notifyDropDown.style.display === 'none') {
            notifyDropDown.style.display = 'block';
        } else {
            notifyDropDown.style.display = 'none';
        }
    });
}
