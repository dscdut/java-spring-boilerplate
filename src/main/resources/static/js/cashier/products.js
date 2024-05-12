let productByCategory = {};
let allProducts;

function fetchProducts() {
    fetchMethod("/products/active")
        .then(productData => {
            allProducts = productData;
            productData.forEach(product => {
                let { category, ...rest } = product;
                if (category) {
                    if (!productByCategory[category.id])
                        productByCategory[category.id] = [];
                    productByCategory[category.id].push(rest);
                }
            })
            showProducts(productData);
        })
    let allCategories = document.querySelectorAll("div#product_categories span");
    for (let i = 0; i < allCategories.length; i++) {
        if (i === 0)
            allCategories[i].addEventListener("click", event => {
                showProducts(allProducts)
            });
        else allCategories[i].addEventListener("click", event => {
            let categoryId = (event.target).getAttribute("category-id");
            showProducts(productByCategory[categoryId]);
        });
    }
}

function showProducts(productData) {
    const listProducts = document.getElementById("list-menu");
    listProducts.innerHTML = '';

    if(!productData || !productData.length) return;
    productData.forEach(product => {
        let productDiv = document.createElement("div");
        productDiv.setAttribute("class", "menu-wrap");
        productDiv.setAttribute("product-id", product.id);
        productDiv.innerHTML =
            `
                <div class="menu-image">
                    <img src="${product.imageUrl}" referrerpolicy="no-referrer"  class="img" alt="${product.name}">
                </div>
                <span class="product-name">${product.name}</span>
                <span class="product-price">${numberWithCommas(product.price)}</span>
            `
        productDiv.addEventListener('click', addProductToBill);
        listProducts.appendChild(productDiv);
    });
}

async function addProductToBill(event) {
    let parentEle = event.target.closest('.menu-wrap')
    let tableOrderId = parseInt(document.getElementById('droplist-label-show').getAttribute("bill-id"), 10)
    if (!tableOrderId) return showToastInfor("Choose Table first");
    let productId = parseInt(parentEle.getAttribute("product-id"));
    let tableId = parseInt(document.getElementById('bill-board').getAttribute('table-id'), 10)

    let data = {
        productId,
        tableOrderId,
    }
    fetchMethod("/tableOrders/items", data, "post")
        .then(async res => {
            if (!VALID_STATUS.includes(res.status)) {
                return showToastError(res.message)
            }

            fetchTables();
            await fetchTableOrders();
            const tableBills = allBills.filter(bill => bill.idTable === tableId);
            showBillItems(tableBills, null, tableOrderId);
        })
}

let searchBar = document.getElementById('search-bar');
let searchList = document.getElementById('search-list');
searchBar.addEventListener('keyup', event => {
    let inputText = event.target.value;

    if (inputText.length === 0) { searchList.innerHTML = ``; }
    else {
        let pattern = new RegExp(`.*${inputText}.*`, 'i');
        let foundProducts = allProducts.filter(item => item.name.match(pattern));
        searchList.innerHTML = ``;
        for (const foundProduct of foundProducts) {
            searchList.innerHTML += `
            <li class="header_search-product-item" product-id="${foundProduct.id}" id="found-product">
                <div class="item-img">
                    <img src="${foundProduct.imageUrl}" width="90px" height="90px" alt="${foundProduct.name}">
                </div>
                <div class="item-info">
                    <div class="info-col">
                        <p class="info-name">${foundProduct.name}</p>
                        <p class="info-id">${foundProduct.id}</p>
                    </div>
                    <div class="info-col">
                        <br>
                        <p>Giá: <span class="info-price">${numberWithCommas(foundProduct.price)}</span></p>
                    </div>
                </div>
            </li>
            `
        }

        let item = searchList.querySelectorAll('.header_search-product-item')
        for (const itemElement of item) {
            itemElement.addEventListener('click', event => {
                let productId;
                productId = parseInt(event.target.getAttribute('product-id'), 10);
                if (!productId)
                    productId = parseInt(event.target.closest('#found-product').getAttribute('product-id'), 10)

                let tableOrderId = parseInt(document.getElementById('droplist-label-show').getAttribute("bill-id"), 10)
                let tableId = parseInt(document.getElementById('bill-board').getAttribute('table-id'), 10)

                if (!productId || !tableOrderId) return;
                let data = {
                    productId,
                    tableOrderId,
                }

                fetchMethod("/tableOrders/items", data, "post")
                    .then(async res => {
                        if (!VALID_STATUS.includes(res.status)) {
                            return showToastError(res.message)
                        }

                        fetchTables();
                        await fetchTableOrders();
                        const tableBills = allBills.filter(bill => bill.idTable === tableId);
                        showBillItems(tableBills, null, tableOrderId);

                        let searchProducts = document.getElementById('search-products');
                        searchProducts.style.display = 'none';
                    })
            })
        }
    }
})

searchBar.addEventListener('focus', () => {
    let searchProducts = document.getElementById('search-products');
    searchProducts.style.display = 'block';
})

document.addEventListener('click', event => {
    let fatherDiv = document.getElementById('search-wrap');
    let searchProducts = document.getElementById('search-products');
    if (!fatherDiv.contains(event.target)) {
        searchProducts.style.display = 'none';
    }
})