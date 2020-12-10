'use strict';

let socket = new SockJS('/websocketApp');
let stompClient = Stomp.over(socket);
stompClient.connect({}, connectionSuccess);

function connectionSuccess() {
    stompClient.subscribe('/topic/notification', onMessageReceived);
}

//get message from backend and present it to view.
function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    let messageElement = document.createElement('li');
    messageElement.classList.add('notification-data');


    let divNode = document.createElement('div');
    divNode.classList.add('recipientUser');
    let pNode = document.createElement('p');
    let text = document.createTextNode(message.content);

    pNode.appendChild(text);
    divNode.appendChild(pNode);
    messageElement.appendChild(divNode);


    document.querySelector('#messageList').appendChild(messageElement);
    document.querySelector('#messageList').scrollTop = document
        .querySelector('#messageList').scrollHeight;
}