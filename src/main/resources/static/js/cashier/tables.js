let tableByType = {};
let allTables;

function fetchTables() {
    fetchMethod("/tables/active")
        .then(data => {
            allTables = data;
            data.forEach(table => {
                let {tableType, ...rest} = table;
                if (!tableByType[tableType.id])
                    tableByType[tableType.id] = [];
                tableByType[tableType.id].push(rest);
            })
            showTables(data);
        })

    let allCategories = document.querySelectorAll("div#table_types span");
    for (let i = 0; i < allCategories.length; i++) {
        if (i === 0)
            allCategories[i].addEventListener("click", event => {
                showTables(allTables)
            });
        else allCategories[i].addEventListener("click", event => {
            let typeId = (event.target).getAttribute("type-id");
            showTables(tableByType[typeId]);
        });
    }

}

function showTables(data) {
    const listTable = document.getElementById("list-table");
    listTable.innerHTML = '';

    if(!data || !data.length) return;
    data.forEach(table => {
        let tableDiv = document.createElement("div");
        tableDiv.setAttribute("class", "table-wrap");
        tableDiv.setAttribute("table-id", table.id);
        let logoDiv = document.createElement('div');
        let logoDivClass = "table-wrap-logo";
        if (table.status)
            logoDivClass += " table-busy";

        logoDiv.setAttribute("class", logoDivClass);

        let pTag = document.createElement("p");
        pTag.textContent = table.name;

        tableDiv.appendChild(logoDiv)
        tableDiv.appendChild(pTag)
        listTable.appendChild(tableDiv);

        tableDiv.addEventListener("click", showBill);
    });
}