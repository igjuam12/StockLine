// Solo para el toggle del menú móvil
document.addEventListener('DOMContentLoaded', function () {
    const menuButton = document.getElementById('mobile-menu-button');
    const mobileMenu = document.getElementById('mobile-menu');

    if (menuButton && mobileMenu) {
        menuButton.addEventListener('click', () => {
            mobileMenu.classList.toggle('hidden');
        });
    }

    const articuloSelect = document.getElementById('articulo');
    const proveedorSelect = document.getElementById('proveedor');
    const cantidadInput = document.getElementById('cantidad');

    if (articuloSelect && proveedorSelect) {
        const updateFields = () => {
            const selectedOption = articuloSelect.options[articuloSelect.selectedIndex];
            const proveedorId = selectedOption ? selectedOption.getAttribute('data-proveedor') : null;
            if (proveedorId) {
                proveedorSelect.value = proveedorId;
            }

            if (cantidadInput && selectedOption) {
                const stock = parseInt(selectedOption.getAttribute('data-stock'));
                const lote = parseInt(selectedOption.getAttribute('data-lote'));
                if (!cantidadInput.value) {
                    const sugerido = lote - stock;
                    cantidadInput.value = sugerido > 0 ? sugerido : 0;
                }
            }
        };

        articuloSelect.addEventListener('change', () => {
            if (cantidadInput) {
                cantidadInput.value = '';
            }
            updateFields();
        });
        // Inicializar al cargar la página
        updateFields();
    }
});

function abrirModal(elemento, event) {
    event.preventDefault();
    const id = elemento.getAttribute("data-id");
    const path = elemento.getAttribute("data-path");
    const form = document.getElementById("formBaja");
    form.action = `/${path}/${id}/baja`;
    console.log("Form action:", form.action);
    document.getElementById("modalBaja").classList.remove("hidden");
}

function cerrarModal() {
    document.getElementById("modalBaja").classList.add("hidden");
}


