let currentAdminUrl = 'http://localhost:8080/api/admin/current';
let getAllUsersUrl = 'http://localhost:8080/api/admin/';
const rolesApiUrl = `http://localhost:8080/api/roles`;
let currentUser = {};
let allUsersList = {};


//Заполнение шапки
async function fillHeader(){
    await fetch(currentAdminUrl)
        .then(res => res.json())
        .then(user => {
            currentUser = user;
            console.log('Страница загружена, а шапка?')
            $("#currentAdminsEmail").text(currentUser.email);
            $("#currentAdminRoles").text((user.roles.map(role => role.name.substring(5))));
            console.log(user);
        })
        .catch(error => console.error('Error fetching data:', error));
};

/*Вкладка с таблицей с информацией о текущем админе*/
$('#v-pills-user-tab').on('click', function() {

    console.log('Нажата кнока юзер в левой панели...')

    showUserInfo(currentUser)


});

//Заполняется строка в таблице с данными юзера
function showUserInfo(user) {
    let tbody = $("#userDataTable");
    tbody.empty();
    let temp = "";
    temp += "<td>" + user.id + "</td>"
    temp += "<td>" + user.username + "</td>"
    temp += "<td>" + user.email + "</td>"
    temp += "<td>" + user.roles.map(role => role.name.substring(5)) + "</td>"
    tbody.append(temp);
}


/*Вывод таблицы со всеми пользователями*/
$(document).ready(function() {
    // Функция, которая будет срабатывать при загрузке страницы
    async function onPageLoad() {
        console.log('Страница загружена');
        await fillHeader();
        showUsersTable();

    }

    // Функция, которая будет срабатывать при нажатии кнопки
    function onButtonClicked() {
        console.log('Кнопка вкладки админа нажата');
        showUsersTable();
    }

    // Вызываем функцию при загрузке страницы
    onPageLoad();

    // Назначаем обработчик события на кнопку с id="v-pills-admin-tab"
    $('#v-pills-admin-tab').on('click', function() {

        onButtonClicked();
    });
});

//Отобразить таблицу с юзерами
async function showUsersTable(){
    let tbody = $('#mainUsersTable');
    tbody.empty();
    console.log('Попытка заполнить таблицу пользователями...')
    await fetch(getAllUsersUrl)
        .then(res => res.json())
        .then(users => allUsersList = users);

    $.each(allUsersList, function (index, user) {
        // Создаем новую строку в теле таблицы
        let row = $('<tr>');

        // Добавляем ячейки с данными из JSON
        row.append($('<td>').text(user.id));
        row.append($('<td>').text(user.username));
        row.append($('<td>').text(user.email));
        row.append($('<td>').text(user.rolesString));
        let editButton = $('<button>').attr({
            type: 'button',
            class: 'btn btn-info',
            'data-bs-toggle': 'modal',
            'data-bs-target': '#editModal',
            style: 'background: #17a2b8; color: #fffffb;'
        }).text('Edit');

        editButton.data('userId', user.id);

        let deleteButton = $('<button>')
            .attr({
                type: 'button',
                class: 'btn btn-danger',
                'data-bs-toggle': 'modal',
                'data-bs-target': '#deleteModal',
            })
            .text('Delete');
        deleteButton.data('userId', user.id);
        // Добавляем кнопку в ячейку
        row.append($('<td>').append(editButton));
        row.append($('<td>').append(deleteButton));

        // Добавляем строку в тело таблицы
        tbody.append(row);
    });
}
/* $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$_EDIT_MODAL_$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ */
function fillEditForm(user) {
    // Заполняем поля формы на основе JSON объекта
    $('#editModalUserId').val(user.id);
    $('#editModalUsername').val(user.username);
    $('#editModalEmail').val(user.email);
    $('#editModalPassword').val("********");  // Заглушка

    // Сбрасываем предыдущие выбранные значения в select
    $('#roles').val(null);

    // Выбираем нужные опции в select
    $.each(user.roles, function (index, role) {
        $('#roles option[value="' + role.id + '"]').prop('selected', true);
    });
}

// При открытии модального окна вызываем функцию fillDeleteForm
$('#editModal').on('show.bs.modal', function (event) {

    let button = $(event.relatedTarget);  // Кнопка, вызвавшая модальное окно
    let editingUsersId = button.data('userId');  // Получаем JSON объект из data атрибута кнопки
    console.log(button.data('userId'));
    let currentUser = {};
    fetch(getAllUsersUrl + editingUsersId)
        .then(res => res.json())
        .then(user => {
            currentUser = user;
            console.log(user);
            fillEditForm(user);
        })
        .catch(error => console.error('Error fetching data:', error));
      // Заполняем форму данными
});

