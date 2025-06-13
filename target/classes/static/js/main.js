// Solo para el toggle del menú móvil
document.addEventListener('DOMContentLoaded', function () {
    const menuButton = document.getElementById('mobile-menu-button');
    const mobileMenu = document.getElementById('mobile-menu');

    if (menuButton && mobileMenu) {
        menuButton.addEventListener('click', () => {
            mobileMenu.classList.toggle('hidden');
        });
    }
});

function abrirModal(elemento, event) {
    event.preventDefault();
    const id = elemento.getAttribute("data-id");
    const path = elemento.getAttribute("data-path");
    const parametroExtra = elemento.getAttribute("data-parametro");
    let actionUrl = `/${path}/${id}/baja`;
    if (parametroExtra) {
        actionUrl += `?${parametroExtra}`;
    }
    const form = document.getElementById("formBaja");
    form.action = actionUrl;
    document.getElementById("modalBaja").classList.remove("hidden");
}

function cerrarModal() {
    document.getElementById("modalBaja").classList.add("hidden");
}

