removeTrailingZeros()
let ButtonAddTable = document.getElementById("btn-add-table")
let ALL_TABLES = []
fetchTables()

async function fetchTables() {
    ALL_TABLES = await fetchMethod("/tables");
}

ButtonAddTable.onclick = function () {
    AddTable()
}

function AddTable(IDTable = null) {
    let PopoverAddTable = document.getElementById("add-table")

    let Title = PopoverAddTable.querySelector(".title p")
    let TitleAction = PopoverAddTable.querySelector(".title-action")

    let ListInput = PopoverAddTable.querySelectorAll("input:not([readonly])")
    let ListInputAll = PopoverAddTable.querySelectorAll("input")
    let tableStatusField = document.getElementById('table-status-form-div');

    let ButtonList = PopoverAddTable.querySelector(".button-list")
    let ButtonDone = ButtonList.querySelector(".btn-done")
    let ButtonSkip = ButtonList.querySelector(".btn-skip")
    let AskRemove = PopoverAddTable.querySelector(".AskRemove")

    let typeSpan = document.querySelector('#type-table span');
    let tableNameForm = document.getElementById('nameTable');
    let statusTrue = document.getElementById('statusTableTrue');
    let statusFalse = document.getElementById('statusTableFalse');

    if (IDTable) {
        let foundTable = ALL_TABLES.find(table => table.id === IDTable)
        if (!foundTable) return;

        tableNameForm.value = foundTable.name;
        typeSpan.innerText = foundTable.tableType.name;
        typeSpan.setAttribute('table-type-id', foundTable.tableType.id)
        foundTable.deletedAt ? statusFalse.checked = true : statusTrue.checked = true;

        // Load form
        Title.textContent = "Thông tin bàn"

        TitleAction.classList.add("activeFlex")
        SetReadOnly(true, ListInput)
        PopoverRoleTable(true, IDTable)
        tableStatusField.style.display = 'flex'
        let ButtonRemove = TitleAction.querySelector(".btn-remove")
        let ButtonEdit = TitleAction.querySelector(".btn-edit")


        ButtonRemove.onclick = function () {
            AskRemove.classList.add("activeFlex")
            tableStatusField.style.display = 'none';
            SetReadOnly(true, ListInput)
            PopoverRoleTable(true)

            ButtonDone.onclick = function () {
                fetchMethod("/tables/" + IDTable, null, "delete")
                    .then(res => {
                        if (!VALID_STATUS.includes(res.status)) {
                            return showToastError(res.message)
                        }
                        return window.location.reload();
                    })
            }
        }

        ButtonEdit.onclick = function () {
            AskRemove.classList.remove("activeFlex")
            SetReadOnly(false, ListInput)
            PopoverRoleTable(false)

            ButtonDone.onclick = function () {
                let name = document.getElementById('nameTable').value;
                let idTableType = parseInt(document.querySelector('#type-table span').getAttribute('table-type-id'), 10)
                if (name === '' || !idTableType || Number.isNaN(idTableType)) {
                    return showToastError("Name and table type shouldn't be empty")
                }
                let isRestore = statusTrue.checked;
                let data = {
                    name,
                    idTableType,
                    isRestore
                }

                fetchMethod("/tables/" + IDTable, data, "put")
                    .then(res => {
                        if (!VALID_STATUS.includes(res.status)) {
                            return showToastError(res.message)
                        }
                        return window.location.reload();
                    })
            }
        }
    } else {
        Title.textContent = "Thêm bàn"
        TitleAction.classList.remove("activeFlex")
        SetReadOnly(false, ListInput)
        PopoverRoleTable(false)
        tableStatusField.style.display = 'none';
        ButtonDone.onclick = function () {
            let name = document.getElementById('nameTable').value;
            let idTableType = parseInt(document.querySelector('#type-table span').getAttribute('table-type-id'), 10)
            if (name === '' || !idTableType || Number.isNaN(idTableType)) {
                return showToastError("Name and Table Type can't be empty")
            }

            let data = {
                name,
                idTableType
            }

            fetchMethod("/tables", data, "post")
                .then(res => {
                    if (!VALID_STATUS.includes(res.status)) {
                        return showToastError(res.message)
                    }
                    return window.location.reload();
                })
        }
    }

    PopoverAddTable.classList.add("activeFlex")
    ButtonSkip.onclick = function () {
        AskRemove.classList.remove("activeFlex")
        PopoverAddTable.classList.remove("activeFlex")
        for (let i = 0; i < ListInputAll.length; i++) {
            ListInputAll[i].value = ""
        }
    }
}

function PopoverRoleTable(status) {
    let InputRole = document.querySelector("#type-table") // loai ban
    let PopoverRole = document.querySelector("#popover-type-table")
    let RoleSpan = InputRole.querySelector("span")


    if (status === true) {
        console.log(status);
        // let typeTableSpan = InputRole.querySelector('span');
        return
    }
    InputRole.onclick = function () {
        PopoverRole.classList.toggle("activeFlex")
        window.onmouseup = function (event) {
            if (PopoverRole.contains(event.target)) {
                if (event.target.tagName === "LI") {
                    let id = event.target.getAttribute('table-type-form-id')
                    RoleSpan.textContent = event.target.textContent;
                    RoleSpan.setAttribute('table-type-id', id);
                    PopoverRole.classList.remove("activeFlex");
                }
            } else {
                PopoverRole.classList.remove("activeFlex");
            }
        }
    }
}

