let ButtonAddUser = document.getElementById("btn-add-user");
let ALL_USERS = []
fetchUsers()

async function fetchUsers() {
    ALL_USERS = await fetchMethod("/users");
}

ButtonAddUser.onclick = function () {
    AddUser();
}

function AddUser(IDAccount = null) {
    let PopoverAddUser = document.getElementById("add-user");

    let Title = PopoverAddUser.querySelector(".title p");
    let TitleAction = PopoverAddUser.querySelector(".title-action");
    let ButtonRemove = TitleAction.querySelector(".btn-remove");
    let ButtonEdit = TitleAction.querySelector(".btn-edit");

    let ButtonList = PopoverAddUser.querySelector(".button-list");
    let ButtonDone = ButtonList.querySelector(".btn-done");
    let ButtonSkip = ButtonList.querySelector(".btn-skip");
    let AskRemove = PopoverAddUser.querySelector(".AskRemove");
    let formPassword = document.getElementById('form-password')
    let formConfirmPassword = document.getElementById('form-confirm-password')
    let userStatusTrue = document.getElementById('statusUserTrue')
    let userStatusFalse = document.getElementById('statusUserFalse')
    userStatusTrue.checked = true

    let nameInput = document.getElementById('nameUser');
    let emailInput = document.getElementById('accountUser');
    let passwordInput = document.getElementById('passwordUser');
    let confirmPasswordInput = document.getElementById('confirmPasswordUser');
    let roleInput = document.querySelector('#Input-Role-User span');


    let statusFormDiv = document.getElementById('user-status-form-div');

    let ListInput = PopoverAddUser.querySelectorAll("input:not([readonly])");
    let ListInputAll = PopoverAddUser.querySelectorAll("input");
    if (IDAccount) {
        statusFormDiv.style.display = 'flex'
        formPassword.style.display = 'none';
        formConfirmPassword.style.display = 'none'

        let foundUser = ALL_USERS.find(user => user.id === IDAccount);
        if (!foundUser) return showToastError(`Not found User has ID ${IDAccount}`)

        // Load Form
        Title.textContent = "Thông tin người dùng";

        nameInput.value = foundUser.userName;
        emailInput.value = foundUser.email;
        roleInput.textContent = foundUser.role
        foundUser.deletedAt ? userStatusFalse.checked = true : null;

        // .......
        TitleAction.classList.add("activeFlex");
        SetReadOnly(true, ListInput);
        PopoverRoleUser(true);
        // Button Action evnt

        ButtonRemove.onclick = function () {
            AskRemove.classList.add("activeFlex");
            SetReadOnly(true, ListInput);
            PopoverRoleUser(true);
            ButtonDone.onclick = function () {
                fetchMethod("/users/" + IDAccount, null, "delete")
                    .then(res => {
                        if (!VALID_STATUS.includes(res.status)) {
                            return showToastError(res.message)
                        }
                        return window.location.reload()
                    })
            }
        }

        ButtonEdit.onclick = function () {
            userStatusFalse.disabled = true;
            AskRemove.classList.remove("activeFlex");
            formPassword.style.display = 'flex';
            formConfirmPassword.style.display = 'flex'
            SetReadOnly(false, ListInput);
            PopoverRoleUser(false);
            ButtonDone.onclick = function () {
                let userName = nameInput.value
                let email = emailInput.value
                let password = passwordInput.value
                let confirmPassword = confirmPasswordInput.value
                let role = roleInput.innerText
                let isRestore = userStatusTrue.checked;

                if (email === '' || userName === '') {
                    showToastError('email and password can\'t be empty')
                    return
                }

                let data = {
                    userName, email, role, isRestore
                }

                if (password !== '') {
                    if (password !== confirmPassword) {
                        showToastError("Password doesn't match")
                        return;
                    } else {
                        data['password'] = password;
                        data['confirmPassword'] = confirmPassword
                    }
                }
                fetchMethod("/users/" + IDAccount, data, "put")
                    .then(res => {
                        if (!VALID_STATUS.includes(res.status)) {
                            return showToastError(res.message)
                        }
                        return window.location.reload()
                    })
            }
        }
    } else {
        Title.textContent = "Thêm người dùng";
        TitleAction.classList.remove("activeFlex");
        statusFormDiv.style.display = 'none'
        formPassword.style.display = 'flex';
        formConfirmPassword.style.display = 'flex'
        SetReadOnly(false, ListInput);
        PopoverRoleUser(false);

        ButtonDone.onclick = function () {
            let userName = nameInput.value
            let email = emailInput.value
            let password = passwordInput.value
            let confirmPassword = confirmPasswordInput.value
            let role = roleInput.innerText
            if (email === '' || password === '' || confirmPassword === '' || userName === '')
                return showToastError("Fields can't be empty")

            if(password !== confirmPassword) return  showToastError("password doesn't match!")
            let data = {
                userName, email, password, confirmPassword, role
            }
            fetchMethod("/users", data, "post")
                .then(res => {
                    if (!VALID_STATUS.includes(res.status)) {
                        return showToastError(res.message)
                    }
                    return window.location.reload()
                })
        }
    }

    PopoverAddUser.classList.add("activeFlex");
    ButtonSkip.onclick = function () {
        AskRemove.classList.remove("activeFlex");
        PopoverAddUser.classList.remove("activeFlex");
        for (let i = 0; i < ListInputAll.length; i++) {
            ListInputAll[i].value = "";
        }

        let ListError = document.querySelectorAll(".tab-user .errorColor");
        for (let i = 0; i < ListError.length; i++) {
            ListError[i].classList.remove("errorColor");
        }
    }
}

