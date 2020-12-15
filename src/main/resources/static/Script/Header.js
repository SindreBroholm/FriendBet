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
    let notification = JSON.parse(payload.body);

    if(notification.senderId === user){

    }else {
        let NotificationAlertNode = document.getElementById('notificationBox');
        NotificationAlertNode.style.background = '#DB7C88';
        notificationCount++;

        let notificationElement = document.createElement('li');

        notificationElement.classList.add('notification-data');
        let notificationDiv = document.createElement('div');
        notificationDiv.classList.add('NotificationRecipient');
        let notificationContentNode = document.createElement('a');
        notificationContentNode.style.background = '#DB7C88';

        if (notification.type === 'chatMessage') {
            notificationContentNode.setAttribute('href', 'http://localhost:8080/chat/' + notification.senderId);
        }

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
            NotificationAlertNode.style.background = '#4C4452';
        });
    };
}

function gotoBottom(id){
    let element = document.getElementById(id);
    element.scrollTop = element.scrollHeight - element.clientHeight;
}