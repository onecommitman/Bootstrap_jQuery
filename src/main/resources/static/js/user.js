let url = 'http://localhost:8080/api/user';
let currentUser = {};

fetch(url)
    .then(res => res.json())
    .then(user => {
        currentUser = user;
        console.log(user);
        showUserInfo(user);
    })
    .then(() => {
        const headerSpanElement = document.querySelector('.nav-link.active.h5');
        headerSpanElement.innerHTML = currentUser.email;
        const h5RolesElement = document.querySelector('.text-white.fw-normal.roleValues')
        h5RolesElement.innerHTML = currentUser.roles[0].name.substring(5);
    })
    .catch(error => console.error('Error fetching data:', error));

function showUserInfo(user) {
    console.log(`Current user's email - ${currentUser.email}.`);
    console.log(`Current user's roles: ${user.roles[0].name}`);
    console.log(`${user.username} with ID = ${user.id} and email ${user.email} was authenticated.`);
    let userDataElem = document.getElementById('userDataTable');
    let temp = "";
    //temp += "<tr>"
    temp += "<td>" + user.id + "</td>"
    temp += "<td>" + user.username + "</td>"
    temp += "<td>" + user.email + "</td>"
    temp += "<td>" + user.roles.map(role => role.name.substring(5)) + "</td>"
    //temp += "</tr>"
    userDataElem.innerHTML = temp;
}

// const headerSpanElement = document.getElementById('.nav-link active h5');
// headerSpanElement.innerHTML = currentUser.email;

// document.addEventListener('DOMContentLoaded', function () {
//     // Получаем ссылку на элемент по классу
//     const headerSpanElement = document.querySelector('.nav-link.active.h5');
//
//     // Проверяем, что элемент существует, прежде чем изменять его содержимое
//     if (headerSpanElement) {
//         // Устанавливаем новое значение текста
//         console.log('Элемент существует!!!')
//         headerSpanElement.innerHTML = currentUser.email;
//     } else {
//         console.log('Элемента не существует!!!')
//     }
// })


