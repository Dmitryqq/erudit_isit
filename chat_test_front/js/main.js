'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var sender = null;
var receiver = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

var headers = {
    Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJiMWY5ZjNlZS0xNjlkLTQ3YjItODNlYi1jNjY4ZmQ0NzMyZmYiLCJzdWIiOiJzYW1hdCIsImlkIjo0LCJ1c2VybmFtZSI6InNhbWF0IiwibmFtZSI6ItCh0LDQvNCw0YIiLCJzdXJuYW1lIjoi0JzRi9C60YLRi9Cx0LXQutC-0LIiLCJwYXRyb255bWljIjoi0JjRiNCw0L3QvtCy0LjRhyIsInJvbGUiOiJTVFVERU5UIiwicHdkQ2hhbmdlUmVxdWlyZWQiOnRydWUsImlhdCI6MTcwMjgyNDc1NywiZXhwIjoxNzAyODMxOTU3fQ.9yyYMr3s5o93WDE4G7hRnMDQ2M3mUIXBKe_aUV1Toymkl-r4sNMXR4VPZCvEF7MyylO_aBcYp7v127WQhmQfCA"
}

function connect(event) {
    sender = document.querySelector('#name').value.trim().split("_")[0];
    receiver = document.querySelector('#name').value.trim().split("_")[1];

    if (sender) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('http://localhost:8080/ws');
        // var socket = new SockJS('ws://176.126.164.130:21803/ws');
        // socket.withCredentials = true;

        stompClient = Stomp.over(socket);

        stompClient.connect(headers, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe("/user/" + sender + "/queue/messages", onMessageReceived);
    stompClient.subscribe("/user/" + sender + "/queue/notifications", onNotificationReceived);

    // Tell your username to the server
    // stompClient.send("/api/v1/chat/chat.register", headers,
    //     JSON.stringify({ sender: sender, type: 'JOIN' })
    // )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Невозможно подключиться к WebSocket! Обновите страницу и повторите попытку или обратитесь к администратору.';
    connectingElement.style.color = 'red';
}


function send(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            senderId: parseInt(sender),
            recipientId: parseInt(receiver),
            senderName: "Test user 1",
            recipientName: "Test user 2",
            content: messageInput.value,
            // type: 'CHAT'
        };

        stompClient.send("/app/chat", headers, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onNotificationReceived(payload) {
    var message = JSON.parse(payload.body);
    console.log(message)
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    console.log(message)

    var messageElement = document.createElement('li');

    // if (message.type === 'JOIN') {
    //     messageElement.classList.add('event-message');
    //     message.content = message.sender + ' joined!';
    // } else if (message.type === 'LEAVE') {
    //     messageElement.classList.add('event-message');
    //     message.content = message.sender + ' left!';
    // } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.senderName);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.senderName);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    // }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', send, true)