let closeModalBtn = document.querySelectorAll(".close-modal");
let confirmModal = document.querySelector("#confirm-remove");
const modalDiv = document.querySelector(".modal-user-infor");

// Modal huy mon

closeModalBtn.forEach(item => {
    item.onclick = closeModal;
})

function closeModal() {
    modalDiv.classList.remove("open");
    confirmModal.classList.remove("bill-id")
    confirmModal.classList.remove("item-id")
}

function openModal(target) {
    let cancelTitle = document.getElementById("cancel-title");
    let cancelItemModal = document.getElementById("cancel-item");

    if (target.tagName === "DIV") {
        let rowList = target.closest(".row-list");
        let orderDetailId = rowList.getAttribute("order-detail-id");
        let itemName = rowList.querySelector(".cell-name").innerText

        // set title for modal
        cancelTitle.innerText = "Xác nhận hủy món"

        // set bill-id for confirm button
        confirmModal.setAttribute("item-id", orderDetailId)

        // set item name for span field
        cancelItemModal.innerText = itemName;
    } else if (target.tagName === "I") {
        let dropItem = target.closest(".drop__item");
        let billId = dropItem.getAttribute("bill-id");

        // set title for modal
        cancelTitle.innerText = "Hủy HD" + billId;

        // set item name
        cancelItemModal.innerText = "HD" + billId;

        // set bill-id for confirm button
        confirmModal.setAttribute("bill-id", billId)
    }
    modalDiv.classList.add("open");
}

confirmModal.addEventListener("click", async event => {
    let deleteBillId = parseInt(event.target.getAttribute("bill-id"), 10);
    let itemId = parseInt(event.target.getAttribute("item-id"), 10);
    let reason = modalDiv.querySelector("textarea").value;
    let tableId = parseInt(document.getElementById("bill-board")
        .getAttribute("table-id"), 10)

    let billId = parseInt(document.getElementById("droplist-label-show").getAttribute("bill-id"), 10)

    let route = "";
    if (reason.length === 0) return;

    if (!isNaN(itemId)) {
        route = "/orderDetails/" + itemId + "/cancel";
    } else if (!isNaN(deleteBillId)) {
        route = "/tableOrders/" + deleteBillId + "/cancel";
    }

    let res = await fetchMethod(route, {reason}, "post")

    if (!VALID_STATUS.includes(res.status)) {
        return showToastError(res.message)
    }

    if(res.status === 405) {
        return showToastError('Bạn không có có quyền thực hiện hành động này!')
    }

    fetchTableOrders()
        .then(() => {
            if (tableId) {
                // delete bill
                let tableBills = allBills.filter(bill => bill.idTable === tableId) || []
                if (!isNaN(deleteBillId)) {
                    // if there are no bills
                    if (!tableBills) {
                        fetchTables();
                        setEmptyBill()
                    } else return window.location.reload();
                } else showBillItems(tableBills, null, billId);

                closeModal();
            }
        })

})

