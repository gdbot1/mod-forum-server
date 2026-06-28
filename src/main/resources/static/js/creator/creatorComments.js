const input = document.getElementById('input-message');
const chat = document.getElementById('chat');

personal_id = Math.random().toString(36);

var socket = SockJS('http://141.138.122.172:80/ws');
var stompClient = Stomp.over(socket);

/*
const headers = {
    jwt_token: '',
    session_key: 'b1e0c9e4-e1b8-4c0a-b1b4-b0f133e00149'
}
*/

function getCookie(name) {
    const cookies = document.cookie.split(';');
    for (const cookie of cookies) {
        const [key, value] = cookie.trim().split('=');
        if (key === name) return value;
    }
    return null;
}

/*
stompClient.onstomperror = (error) => {
    if (error.headers && error.headers.message === "INVALID_SESSION") {
        window.location.href = "/login?expired";
    }
};
*/

stompClient.connect({
    jwt_token: getCookie('jwt_token') || '',
    session_key: getCookie('session_key') || ''
}, () => {
    stompClient.subscribe('/topic/creator/' + id + '/comments', (dto)=>{
        const data = JSON.parse(dto.body);

        addMessage(data);
    });

    historySubscription = stompClient.subscribe('/queue/creator/' + id + '/comments/' + personal_id + '/history', (dto)=>{
        const arr = JSON.parse(dto.body);

        arr.forEach(data => addMessage(data));

        historySubscription.unsubscribe();
    });

    stompClient.subscribe('/user/queue/refresh', (jwt)=>{
        console.log('update access jwt tocken: ' + jwt.body);
        document.cookie = `jwt_token=${jwt.body}; path=/; max-age=${15 * 60}`;
        //localStorage.setItem('jwt_token', jwt.body)
    });

    stompClient.send('/app/creator/' + id + '/' + personal_id + '/comments.join', {}, '');
}, (error) => {
    if (error.headers && error.headers.message === "INVALID_SESSION") {
        window.location.href = "/login?expired";
    }
});

//Отправка сообщения серверу
function sendMessage() {
    if (input.value == "") {
        return; //Если входное поле пустое - не отправляем пустое сообщение
    }

    stompClient.send('/app/creator/' + id + '/comments.send', {
        jwt_token: getCookie('jwt_token') || '',
        session_key: getCookie('session_key') || ''
    }, JSON.stringify({
        message: input.value,
        senderId: null
    }));

    input.value = "" //После отправки сообщения - форма очищается
}

//Дополняет #chat блок новым сообщением
function addMessage(data) {
    var name_block = `<b class="sender-name"></b>`;

    if (data.senderId != null && data.senderId != "") {
        name_block = `<a class="sender-name-link sender-name" href="/creator/${data.senderId}"></a>`;
    }

    const div = document.createElement('div');
    div.className = 'message-block';
    div.innerHTML = `
        <img class="sender-image" src="/images/creators/${data.senderImage}">
        <div>
            <b>
                ${name_block}
                <span class="message-date"></span>
            </b>
            <span class="message-message"></span>
        </div>
    `;

    div.querySelector('.message-date').textContent = data.date;
    div.querySelector('.message-message').textContent = data.message;
    div.querySelector('.sender-name').textContent = data.senderName;

    chat.prepend(div);
}