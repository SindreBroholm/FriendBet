function sendFriendRequest(id) {
    let notification = {
        type: "friendRequest",
        recipientId: id
    }
    stompClient.send("/app/notification.sendNotification", {}, JSON
        .stringify(notification));
}

function sendFriendRequestResponse(id) {
    let notification = {
        type: "acceptFriend",
        recipientId: id
    }
    stompClient.send("/app/notification.sendNotification", {}, JSON
        .stringify(notification));
}