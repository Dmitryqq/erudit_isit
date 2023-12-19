// let socket = new WebSocket("ws://localhost:8080/ws");

// let payload = {
//     "headers": {
//       "Authorization": "Bearer TOKEN",
//       "x-ms-client-request-id": "CLIENT_ID"
//   }, 
//   "content": {
//     "searchSpan": {
//       "from": "UTCDATETIME",
//       "to": "UTCDATETIME"
//     },
//   "top": {
//     "sort": [
//       {
//         "input": {"builtInProperty": "$ts"},
//         "order": "Asc"
//       }], 
//   "count": 1000
//   }}}

// socket.onopen = function () {
//     //Subscribe to the channel
//     console.log("Connected")
//     socket.send(JSON.stringify(payload))
// }

// socket.onmessage = function (msg) {
//     console.log(JSON.parse(msg.data).message);
// }

const headers = {
    Authorization: "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJmYmUwMDEwZS0xYmE5LTQxOWUtYWM3MC03NjVkY2UyOWQwZTgiLCJzdWIiOiJhZG1pbiIsImlkIjoxLCJ1c2VybmFtZSI6ImFkbWluIiwibmFtZSI6ItCU0LzQuNGC0YDQuNC5Iiwic3VybmFtZSI6ItCi0LXRgdGCIiwicGF0cm9ueW1pYyI6ItGC0LXRgdGCIiwicm9sZSI6IlNVUEVSQURNSU4iLCJwd2RDaGFuZ2VSZXF1aXJlZCI6dHJ1ZSwiaWF0IjoxNzAyOTEwMzUxLCJleHAiOjE3MDI5MTc1NTF9.ahweeBo0TtL5eKP6LkHgVbUL8nbjk4sERWUmeQT9jz58WQaByzYcq9IXojUF0kvmQbCiG8sIVS08CIGQOCXNHA"
}

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/ws',
    // brokerURL: 'ws://176.126.164.130:21803/ws',
    connectHeaders: headers,
    debug: function (str) {
        console.log(str);
    },
});

stompClient.beforeConnect = () => {
    console.log("123")
    stompClient.connectHeaders = headers;
};

stompClient.onConnect = (frame) => {
    // setConnected(true);
    console.log('Connected: ' + frame);

    stompClient.subscribe("/user/4/queue/messages", onMessageReceived);
    stompClient.subscribe("/user/4/queue/notifications", onNotificationReceived);

    var chatMessage = {
        // senderId: 1,
        recipientId: 4,
        // senderName: "Test user 1",
        // recipientName: "Test user 2",
        content: "TEST",
        // type: 'CHAT'
    };

    // stompClient.send("/app/chat", headers, JSON.stringify(chatMessage));
    stompClient.publish({ destination: "/app/chat", body: JSON.stringify(chatMessage), headers: headers });
};

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

console.log(stompClient)
stompClient.activate();