function SetReadOnly(status, List) {
    if (status === true) {
        for (i = 0; i < List.length; i++) {
            List[i].disabled = true;
        }
    } else {
        for ( let i = 0; i < List.length; i++) {
            List[i].disabled = false;
        }
    }
}


function PopoverRoleUser(status) {
    let InputRole = document.querySelector("#Input-Role-User");
    let PopoverRole = document.querySelector("#popover-role-User");
    let RoleSpan = InputRole.querySelector("span");

    if (status === true) {
        InputRole.onclick = function () {
        };
    } else {
        InputRole.onclick = function () {
            PopoverRole.classList.toggle("activeFlex");
            window.onmouseup = function (event) {
                if (PopoverRole.contains(event.target)) {
                    if (event.target.tagName === "LI") {
                        RoleSpan.textContent = event.target.textContent;
                        PopoverRole.classList.remove("activeFlex");
                    }
                } else {
                    PopoverRole.classList.remove("activeFlex");
                }
            }
        }
    }

}

UserRowClick();

function UserRowClick() {
    let ListRow = document.querySelectorAll(".tab-user .list-row .row");
    for (let i = 0; i < ListRow.length; i++) {
        ListRow[i].onclick = function () {
            let IDAccount = parseInt(this.children[0].parentElement.getAttribute('user-id'));
            AddUser(IDAccount);
        }
    }
}

function showUsers(users) {
    let listUsers = document.getElementById('list-users')
    listUsers.innerHTML = '';

    users.forEach(user => {
        let rowDiv = document.createElement('div')
        rowDiv.setAttribute('class', 'row')
        rowDiv.setAttribute('user-id', user.id)

        let status = user.deletedAt ? "Ngừng hoạt động" : "Hoạt động"

        rowDiv.innerHTML = `
            <div class="col-5-2">${user.userName}</div>
            <div class="col-5-2">${user.email}</div>
            <div class="col-5-2">${user.role}</div>
            <div class="col-5-2">${status}</div>
        `
        listUsers.appendChild(rowDiv)
    })
    UserRowClick()
}

function InputNumber(evt) {
    let ASCIICode = (evt.which) ? evt.which : evt.keyCode
    return !(ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57));
}

function PasswordLength(target) {
    if (target.value.length < 6) {
        target.parentNode.classList.toggle("errorColor");
        return;
    }
    target.parentNode.classList.remove("errorColor");
}

function ConfirmPassword(target) {
    let Password = document.querySelector("#passwordUser").value;
    let ConfirmPassword = target.value;
    if (Password !== ConfirmPassword) {
        target.parentNode.classList.toggle("errorColor");
        return;
    }
    target.parentNode.classList.remove("errorColor");
}

function getUserRadioCheck() {
    let userStatusCheckedType = "StatusUserAll";
    let userRoleChecked = "TypeUserAll";

    let userStatusRadios = document.querySelectorAll('input[name="user-status"]')
    userStatusRadios.forEach(item => {
        if (item.checked)
            userStatusCheckedType = item.getAttribute("id")
    })

    let userRoleRadios = document.querySelectorAll('input[name="user-role"]')
    userRoleRadios.forEach(item => {
        if (item.checked)
            userRoleChecked = item.getAttribute("id")
    })
    const ALLOWED_ROLES = ['ADMIN', 'STAFF']
    switch (userStatusCheckedType) {
        case 'StatusUserAll': {
            let users = ALL_USERS

            if (ALLOWED_ROLES.includes(userRoleChecked))
                users = ALL_USERS.filter(user => user.role === userRoleChecked)
            showUsers(users)
            break
        }
        case 'StatusUserWork': {
            let users = ALL_USERS.filter(user => user.deletedAt === null)

            if (ALLOWED_ROLES.includes(userRoleChecked))
                users = ALL_USERS.filter(user => (user.role === userRoleChecked && user.deletedAt === null))

            showUsers(users)
            break
        }
        case 'StatusUserNotWork' : {
            let users = ALL_USERS.filter(user => user.deletedAt !== null)

            if (ALLOWED_ROLES.includes(userRoleChecked))
                users = ALL_USERS.filter(user => (user.role === userRoleChecked && user.deletedAt !== null))
            showUsers(users)
            break
        }
    }
}
