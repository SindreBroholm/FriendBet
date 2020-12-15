'use strict';

let senderName = document.querySelector('#senderUN').innerHTML;

let senderId = document.querySelector('#senderId').innerHTML;
let recipientId = document.querySelector('#recipient').innerHTML;



function sendMessage(event) {
    let messageContent = document.querySelector('#chatMessage').value.trim();

    if (messageContent && stompClient) {
        console.log(recipientId);
        let chatMessage = {
            sender: senderName,
            content: document.querySelector('#chatMessage').value,
            senderId: senderId,
            recipientId: recipientId
        };
        let notification = {
            sender: senderName,
            type: "chatMessage",
            senderId: senderId,
            recipientId: recipientId
        }

        stompClient.send("/app/chat.sendMessage", {}, JSON
            .stringify(chatMessage));
        stompClient.send("/app/notification.sendNotification", {}, JSON
            .stringify(notification));
        document.querySelector('#chatMessage').value = '';
    }
    event.preventDefault();
}


//get message from backend and present it to view.
function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    console.log(message);
    let messageElement = document.createElement('li');


    if (message.type === 'newUser') {
        messageElement.classList.add('event-data');
        message.content = message.sender + 'has joined the chat';
    } else if (message.type === 'Leave') {
        messageElement.classList.add('event-data');
        message.content = message.sender + 'has left the chat';
    } else {
        messageElement.classList.add('message-data');

        let divNode = document.createElement('div');
        let iNode = document.createElement('i');
        let text = document.createTextNode(message.senderName[0]);
        iNode.appendChild(text);

        let pNode = document.createElement('p');
        let messageText = document.createTextNode(message.content);
        pNode.appendChild(messageText);

        if (message.senderName === senderName){
            divNode.classList.add('currentUser');
            divNode.appendChild(pNode);
            divNode.appendChild(iNode);
        } else {
            divNode.classList.add('recipientUser');
            divNode.appendChild(iNode);
            divNode.appendChild(pNode);
        }

        messageElement.appendChild(divNode);
    }

    document.querySelector('#messageList').appendChild(messageElement);
    document.querySelector('#messageList').scrollTop = document
        .querySelector('#messageList').scrollHeight;
}

gotoBottom('messageList');
function gotoBottom(id){
    let element = document.getElementById(id);
    element.scrollTop = element.scrollHeight - element.clientHeight;
}

document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true);
