function InfoBill(Id) {
    // Load form
    let PopoverInfoBill = document.getElementById("info-bill");
    let IDBill = document.getElementById("IDBillDeal");
    IDBill.value = Id;

    let foundBill = ALL_BILLS.find(bill => bill.id === Id)
    if (!foundBill) return showToastError("Bill Id is invalid");

    let foundTable = ALL_TABLES.find(table => table.id === foundBill.idTable)
    if (!foundTable) return showToastError("Table Id is invalid")

    // bill information
    let idInput = document.getElementById('IDBillDeal');
    let tableNameInput = document.getElementById('InfoTableDeal');
    let beginAtInput = document.getElementById('DateFromDeal');
    let endAtInput = document.getElementById('DateToDeal');
    let statusInput = document.getElementById('statusDeal');
    let noteInput = document.getElementById('noteDeal');
    let updatedInfor = document.getElementById('updated-bill-infor');

    updatedInfor.innerText = getDateTime(new Date(foundBill.updatedAt))

    let dateIn = new Date(foundBill.beginAt);
    let dateOut = '';
    if (foundBill.endAt) dateOut = new Date(foundBill.endAt)

    idInput.value = foundBill.id
    tableNameInput.value = foundTable.name
    beginAtInput.value = getDateTime(dateIn)
    endAtInput.value = getDateTime(dateOut) || ''
    let status = "Đã hủy"
    if (foundBill.paid) status = "Đã hoàn thành"
    statusInput.value = status
    if(foundBill.canceled)
        noteInput.value = foundBill.user.userName + " : " +foundBill.cancelReason

    //create cancelReason list
    let listCancelReason = document.querySelector('#popover-cancel-reason ul');
    listCancelReason.innerHTML = '';
    let itemIndex = 0;
    foundBill.orderDetails.forEach(orderDetail => {
        if(orderDetail.canceled === true) {
            let liCancelReason = document.createElement('li');
            liCancelReason.innerHTML = `${++itemIndex}. ${orderDetail.product.name} -
             ${orderDetail.amount} - ${orderDetail.cancelReason}`
            listCancelReason.appendChild(liCancelReason);
        }
    });

    // bill items
    let listOrderDetails = document.getElementById('list-order-details');
    listOrderDetails.innerHTML = '';

    let divRow = document.createElement('div')
    divRow.setAttribute('class', 'row-payment')
    let diffHour = parseFloat(getDiffHour(dateIn, dateOut));

    divRow.innerHTML = `
        <div class="col-2">Tiền giờ</div>
        <div class="col-2">${foundBill.endAt ? diffHour : ''}</div>
        <div class="col-2">${numberWithCommas(foundTable.tableType.price)}</div>
        <div class="col-2">${numberWithCommas(foundBill.tablePrice)}</div>
        <div class="col-2">${foundBill.endAt ? numberWithCommas(foundBill.tablePrice * diffHour) : ''}</div>
        `
    listOrderDetails.append(divRow)
    foundBill.orderDetails.forEach(item => {
        let divRow = document.createElement('div')
        divRow.setAttribute('class', 'row-payment')

        divRow.innerHTML = `
        <div class="col-2">${item.product.name}</div>
        <div class="col-2">${item.amount}</div>
        <div class="col-2">${numberWithCommas(item.product.price)}</div>
        <div class="col-2">${numberWithCommas(item.price)}</div>
        <div class="col-2">${numberWithCommas(item.price * item.amount)}</div>
        `
        listOrderDetails.append(divRow)
    })

    // bill payment information

    let billTotalAmount = document.getElementById('bill-total-amount');
    let billTotalMoney = document.getElementById('bill-total-money')
    let billDiscount = document.getElementById('bill-discount')
    let billAfterDiscount = document.getElementById('bill-after-discount');

    let totalAmount = 0;
    if (foundBill.endAt)
        totalAmount = diffHour

    foundBill.orderDetails.forEach(item => {
        if (!item.canceled) totalAmount += item.amount
    })
    billTotalAmount.innerText = totalAmount
    billTotalMoney.innerText = numberWithCommas(foundBill.total)
    billDiscount.innerText = numberWithCommas(foundBill.discount)
    billAfterDiscount.innerText = numberWithCommas(foundBill.total - foundBill.discount)

    let TitleAction = PopoverInfoBill.querySelector(".title-action");
    let ListInput = PopoverInfoBill.querySelectorAll("input:not([readonly])");
    let ListInputAll = PopoverInfoBill.querySelectorAll("input");

    let ButtonList = PopoverInfoBill.querySelector(".button-list");
    let ButtonDone = ButtonList.querySelector(".btn-done");
    let ButtonRestore = ButtonList.querySelector(".btn-restore");
    let ButtonSkip = ButtonList.querySelector(".btn-skip");
    let AskRemove = PopoverInfoBill.querySelector(".AskRemove");


    ButtonDone.style.display = 'none'
    ButtonRestore.style.display = 'flex'
    // .....

    PopoverInfoBill.classList.toggle("activeFlex");

    TitleAction.classList.add("activeFlex");
    SetReadOnly(true, ListInput);

    let ButtonRemove = TitleAction.querySelector(".btn-remove");
    let ButtonEdit = TitleAction.querySelector(".btn-edit");


    ButtonRemove.onclick = function () {
        ButtonDone.style.display = 'flex'
        ButtonRestore.style.display = 'none'

        AskRemove.classList.add("activeFlex");
        SetReadOnly(true, ListInput);
        ButtonDone.onclick = function () {
            fetchMethod("/tableOrders/" + Id, null, "delete")
                .then(res => {
                    if (!VALID_STATUS.includes(res.status)) {
                        return showToastError(res.message)
                    }
                    return window.location.reload();
                })
        }
    }

    ButtonEdit.onclick = function () {
        ButtonDone.style.display = 'none'
        ButtonRestore.style.display = 'flex'
        AskRemove.classList.remove("activeFlex");
        SetReadOnly(false, ListInput);
    }
    ButtonRestore.onclick = function () {
        fetchMethod("/tableOrders/" + Id + "/restore", null, "post")
            .then(res => {
                if (!VALID_STATUS.includes(res.status)) {
                    return showToastError(res.message)
                }
                return window.location.reload();
            })
    }
    PopoverInfoBill.classList.add("activeFlex");
    ButtonSkip.onclick = function () {
        AskRemove.classList.remove("activeFlex");
        PopoverInfoBill.classList.remove("activeFlex");
        for (let i = 0; i < ListInputAll.length; i++) {
            ListInputAll[i].value = "";
        }
    }
}