//Если id == 1, это обычный юзер, если id == 2, то в правах и юзер и админ
async function getRolesById(id) {
    let roles = [];

    if (id === 1) {
        const response1 = await fetch(`${rolesApiUrl}/${id}`);
        const jsonResult1 = await response1.json();
        roles.push(jsonResult1);
    } else if (id === 2) {
        const response2 = await fetch(`${rolesApiUrl}/${1}`);
        const jsonResult2 = await response2.json();
        roles.push(jsonResult2);

        const response3 = await fetch(`${rolesApiUrl}/${id}`);
        const jsonResult3 = await response3.json();
        roles.push(jsonResult3);
    }

    return roles;
}

//Сохранение измененного юзера в БД
async function saveEditedUser() {
    // Получаем значения из формы и создаем объект formData
    let formData = {
        id: $('#editModalUserId').val(),
        username: $('#editModalUsername').val(),
        email: $('#editModalEmail').val(),
        password: $('#editModalPassword').val(),
        roles: await getRolesById(parseInt($('#roles').val()))
    };


    let requestOptions = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    };

    fetch(getAllUsersUrl + formData.id, requestOptions)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Обработка успешного ответа от сервера
            console.log('Данные успешно отправлены на сервер:', data);
            /*$('#editModal').modal('hide');
            showUsersTable();*/
        })
        .catch(error => {
            // Обработка ошибки при отправке данных
            console.error('Ошибка при отправке данных:', error);
        });

}

$(document).ready(function () {
    // Отлавливаем событие submit формы
    $('#editModalForm').on('submit', function (event) {
        // Предотвращаем стандартное поведение формы (перезагрузку страницы)
        event.preventDefault();

        // Вызываем функцию для сохранения данных
        saveEditedUser();
    });

});
/* $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$_DELETE_MODAL_$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ */

$('#deleteModal').on('show.bs.modal', function (event) {
    let button = $(event.relatedTarget);  // Кнопка, вызвавшая модальное окно
    let userIdToDelete = button.data('userId');  // Получаем значение из data атрибута кнопки
    console.log(userIdToDelete);

    let currentUser = {};
    fetch(getAllUsersUrl + userIdToDelete)
        .then(res => res.json())
        .then(user => {
            currentUser = user;
            console.log(user);
            fillDeleteForm(user);
        })
        .catch(error => console.error('Error fetching data:', error));
    // Заполняем форму данными

});

//Заполнение модального окна данными удаляемого юзера
function fillDeleteForm(user) {
    // Заполняем поля формы на основе JSON объекта
    $('#deleteModalUserId').val(user.id);
    $('#deleteModalUsername').val(user.username);
    $('#deleteModalEmail').val(user.email);
    $('#deleteModalPassword').val("********");  // Заглушка
    let roles = user.roles;
    let $selectElement = $('#deletedUserRoles');

    // Очищаем существующие опции
    $selectElement.empty();

    // Проходимся по массиву ролей пользователя и добавляем их в селект
    $.each(roles, function(index, role) {
        let optionElement = $('<option></option>').attr('value', role.id).text(role.authority.replace('ROLE_', ''));
        $selectElement.append(optionElement);
    });
}
//Отправка запроса на удаление
$('#deleteUser').click(async function(event) {
    event.preventDefault();
    // Извлекаем ID пользователя из поля формы в модальном окне
    let userId = $('#deleteModalUserId').val();
    console.log(`С ID ${userId}`);
    await fetch(getAllUsersUrl + userId, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            // Обработка ответа от сервера
            if (response.ok) {
                // Успешное удаление пользователя
                console.log('User deleted successfully');
            } else {
                // Обработка ошибок удаления пользователя
                console.error('Error deleting user');
            }
            $("#closeDeleteModal").click();
            showUsersTable();
        })
        .catch(error => {
            // Обработка ошибок сети и других исключений
            console.error('Network error or other exception:', error);
        });
});

//Отправка на сервер данных из формы нового юзера
$('#newUserForm').submit(async function (event) {
    event.preventDefault(); // Предотвращаем стандартное поведение формы (перезагрузку страницы)

    // Получаем данные из формы
    let formData = {
        username: $('#newUsername').val(),
        email: $('#newUserEmail').val(),
        password: $('#newUserPassword').val(),
        roles: await getRolesById(parseInt($('#newUserRoles').val()))
    };

    console.log(`form data: `);
    console.log(formData);
    //console.log(await getRolesById(parseInt($('#roles').val())));

    let requestOptions = {
        method: 'POST',  // Используем метод PUT для обновления данных на сервере
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    };

    // Выполняем fetch запрос
    fetch(getAllUsersUrl, requestOptions)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Обработка успешного ответа от сервера
            console.log('Данные успешно отправлены на сервер:', data);
            let targetButton = $('#home-tab');

            // Переключение на вкладку со всемит пользователями
            showUsersTable();
            targetButton.trigger('click');

        })
        .catch(error => {
            // Обработка ошибки при отправке данных
            console.error('Ошибка при отправке данных:', error);
        });


});
