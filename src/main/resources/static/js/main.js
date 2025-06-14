// --- Lógica del menú móvil (existente) ---
document.addEventListener('DOMContentLoaded', function () {
    const menuButton = document.getElementById('mobile-menu-button');
    const mobileMenu = document.getElementById('mobile-menu');

    if (menuButton && mobileMenu) {
        menuButton.addEventListener('click', () => {
            mobileMenu.classList.toggle('hidden');
        });
    }

    // --- Lógica de mensajes flash (AÑADIDA) ---
    // Llamamos a la función que se encarga de los mensajes
    initFlashMessageTimer();
});


/**
 * Busca mensajes flash y los oculta después de 5 segundos con un efecto de desvanecimiento.
 * Esta función es llamada cuando el DOM está listo.
 */
function initFlashMessageTimer() {
    const flashMessages = document.querySelectorAll('.flash-message');

    flashMessages.forEach(function(message) {
        setTimeout(function() {
            message.classList.add('fade-out');
            setTimeout(function() {
                message.style.display = 'none';
            }, 500); // Coincide con la duración de la transición CSS
        }, 5000); // 5 segundos de visibilidad
    });
}


// --- Lógica del modal de baja (existente) ---
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