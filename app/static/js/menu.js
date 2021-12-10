let btn = document.getElementById("bars")
let menu = document.getElementById("menu-mobile")
let body = document.querySelector("body")
menu.style.left = "-100%"

btn.addEventListener("click", () => {
    if (menu.style.left == "-100%") {
        window.scrollTo(0,0)
        body.style.overflow = "hidden"
        menu.style.left = "-7%"
    } else {
        body.style.overflow = "scroll"
        menu.style.left = "-100%"
    }
})