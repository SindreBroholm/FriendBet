'use strict';

let socket = new SockJS('/websocketApp');
let stompClient = Stomp.over(socket);
stompClient.connect({}, connectionSuccess);
let notificationCount = 0;
function connectionSuccess() {
    stompClient.subscribe('/topic/chat', onMessageReceived);
    stompClient.subscribe('/topic/notification', onNotificationReceived);
}

//get message from backend and present it to view.
function onNotificationReceived(payload) {
    notificationCount++;
    let notification = JSON.parse(payload.body);
    let notificationElement = document.createElement('li');
    notificationElement.classList.add('notification-data');


    let notificationDiv = document.createElement('div');
    notificationDiv.classList.add('recipientUser');
    let notificationContentNode = document.createElement('p');
    let notificationContent = document.createTextNode(notification.content);

    notificationContentNode.appendChild(notificationContent);
    notificationDiv.appendChild(notificationContentNode);
    notificationElement.appendChild(notificationDiv);

    document.querySelector('#NotificationList').appendChild(notificationElement);
    document.querySelector('#NotificationList').scrollTop = document
        .querySelector('#messageList').scrollHeight;

    notificationContentNode.style.background = '#1D9F91';

    notificationContentNode.addEventListener('click', function (event) {
        notificationCount--;
        notificationContentNode.style.background = '#DB7C88';
    });
}

function showDropDownMenu(ele) {
    let buttonNode = ele;
    let DDM = document.getElementById("dropDownLinks")
    buttonNode.addEventListener('click', function () {
        if (DDM.style.display === 'none') {
            DDM.style.display = 'block';
        } else {
            DDM.style.display = 'none';
        }
    });
}

function showNotification(ele) {
    let buttonNode = ele;
    let DDM = document.getElementById("NotificationList")
    buttonNode.addEventListener('click', function () {
        if (DDM.style.display === 'none') {
            DDM.style.display = 'block';
        } else {
            DDM.style.display = 'none';
        }
    });
}
