SideBarEvent();

function SideBarEvent() {
    let ListItem = document.getElementsByClassName("side-bar-item ");

    let searchBar = document.querySelector('.header-search');
    for (let i = 0; i < ListItem.length; i++) {
        ListItem[i].onclick = function () {

            let NameItem = this.querySelector("p").textContent;
            let NameHeader = document.querySelector("#main-content-header");
            NameHeader.textContent = NameItem;

            let nameTab = this.getAttribute('name_tab')
            if(nameTab === 'tab-dashboard' || nameTab === 'tab-deal')
                searchBar.style.visibility = 'hidden'
            else searchBar.style.visibility = 'visible'

            document.querySelector(".active-item").classList.remove("active-item");
            this.classList.add("active-item");

            let TabFlex = document.querySelector(".section.activeFlex");
            TabFlex.classList.remove("activeFlex");
            let Name_tab = this.getAttribute("Name_Tab").toString();
            let TabThis = document.getElementsByClassName(`${Name_tab}`);
            TabThis[0].classList.toggle("activeFlex");

        }
    }
}

// Nav bar : Button list bar
function ListBarPopover() {
    let ListBar = document.querySelector(".list-bar-popover ");
    ListBar.classList.add("activeFlex");
    window.onmouseup = function (event) {
        if (!ListBar.contains(event.target)) {
            ListBar.classList.remove("activeFlex");
        }
    }
}