BillRowClick();

function BillRowClick() {
    let ListRow = document.querySelectorAll(".tab-deal .list-row .row");
    for (let i = 0; i < ListRow.length; i++) {
        ListRow[i].onclick = function () {
            let IDBill = parseInt(this.children[0].parentElement.getAttribute('id'))
            InfoBill(IDBill);
        }
    }
}

function showBills(bills) {
    let listBills = document.getElementById('list-bills')
    listBills.innerHTML = ''
    bills.forEach(bill => {
        let rowDiv = document.createElement('div')
        rowDiv.setAttribute('class', 'row')
        rowDiv.setAttribute('id', bill.id)

        let status = bill.paid ? "Hoàn thành" : "Đã hủy"

        rowDiv.innerHTML = `
            <div class="col-2">HD${bill.id}</div>
            <div class="col-2">${bill.beginAt}</div>
            <div class="col-2">${numberWithCommas(bill.total)}</div>
            <div class="col-2">${numberWithCommas(bill.discount)}</div>
            <div class="col-2"">${status}</div>
        `
        listBills.appendChild(rowDiv)
    })
    BillRowClick();
}


function getBillRadioCheck() {
    let billStatusCheckedType = "StatusDealAll";
    let billTimeChecked = "DateDealNow";

    let billStatusRadios = document.querySelectorAll('input[name="order-status"]')
    billStatusRadios.forEach(item => {
        if (item.checked)
            billStatusCheckedType = item.getAttribute("id")
    })

    let billTimeRadios = document.querySelectorAll('input[name="order-date"]')
    billTimeRadios.forEach(item => {
        if (item.checked)
            billTimeChecked = item.getAttribute("id")
    })
    let bills = ALL_BILLS;
    let labelText = document.querySelector('label[for="DateDealAnother"]').innerText
    switch (billStatusCheckedType) {
        case 'StatusDealAll': {
            if (billTimeChecked === 'DateDealNow') {
                bills = ALL_BILLS.filter(bill => isToday(new Date(bill.beginAt)))
            } else if (billTimeChecked === 'DateDealWeek') {
                let monday = getMonday(new Date())
                bills = ALL_BILLS.filter(bill => compareDate(monday, new Date(bill.beginAt)))
            } else if (billTimeChecked === "DateDealAnother" && labelText !== 'Lựa chọn khác')
                return showBills(findBillsBetweenDays('StatusDealAll'));
            showBills(bills)
            break
        }
        case 'StatusDealCompleted': {
            if (billTimeChecked === 'DateDealNow') {
                bills = ALL_BILLS.filter(bill => isToday(new Date(bill.beginAt)) && bill.paid)
                console.log(bills);
            } else if (billTimeChecked === 'DateDealWeek') {
                let monday = getMonday(new Date())
                bills = ALL_BILLS.filter(bill => compareDate(monday, new Date(bill.beginAt)) && bill.paid)
            } else if (billTimeChecked === "DateDealAnother" && labelText !== 'Lựa chọn khác')
                return showBills(findBillsBetweenDays('StatusDealCompleted'));
            else bills = ALL_BILLS.filter(bill => bill.paid);
            showBills(bills)
            break
        }
        case 'StatusDealCanceled' : {
            if (billTimeChecked === 'DateDealNow') {
                bills = ALL_BILLS.filter(bill => isToday(new Date(bill.beginAt)) && bill.canceled)
            } else if (billTimeChecked === 'DateDealWeek') {
                let monday = getMonday(new Date())
                bills = ALL_BILLS.filter(bill => compareDate(monday, new Date(bill.beginAt)) && bill.canceled)
            } else if (billTimeChecked === "DateDealAnother" && labelText !== 'Lựa chọn khác')
                return showBills(findBillsBetweenDays('StatusDealCanceled'));
            else bills = ALL_BILLS.filter(bill => bill.canceled);
            showBills(bills)
            break
        }
    }
}

