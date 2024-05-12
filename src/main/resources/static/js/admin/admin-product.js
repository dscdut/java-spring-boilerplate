let ButtonAddProduct = document.getElementById("btn-add-product");
let ALL_PRODUCTS = [];
ButtonAddProduct.onclick = function () {
    AddProduct();
}
fetchProducts()

async function fetchProducts() {
    ALL_PRODUCTS = await fetchMethod("/products");
}

function AddProduct(productId = null) {
    let PopoverAddProduct = document.getElementById("add-product");
    let Title = PopoverAddProduct.querySelector(".title p");
    let TitleAction = PopoverAddProduct.querySelector(".title-action");
    let ButtonRemove = TitleAction.querySelector(".btn-remove");
    let ButtonEdit = TitleAction.querySelector(".btn-edit");
    let ListInput = PopoverAddProduct.querySelectorAll("input:not([readonly])");
    let ListInputAll = PopoverAddProduct.querySelectorAll("input");

    let ButtonList = PopoverAddProduct.querySelector(".button-list");
    let ButtonDone = ButtonList.querySelector(".btn-done");
    let ButtonSkip = ButtonList.querySelector(".btn-skip");
    let AskRemove = PopoverAddProduct.querySelector(".AskRemove");

    let idInput = document.getElementById('idProduct');
    let nameInput = document.getElementById('nameProduct');
    let priceInput = document.getElementById('priceProduct');
    let unitInput = document.getElementById('productUnit');
    let imgInput = document.getElementById('productImg');
    let quantityInput = document.getElementById('quantityRemainProduct');
    let cateSpan = document.querySelector('#type-product span');

    let statusForm = document.getElementById('product-status-form-div');

    let radioTrue = document.getElementById('statusProductTrue');
    let radioFalse = document.getElementById('statusProductFalse');


    if (productId) {
        // Load form
        Title.textContent = "Thông tin hàng hóa";

        TitleAction.classList.add("activeFlex");
        SetReadOnly(true, ListInput);
        PopoverRoleProduct(true);
        let foundProduct = ALL_PRODUCTS.find(product => product.id === productId)
        if (!foundProduct) return showToastError("Product Id is invalid")
        idInput.value = foundProduct.id
        nameInput.value = foundProduct.name
        priceInput.value = parseFloat(foundProduct.price)
        unitInput.value = foundProduct.unit
        imgInput.value = foundProduct.imageUrl
        quantityInput.value = parseFloat(foundProduct.quantityRemain)
        cateSpan.innerText = foundProduct.category?.name || ''
        cateSpan.setAttribute('id', foundProduct.category?.id || null)

        statusForm.style.display = 'flex';
        foundProduct.deletedAt ? radioFalse.checked = true : radioTrue.checked = true;

        ButtonRemove.onclick = function () {
            AskRemove.classList.add("activeFlex");
            SetReadOnly(true, ListInput);
            PopoverRoleProduct(true);
            ButtonDone.onclick = function () {
                fetchMethod("/products/" + productId, null, "delete")
                    .then(res => {
                        if (!VALID_STATUS.includes(res.status)) {
                            return showToastError(res.message)
                        }
                        return window.location.reload();
                    })
            }
        }

        ButtonEdit.onclick = function () {
            AskRemove.classList.remove("activeFlex");
            SetReadOnly(false, ListInput);
            PopoverRoleProduct(false);
            ButtonDone.onclick = function () {
                let id = idInput.value
                let name = nameInput.value
                let price = parseFloat(priceInput.value.replaceAll(',', ''))
                let unit = unitInput.value
                let imageUrl = imgInput.value || null
                let quantityRemain = parseFloat(quantityInput.value.replaceAll(',', ''))
                let idCategory = parseInt(cateSpan.getAttribute('id')) || null
                let isRestore = false;
                if (name === '' || price === '' || unit === '' || quantityRemain === '')
                    return showToastError("Id, Name, Price, Unit, Quantity shouldn't be empty")

                if (radioTrue.checked) isRestore = true;

                let data = {
                    id, name, price,
                    unit, imageUrl, quantityRemain, idCategory, isRestore
                }

                fetchMethod("/products/" + productId, data, "put")
                    .then(res => {
                        if (!VALID_STATUS.includes(res.status)) {
                            return showToastError(res.message)
                        }
                        return window.location.reload();
                    })
            }

        }
    } else {
        idInput.value = ''
        nameInput.value = ''
        priceInput.value = ''
        unitInput.value = ''
        imgInput.value = ''
        quantityInput.value = ''
        cateSpan.innerText = ''
        cateSpan.removeAttribute('id')

        statusForm.style.display = 'none'

        Title.textContent = "Thêm hàng hóa";
        TitleAction.classList.remove("activeFlex");
        SetReadOnly(false, ListInput);
        PopoverRoleProduct(false);

        ButtonDone.onclick = function () {
            let id = idInput.value
            let name = nameInput.value
            let price = parseFloat(priceInput.value.replaceAll(',', ''))
            let unit = unitInput.value
            let imageUrl = imgInput.value || null
            let quantityRemain = parseFloat(quantityInput.value.replaceAll(',', ''))
            let idCategory = parseInt(cateSpan.getAttribute('id')) || null

            if (name === '' || price === '' || unit === '' || quantityRemain === '')
                return showToastError("Id, Name, Price, Unit, Quantity shouldn't be empty")

            let data = {
                id, name, price,
                unit, imageUrl, quantityRemain, idCategory
            }
            fetchMethod("/products", data, "post")
                .then(res => {
                    if (!VALID_STATUS.includes(res.status)) {
                        return showToastError(res.message)
                    }
                    return window.location.reload();
                })
        }
    }

    PopoverAddProduct.classList.add("activeFlex");
    ButtonSkip.onclick = function () {
        AskRemove.classList.remove("activeFlex");
        PopoverAddProduct.classList.remove("activeFlex");
        for (let i = 0; i < ListInputAll.length; i++) {
            ListInputAll[i].value = "";
        }
    }
}