function TypeTable(IDTypeTable = null) {
    let PopoverAddTypeTable = document.getElementById("add-type-table")
    let ListInput = PopoverAddTypeTable.querySelectorAll("input:not([readonly])")
    let ListInputAll = PopoverAddTypeTable.querySelectorAll("input")

    PopoverAddTypeTable.classList.toggle("activeFlex")
    let ButtonList = PopoverAddTypeTable.querySelector(".button-list")
    let ButtonDone = ButtonList.querySelector(".btn-done")
    let ButtonSkip = ButtonList.querySelector(".btn-skip")
    let AskRemove = PopoverAddTypeTable.querySelector(".AskRemove")
    let Title = PopoverAddTypeTable.querySelector(".title p")
    let TitleAction = PopoverAddTypeTable.querySelector(".title-action")

    if (IDTypeTable) {
        // Load form
        Title.textContent = "Thông tin loại bàn"

        SetReadOnly(true, ListInput)

        let ButtonRemove = TitleAction.querySelector(".btn-remove")
        let ButtonEdit = TitleAction.querySelector(".btn-edit")

        let curElement = document.querySelector(`#list-table-types input[id="type${IDTypeTable}"]`)
        let label = curElement.nextElementSibling.innerText;
        let price = curElement.getAttribute('price');


        let typeName = document.getElementById('NameTypeTable');
        let typePrice = document.getElementById('PriceTypeTable');

        typeName.value = label;
        typePrice.value = numberWithCommas(parseFloat(price));

        ButtonRemove.onclick = function () {
            AskRemove.classList.add("activeFlex")
            SetReadOnly(true, ListInput)
            ButtonDone.onclick = function () {
                fetchMethod("/tableTypes/" + IDTypeTable, null, "delete")
                    .then(res => {
                        if (!VALID_STATUS.includes(res.status)) {
                            return showToastError(res.message)
                        }
                        return window.location.reload();
                    })
            }
        }

        ButtonEdit.onclick = function () {
            AskRemove.classList.remove("activeFlex")
            SetReadOnly(false, ListInput)
            ButtonDone.onclick = function () {
                let typeName = document.getElementById('NameTypeTable').value;
                let typePrice = document.getElementById('PriceTypeTable').value.replaceAll(',', '');
                let data = {
                    name: typeName,
                    price: typePrice
                }
                if (typeName === '' || typePrice === '') {
                    return showToastError("Table Type name and price shouldn't be empty")
                }
                fetchMethod("/tableTypes/" + IDTypeTable, data, "put")
                    .then(res => {
                        if (!VALID_STATUS.includes(res.status)) {
                            return showToastError(res.message)
                        }
                            return window.location.reload();
                    })

            }
        }
        TitleAction.classList.add("activeFlex")
    } else {
        Title.textContent = "Thêm loại bàn"
        TitleAction.classList.remove("activeFlex")
        SetReadOnly(false, ListInput)

        ButtonDone.onclick = async function () {
            let name = document.getElementById('NameTypeTable').value;
            let price = parseFloat(document.getElementById('PriceTypeTable').value.replaceAll(',', ''))
            let data = {
                name,
                price
            }
            if (name === '' || Number.isNaN(price)) {
                return showToastError("Table Type name and price shouldn't be empty")
            }

            let res = await fetchMethod("/tableTypes", data, "post")
            if (!VALID_STATUS.includes(res.status)) {
                return showToastError(res.message)
            }

            let listTableTypes = document.getElementById('list-table-types');
            let liTag = document.createElement('li');

            liTag.innerHTML = `
             <input type="radio" name="table-type" id="type${res.detail.id}" price="${res.detail.price}" onchange="getTableRadioCheck(this)">
            <label for="type${res.detail.id}">${res.detail.name}</label>
            <i class="fas fa-edit"></i>
            `
            // when add edit table type
            let listTableTypesForm = document.querySelector('#popover-type-table ul');
            let formLiTag = document.createElement('li');
            formLiTag.setAttribute('table-type-form-id', res.detail.id)
            formLiTag.innerText = res.detail.name

            listTableTypesForm.append(formLiTag)
            listTableTypes.appendChild(liTag)
            TableSideBarClick()
            PopoverAddTypeTable.classList.remove("activeFlex")
        }
    }

    PopoverAddTypeTable.classList.add("activeFlex")
    ButtonSkip.onclick = function () {
        AskRemove.classList.remove("activeFlex")
        PopoverAddTypeTable.classList.remove("activeFlex")
        for (let i = 0; i < ListInputAll.length; i++)
            ListInputAll[i].value = ""
    }
}

// add event click for tables
TableRowClick()