GetDateAnother();

function GetDateAnother() {
    let ListLi = document.querySelectorAll("#DateDeal li");
    let LastLi = ListLi[ListLi.length - 1];
    let LabelDate = LastLi.querySelector("label");

    let PopoverDateDeal = document.querySelector(".DateDealAnother");
    let ButtonOK = PopoverDateDeal.querySelector("button");
    let DateFrom = PopoverDateDeal.querySelector("#DateFrom");
    let DateTo = PopoverDateDeal.querySelector("#DateTo");


    LabelDate.onclick = function () {
        PopoverDateDeal.classList.add("activeFlex");
        window.onmouseup = function (event) {
            if (PopoverDateDeal.contains(event.target)) {
                ButtonOK.onclick = function () {
                    if (DateFrom.value < DateTo.value) {
                        DateFrom.classList.remove("errorColor");
                        LabelDate.innerHTML = DateFrom.value + `</br>`+ DateTo.value;
                        PopoverDateDeal.classList.remove("activeFlex");

                        let billStatus = 'StatusDealAll'
                        let billStatusRadios = document.querySelectorAll('input[name="order-status"]')
                        billStatusRadios.forEach(item => {
                            if (item.checked)
                                billStatus = item.getAttribute("id")
                        })

                        showBills(findBillsBetweenDays(billStatus));
                    } else {
                        DateFrom.classList.add("errorColor");
                    }
                }
            } else {
                PopoverDateDeal.classList.remove("activeFlex");
            }
        }

    }
}

function compareDate(smaller, bigger) {
    return smaller.getTime() < bigger.getTime();
}

function getMonday(d) {
    d = new Date(d);
    let day = d.getDay(),
        diff = d.getDate() - day + (day === 0 ? -6 : 1);
    d.setHours(0, 0, 0, 0)
    return new Date(d.setDate(diff));
}

function isToday(someDate) {
    const today = new Date()
    return someDate.getDate() === today.getDate() &&
        someDate.getMonth() === today.getMonth() &&
        someDate.getFullYear() === today.getFullYear()
}

function findBillsBetweenDays(status = 'StatusDealAll') {
    let DateFrom = document.querySelector("#DateFrom");
    let DateTo = document.querySelector("#DateTo");

    //  07:00:00 GMT+0700
    let beginDate = new Date(DateFrom.value);
    beginDate.setHours(beginDate.getHours()-7);
    
    let endDate = new Date(DateTo.value)
    endDate.setHours(endDate.getHours() - 7);
    endDate.setDate(endDate.getDate() + 1);

    if (status === 'StatusDealCompleted')
        return ALL_BILLS.filter(bill => {
            return compareDate(beginDate, new Date(bill.beginAt))
                && compareDate(new Date(bill.beginAt), endDate) && bill.paid
        })
    if (status === 'StatusDealCanceled')
        return ALL_BILLS.filter(bill => {
            return compareDate(beginDate, new Date(bill.beginAt))
                && compareDate(new Date(bill.beginAt), endDate) && bill.canceled
        })
    return ALL_BILLS.filter(bill => {
        return compareDate(beginDate, new Date(bill.beginAt))
            && compareDate(new Date(bill.beginAt), endDate)
    })
}