function PopoverRoleProduct(status) {
    let InputRole = document.querySelector("#type-product");
    let PopoverRole = document.querySelector("#popover-type-product");
    let RoleSpan = InputRole.querySelector("span");
    if (status === true) {
        InputRole.onclick = function () {
        };
    } else {
        InputRole.onclick = function () {
            PopoverRole.classList.toggle("activeFlex");
            window.onmouseup = function (event) {
                if (PopoverRole.contains(event.target)) {
                    RoleSpan.textContent = event.target.textContent;
                    RoleSpan.setAttribute('id', event.target.getAttribute('category-id'))
                    PopoverRole.classList.remove("activeFlex");
                } else {
                    PopoverRole.classList.remove("activeFlex");
                }
            }
        }
    }
}

function TypeProduct(IDTypeProduct = null) {
    let PopoverAddTypeProduct = document.getElementById("add-type-product");
    let ListInput = PopoverAddTypeProduct.querySelectorAll("input:not([readonly])");
    PopoverAddTypeProduct.classList.toggle("activeFlex");
    let ButtonList = PopoverAddTypeProduct.querySelector(".button-list");
    let ButtonDone = ButtonList.querySelector(".btn-done");
    let ButtonSkip = ButtonList.querySelector(".btn-skip");
    let AskRemove = PopoverAddTypeProduct.querySelector(".AskRemove");
    let Title = PopoverAddTypeProduct.querySelector(".title p");
    let TitleAction = PopoverAddTypeProduct.querySelector(".title-action");

    let idInput = document.getElementById('IdTypeProduct');
    let nameInput = document.getElementById('NameTypeProduct');

    if (IDTypeProduct) {
        // Load form
        Title.textContent = "Thông tin loại hàng hóa";
        TitleAction.classList.add("activeFlex");
        SetReadOnly(true, ListInput);

        let ButtonRemove = TitleAction.querySelector(".btn-remove");
        let ButtonEdit = TitleAction.querySelector(".btn-edit");
        let curElement = document.querySelector(`#list-product-category input[id="cate${IDTypeProduct}"]`)
        let label = curElement.nextElementSibling.innerText;

        idInput.value = IDTypeProduct;
        nameInput.value = label;

        ButtonRemove.onclick = function () {
            AskRemove.classList.add("activeFlex");
            SetReadOnly(true, ListInput);
            ButtonDone.onclick = function () {
                fetchMethod("/categories/" + IDTypeProduct, null, "delete")
                    .then(res => {
                        if (!VALID_STATUS.includes(res.status)) {
                            return showToastError(res.message)
                        }
                        return window.location.reload();
                    })
            }
        }

        ButtonEdit.onclick = function () {
            AskRemove.classList.remove("activeFlex");
            SetReadOnly(false, ListInput);
            ButtonDone.onclick = function () {
                let name = nameInput.value;
                if (name === '') return showToastError("Name shouldn't be empty")

                fetchMethod("/categories/" + IDTypeProduct, {name}, "put")
                    .then(res => {
                        if (!VALID_STATUS.includes(res.status)) {
                            return showToastError(res.message)
                        }
                            return window.location.reload();
                    })
            }

        }
    } else {
        Title.textContent = "Thêm loại hàng hóa";
        TitleAction.classList.remove("activeFlex");
        SetReadOnly(false, ListInput);
        idInput.value = ''
        nameInput.value = ''
        ButtonDone.onclick = function () {

            let name = nameInput.value;
            if (name === '') return showToastError("Name shouldn't be empty")


            fetchMethod("/categories", {name}, "post")
                .then(res => {
                    if (!VALID_STATUS.includes(res.status)) {
                        return showToastError(res.message)
                    }

                    let listCategories = document.getElementById('list-product-category');
                    let addProductFormListCate = document.querySelector('#popover-type-product ul')
                    let liTag = document.createElement('li');

                    liTag.innerHTML = `
                     <input type="radio" name="product-category" id="cate${res.detail.id}" onchange="getProductRadioCheck(this)">
                     <label for="cate${res.detail.id}">${res.detail.name}</label>
                     <i class="fas fa-edit"></i>
                    `

                    let formLiTag = document.createElement('li')
                    formLiTag.setAttribute('category-id', res.detail.id)
                    formLiTag.innerText = res.detail.name

                    addProductFormListCate.appendChild(formLiTag)
                    listCategories.appendChild(liTag)
                    PopoverAddTypeProduct.classList.remove("activeFlex");
                    ProductSideBarClick();
                })
        }
    }


    PopoverAddTypeProduct.classList.add("activeFlex");
    ButtonSkip.onclick = function () {
        AskRemove.classList.remove("activeFlex");
        PopoverAddTypeProduct.classList.remove("activeFlex");
        nameInput.innerText = '';
    }
}

