let url = 'http://localhost:8080/api/user';

fetch(url)
    .then(res => res.json())
    .then(user => {
        console.log('Шапка заполнена');
        showUserInfo(user);
        console.log(user);
    })
    .catch(error => console.error('Error fetching data:', error));

function showUserInfo(user) {
    console.log('Заполнение таблицы?...')
    let tbody = $("#userDataTable");
    tbody.empty();
    let temp = "";
    temp += "<td>" + user.id + "</td>"
    temp += "<td>" + user.username + "</td>"
    temp += "<td>" + user.email + "</td>"
    temp += "<td>" + user.roles.map(role => role.name.substring(5)) + "</td>"
    tbody.append(temp);
}



