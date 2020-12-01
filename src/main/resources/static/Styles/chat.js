'use strict';

let socket = new SockJS('/websocketApp');
let stompClient = Stomp.over(socket);
stompClient.connect({}, connectionSuccess);

let senderName = document.querySelector('#senderUN').innerHTML;
let senderId = document.querySelector('#senderId').innerHTML;
let recipientId = document.querySelector('#recipient').innerHTML;

function connectionSuccess() {
    stompClient.subscribe('/topic/chat', onMessageReceived);
}

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

        stompClient.send("/app/chat.sendMessage", {}, JSON
            .stringify(chatMessage));
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

        let element = document.createElement('i');
        let text = document.createTextNode(message.senderName[0]);
        element.appendChild(text);

        messageElement.appendChild(element);

        let usernameElement = document.createElement('span');
        let usernameText = document.createTextNode(message.senderName);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }


    let textElement = document.createElement('p');
    let messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    document.querySelector('#messageList').appendChild(messageElement);
    document.querySelector('#messageList').scrollTop = document
        .querySelector('#messageList').scrollHeight;
}


document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true);