ProductRowClick();

function ProductRowClick() {
    let ListRow = document.querySelectorAll(".tab-product .list-row .row");
    for (let i = 0; i < ListRow.length; i++) {
        ListRow[i].onclick = function () {
            let IDProduct = parseInt(this.children[0].parentElement.getAttribute('product-id'))
            AddProduct(IDProduct);
        }
    }
}

function getProductRadioCheck() {
    let productStatusCheckedType = "StatusProductAll";
    let productCategoryChecked = "TypeProductAll";

    let productStatusRadios = document.querySelectorAll('input[name="product-status"]')
    productStatusRadios.forEach(item => {
        if (item.checked)
            productStatusCheckedType = item.getAttribute("id")
    })

    let productTypeRadios = document.querySelectorAll('input[name="product-category"]')
    productTypeRadios.forEach(item => {
        if (item.checked)
            productCategoryChecked = item.getAttribute("id").split('cate')[1]
    })

    switch (productStatusCheckedType) {
        case 'StatusProductAll': {
            let products = ALL_PRODUCTS

            let parsedId = parseInt(productCategoryChecked, 10)
            if (!isNaN(parsedId) && parsedId >= 1)
                products = ALL_PRODUCTS.filter(product => product.category?.id === parsedId)
            showProducts(products)
            break
        }
        case 'StatusProductWork': {
            let products = ALL_PRODUCTS.filter(product => product.deletedAt === null)

            let parsedId = parseInt(productCategoryChecked, 10)
            if (!isNaN(parsedId) && parsedId >= 1)
                products = ALL_PRODUCTS.filter(product => (product.category?.id === parsedId && product.deletedAt === null))

            showProducts(products)
            break
        }
        case 'StatusProductNotWork' : {
            let products = ALL_PRODUCTS.filter(product => product.deletedAt !== null)

            let parsedId = parseInt(productCategoryChecked, 10)
            if (!isNaN(parsedId) && parsedId >= 1)
                products = ALL_PRODUCTS.filter(product => (product.category?.id === parsedId && product.deletedAt !== null))
            showProducts(products)
            break
        }
    }
}

ProductSideBarClick();

function ProductSideBarClick() {
    let ListTypeProduct = document.querySelector("#TypeProduct");
    let ListButtonEdit = ListTypeProduct.querySelectorAll("li i");
    let ButtonAddType = ListTypeProduct.querySelector(".btn-add-type");
    for (let i = 0; i < ListButtonEdit.length; i++) {
        ListButtonEdit[i].onclick = function (event) {
            let cateId = parseInt(event.target.parentNode.querySelector('input').getAttribute('id').split('cate')[1])
            TypeProduct(cateId);
        }
    }
    ButtonAddType.onclick = function () {
        TypeProduct();
    }
}

function showProducts(products) {
    let listProducts = document.getElementById('list-products')
    listProducts.innerHTML = '';

    products.forEach(product => {
        let rowDiv = document.createElement('div')
        rowDiv.setAttribute('class', 'row')
        rowDiv.setAttribute('product-id', product.id)

        rowDiv.innerHTML = `
            <div class="col-2">SP${product.id}</div>
            <div class="col-2">${product.name}</div>
            <div class="col-2">${product.category ? product.category.name : '' }</div>
            <div class="col-2">${numberWithCommas(product.price)}</div>
          <div class="col-2">${numberWithCommas(product.quantityRemain)}</div>
        `
        listProducts.appendChild(rowDiv)
    })
    ProductRowClick()
}
