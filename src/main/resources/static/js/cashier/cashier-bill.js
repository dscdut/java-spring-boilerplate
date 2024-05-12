// button open Merge Split Bill
let mergeBillModal = document.querySelector(".popover-merge-split-bill");
let ButtonDone = mergeBillModal.querySelector(".btn-done");
let ButtonSkip = mergeBillModal.querySelector(".btn-skip");

ButtonDone.addEventListener('click', async event => {
    const status = event.target.getAttribute('status');
    const billId = parseInt(document.getElementById('droplist-label-show').getAttribute('bill-id'), 10)
    let res;
    switch (status) {
        case 'split': {
            const toBillId = parseInt(document.querySelector('#droplist-split1 span').getAttribute('bill-id'));
            if (isNaN(toBillId))
                return;
            let data = {
                fromTableOrderId: billId,
                toTableOrderId: toBillId,
                products: [
                ]
            }

            const listItems = document.querySelector('#split-bill-table .list-row').childNodes;

            listItems.forEach(item => {
                const itemId = parseInt(item.getAttribute('item-id'))
                const splitAmount = parseInt(item.querySelector('input').value)
                let splitItem = {
                    orderDetailId: itemId,
                    splitAmount: splitAmount
                }
                data.products.push(splitItem);
            })

            res = await fetchMethod("/tableOrders/split", data, "post");
            break;
        }
        case 'transfer': {
            let tableId = parseInt(document.querySelector('#droplist-transfer span').getAttribute('table-id'), 10);

            // no option
            if(isNaN(tableId)) return;

            let data = {
                billId,
                tableId
            }

            res = await fetchMethod("/tableOrders/transfer", data, "post");
            break;
        }
    }

    if (VALID_STATUS.includes(res?.status)) {
        return window.location.reload();
    } showToastError(res.message)

    mergeBillModal.classList.remove("droplist-show");
    SplitBill.classList.remove("droplist-show");
    TransferTable.classList.remove("droplist-show");
})

ButtonSkip.onclick = function () {
    mergeBillModal.classList.remove("droplist-show");
    SplitBill.classList.remove("droplist-show");
    TransferTable.classList.remove("droplist-show");
}

let opensplitMergeBtn = document.querySelector(".payment-action-btn");
opensplitMergeBtn.onclick = function () {

    let billId = parseInt(document.getElementById("droplist-label-show").getAttribute("bill-id"), 10)
    let tableId = parseInt(document.getElementById('bill-board').getAttribute('table-id'), 10)
    let tableName = document.getElementById("bill_table-name").innerText;

    let titleModal = mergeBillModal.querySelector("#modal-title");

    if (!billId || !tableName)
        return;

    titleModal.innerText = `HD${billId} - ${tableName}`;

    let transferTableList = document.getElementById('popover-transfer');
    transferTableList.innerHTML = '';
    let rowF = document.createElement('div')
    rowF.setAttribute('class', 'row')
    rowF.setAttribute('table-id', null)
    rowF.innerHTML = `
        <p>Chọn bàn</p>
        <i class="fas fa-check"></i>
    `
    transferTableList.appendChild(rowF);
    let defaultOptionTableList = document.querySelector('#droplist-transfer span');

    // transfer bill
    showTransferBill(billId);
    let splitOptions = document.getElementById("popover-split2");
    splitOptions.innerHTML = `
         <div class="row ">
            <p>Chọn bàn</p>
            <i class="fas fa-check"></i>
        </div>
        `

    let tableOpen = allTables.find(table => table.id === tableId);
    allTables.forEach(table => {
        if (table.id !== tableId
            && table.tableType.id === tableOpen.tableType.id
            && table.status === false) {
            let rowD = document.createElement('div')
            rowD.setAttribute('class', 'row')
            rowD.setAttribute('table-id', table.id)
            rowD.innerHTML = `
            <p>${table.name}</p>
            `

            transferTableList.appendChild(rowD);
        }
    })

    let firstRow = transferTableList.childNodes[0];
    if (firstRow) {
        defaultOptionTableList.innerText = firstRow.textContent;
        defaultOptionTableList.setAttribute('table-id', firstRow.getAttribute('table-id'));
    }

    // split bill
    let listBillsTag = document.getElementById('popover-split1');
    listBillsTag.innerHTML = `
         <div class="row " >
            <p>Chọn hóa đơn</p>
            <i class="fas fa-check"></i>
        </div>
        `
    allBills.forEach(bill => {
        if (bill.id !== billId) {
            let tableName = allTables.find(table => table.id === bill.idTable).name;
            let rowD = document.createElement('div');
            rowD.setAttribute('class', 'row');
            rowD.setAttribute('bill-id', bill.id);
            rowD.innerHTML = `
                    <p>HD${bill.id} - ${tableName}</p>
                `
            listBillsTag.appendChild(rowD);
        }
    })
    const tableBills = allBills.filter(bill => bill.idTable === tableId);
    showSplitListItem(tableBills, billId);
    mergeBillModal.classList.add("droplist-show");
    MergeSplitBill();
}

function MergeSplitBill() {
    let ListRadio = document.querySelectorAll('.checkbox-changing input[type="radio"] ')
    let doneButton = document.querySelector(".btn-done");
    // load
    ListRadio[0].checked = true;
    doneButton.setAttribute('status', 'split')
    SplitBillShow();

    ListRadio[0].onclick = function () {
        doneButton.setAttribute('status', 'split')
        TransferTable.classList.remove("droplist-show");
        SplitBillShow();
    }

    ListRadio[1].onclick = function () {
        doneButton.setAttribute('status', 'transfer')
        SplitBill.classList.remove("droplist-show");
        TransferTableShow();
    }
}