function TableRowClick() {
    let ListRow = document.querySelectorAll(".tab-table .list-row .row")
    for (let i = 0; i < ListRow.length; i++) {
        ListRow[i].onclick = function () {
            let IDTable = parseInt(this.children[0].parentElement.getAttribute('table-id'))
            AddTable(IDTable)
        }
    }
}

TableSideBarClick()

function TableSideBarClick() {
    let ListTypeTable = document.querySelector("#TypeTable")
    let ListButtonEdit = ListTypeTable.querySelectorAll("li i");
    let ButtonAddType = ListTypeTable.querySelector(".btn-add-type")
    for (let i = 0; i < ListButtonEdit.length; i++) {
        ListButtonEdit[i].onclick = function (event) {
            let inputId = parseInt(event.target.parentNode.querySelector('input').getAttribute('id').split('type')[1])
            TypeTable(inputId)
        }
    }
    ButtonAddType.onclick = function () {
        TypeTable()
    }
}

function InputPrice(target) {
    let rawNumber = target.value.replaceAll(/[^0-9]/g, '');
    let parsed = parseInt(rawNumber);
    if (isNaN(parsed)) return target.value = 0;
    target.value = numberWithCommas(parsed)
}

function getTableRadioCheck() {
    let tableStatusCheckedType = "all-status-table";
    let tableTypeChecked = "TypeTableAll";

    let tableStatusRadios = document.querySelectorAll('input[name="table-status"]')
    tableStatusRadios.forEach(item => {
        if (item.checked)
            tableStatusCheckedType = item.getAttribute("id")
    })

    let tableTypeRadios = document.querySelectorAll('input[name="table-type"]')
    tableTypeRadios.forEach(item => {
        if (item.checked)
            tableTypeChecked = item.getAttribute("id")
    })
    switch (tableStatusCheckedType) {
        case 'all-status-table': {
            let tables = ALL_TABLES

            let parsedId = parseInt(tableTypeChecked.split('type')[1], 10)
            if (!isNaN(parsedId) && parsedId >= 1)
                tables = ALL_TABLES.filter(table => table.tableType.id === parsedId)
            showTables(tables)
            break
        }
        case 'available-table': {
            let tables = ALL_TABLES.filter(table => table.deletedAt === null)

            let parsedId = parseInt(tableTypeChecked.split('type')[1], 10)
            if (!isNaN(parsedId) && parsedId >= 1)
                tables = ALL_TABLES.filter(table => (table.tableType.id === parsedId && table.deletedAt === null))

            showTables(tables)
            break
        }
        case 'unavailable-table' : {
            let tables = ALL_TABLES.filter(table => table.deletedAt !== null)

            let parsedId = parseInt(tableTypeChecked.split('type')[1], 10)
            if (!isNaN(parsedId) && parsedId >= 1)
                tables = ALL_TABLES.filter(table => (table.tableType.id === parsedId && table.deletedAt !== null))
            showTables(tables)
            break
        }
    }
}

function showTables(tables) {
    let listTables = document.getElementById('list-tables')
    listTables.innerHTML = ''
    tables.forEach(table => {
        let rowDiv = document.createElement('div')
        rowDiv.setAttribute('class', 'row')
        rowDiv.setAttribute('table-id', table.id)

        let status = table.deletedAt ? "Ngừng hoạt động" : "Hoạt động"

        rowDiv.innerHTML = `
            <div class="col-3">${table.name}</div>
            <div class="col-3">${table.tableType.name}</div>
            <div class="col-2">${numberWithCommas(table.tableType.price)}</div>
            <div class="col-2"">${status}</div>
        `
        listTables.appendChild(rowDiv)
    })
    TableRowClick()
}

function removeTrailingZeros() {
    let numbers = document.querySelectorAll('.trailing-zero');
    numbers.forEach(number => {
        let n = number.innerText.replaceAll(',', '')
        number.innerText = numberWithCommas(parseFloat(n));
    })
}

function searchItem(target) {
    let selectingItem = document.querySelector('.side-bar-item.active-item').getAttribute('name_tab');
    let inputText = target.value;
    let pattern = new RegExp(`.*${inputText}.*`, 'i');

    switch (selectingItem) {
        case 'tab-table': {
            if (inputText === '') {
                getTableRadioCheck()
                return;
            }
            let foundTables = ALL_TABLES.filter(table => table.name.match(pattern));
            if (foundTables) {
                showTables(foundTables)
            }
            break
        }
        case 'tab-product': {
            if (inputText === '') {
                getProductRadioCheck()
                return;
            }
            let foundProducts = ALL_PRODUCTS.filter(product => product.name.match(pattern));
            if (foundProducts) {
                showProducts(foundProducts)
            }
            break
        }

        case 'tab-user': {
            if (inputText === '') {
                getUserRadioCheck()
                return;
            }
            let foundUsers = ALL_USERS.filter(user => user.userName.match(pattern) || user.email.match(pattern));
            if (foundUsers) {
                showUsers(foundUsers)
            }
            break
        }
    }
}