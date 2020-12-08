
let socket = new SockJS('/websocketApp');
let stompClient = Stomp.over(socket);
stompClient.connect({}, connectionSuccess);

let recipientId = document.querySelector('#recipientId').innerHTML;

function connectionSuccess() {
    stompClient.subscribe('/topic/notification');
}
function sendMessage() {
        let notification = {
            type: "friendRequest",
            recipientId: recipientId
        }
        stompClient.send("/app/notification.sendNotification", {}, JSON
            .stringify(notification));
}

document.querySelector('#sendFriendRequest').addEventListener('click', sendMessage, true);