let SplitBill = document.querySelector(".split-bill-wrap");
let TransferTable = document.querySelector(".transfer-table-wrap");


function SplitBillShow() {
    SplitBill.classList.add("droplist-show");
    SplitBillChoose();
}

function TransferTableShow() {
    TransferTable.classList.add("droplist-show");
    let DropListSelect = document.querySelector("#droplist-transfer");
    let DropListSpan = DropListSelect.querySelector("span");
    let PopoverTransfer = document.getElementById("popover-transfer");
    ChooseChanging(DropListSelect, PopoverTransfer, DropListSpan);
}

function SplitBillChoose() {
    let DropListSelect1 = document.querySelector("#droplist-split1");

    let DropListSpan1 = DropListSelect1.querySelector("span");

    let PopoverSplit1 = document.getElementById("popover-split1");

    DropListSelect1.onclick = function () {
        PopoverSplit1.classList.add("droplist-show");
        DropListSelect1.classList.add("droplist-select-active");

        window.onmouseup = function (event) {

            if (PopoverSplit1.contains(event.target.parentNode)) {
                const spanTitle = document.querySelector('#droplist-split1 span');
                const billId = event.target.closest('.row').getAttribute('bill-id');
                spanTitle.setAttribute('bill-id', billId);

                PopoverSplit1.querySelector("i").remove();
                if (event.target.tagName === "P") {
                    event.target.parentNode.innerHTML += `<i class="fas fa-check"></i>`;
                } else {
                    event.target.innerHTML += `<i class="fas fa-check"></i>`;
                }

                DropListSpan1.textContent = event.target.textContent;
                PopoverSplit1.classList.remove("droplist-show");
                DropListSelect1.classList.remove("droplist-select-active");
            } else {
                PopoverSplit1.classList.remove("droplist-show");
                DropListSelect1.classList.remove("droplist-select-active");
            }
        }
    }
}

function ChooseChanging(DropListSelect, PopoverMergeBill, DropListSelectSpan) {
    DropListSelect.onclick = function () {
        PopoverMergeBill.classList.add("droplist-show");
        DropListSelect.classList.add("droplist-select-active");
        window.onmouseup = function (event) {
            if (PopoverMergeBill.contains(event.target.parentNode)) {
                PopoverMergeBill.querySelector("i")?.remove();
                if (event.target.tagName === "P") {
                    let tableId = event.target.parentNode.getAttribute('table-id');
                    DropListSelectSpan.setAttribute('table-id', tableId)
                    event.target.parentNode.innerHTML += `<i class="fas fa-check"></i>`;
                } else {
                    let tableId = event.target.getAttribute('table-id');
                    DropListSelectSpan.setAttribute('table-id', tableId)
                    event.target.innerHTML += `<i class="fas fa-check"></i>`;
                }
                DropListSelectSpan.textContent = event.target.textContent;
                PopoverMergeBill.classList.remove("droplist-show");
                DropListSelect.classList.remove("droplist-select-active");

            } else {
                PopoverMergeBill.classList.remove("droplist-show");
                DropListSelect.classList.remove("droplist-select-active");
            }
        }
    }
}

function CellQuantityChangeNumber(evt) {
    let ASCIICode = (evt.which) ? evt.which : evt.keyCode
    return !(ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57));

}

// use at html
function SetMaxQuantity(evt) {
    let MaxQuantity = parseInt(evt.target.parentNode.parentNode.querySelector(".cell-quantity-old p").textContent);
    if (parseInt(evt.target.value - MaxQuantity) > 0) {
        evt.target.value = MaxQuantity;
        return false;
    }
    if (evt.target.value < 0) {
        evt.target.value = 0;
        return false;
    }
}

function showTransferBill(billId) {
    let foundBill = allBills.find(bill => bill.id === billId);
    let tbody = document.querySelector('#choose-bill-transfer tbody')
    tbody.innerHTML = '';

    let endTime = new Date();
    if (foundBill.endAt)
        endTime = new Date(foundBill.endAt);

    let totalHour = getDiffHour(new Date(foundBill.beginAt), endTime);

    let totalPrice = (foundBill.tablePrice * totalHour);

    let totalAmount = 0;

    foundBill.orderDetails.forEach(item => {
        totalPrice += (item.amount * item.price);
        totalAmount += item.amount;
    })

    let trow = document.createElement('tr');
    trow.innerHTML = `
        <td>HD${foundBill.id}</td>
        <td>${totalAmount}</td>
        <td>${numberWithCommas(totalPrice)}</td>
        `
    tbody.appendChild(trow);

}

function showSplitListItem(tableBills, billId) {
    let billDetailTag = document.getElementById('split-bill-table').querySelector('.list-row');
    let foundBill = tableBills.find(bill => bill.id === billId);
    billDetailTag.innerHTML = '';
    let index = 1;
    foundBill.orderDetails.forEach(item => {
        let rowD = document.createElement('div');
        rowD.setAttribute('class', 'row');
        rowD.setAttribute('item-id', item.id);

        rowD.innerHTML = `
        <div class="cell-order">
            <p>${index++}</p>
        </div>
        <div class="cell-name">
            ${item.product.name}
        </div>
        <div class="cell-quantity-old">
            <p>${item.amount}</p>
        </div>
        <div class="cell-quantity-change">
            <input type="number" value="0" max="2" onChange="SetMaxQuantity(event)" onkeyup=" return CellQuantityChangeNumber(event)" >
        </div>
        `
        billDetailTag.appendChild(rowD);
    })
}