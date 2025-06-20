document.addEventListener('DOMContentLoaded', function () {
    // Lógica para el menú móvil (existente)
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
    initFlashMessageTimer();
    initVentaForm();
});

function initFlashMessageTimer() {
    const flashMessages = document.querySelectorAll('.flash-message');
    flashMessages.forEach(function(message) {
        setTimeout(function() {
            message.classList.add('fade-out');
            setTimeout(function() {
                message.style.display = 'none';
            }, 500);
        }, 5000);
    });
}

function initVentaForm() {
    const container = document.getElementById('detalles-venta-container');
    const addButton = document.getElementById('agregar-detalle-btn');
    const form = document.getElementById('venta-form');

    if (!container || !addButton || !form) {
        return;
    }

    addButton.addEventListener('click', () => {
        const detalleBlocks = container.querySelectorAll('.detalle-venta-block');
        const newIndex = detalleBlocks.length;

        const clone = detalleBlocks[0].cloneNode(true);

        clone.querySelector('select').selectedIndex = 0;
        clone.querySelector('input[type="number"]').value = '';

        clone.querySelectorAll('[id], [name], [for]').forEach(el => {
            if(el.id) el.id = el.id.replace(/-\d+$/, `-${newIndex}`);
            if(el.htmlFor) el.htmlFor = el.htmlFor.replace(/-\d+$/, `-${newIndex}`);
            if(el.name) el.name = el.name.replace(/\[\d+\]/, `[${newIndex}]`);
        });

        clone.querySelector('h3').textContent = `Artículo ${newIndex + 1}`;

        let removeButton = clone.querySelector('.quitar-detalle-btn');
        if (!removeButton) {
            const header = clone.querySelector('.flex.justify-between');
            removeButton = document.createElement('button');
            removeButton.type = 'button';
            removeButton.className = 'text-red-500 hover:text-red-700 quitar-detalle-btn';
            removeButton.innerHTML = '<i class="fas fa-trash-alt"></i> Quitar';
            header.appendChild(removeButton);
        }
        removeButton.style.display = 'inline-block';

        container.appendChild(clone);
        updateRemoveButtonsState();
    });

    container.addEventListener('click', (event) => {
        const removeBtn = event.target.closest('.quitar-detalle-btn');
        if (removeBtn) {
            removeBtn.closest('.detalle-venta-block').remove();
            updateRemoveButtonsState();
        }
    });

    function updateRemoveButtonsState() {
        const detalleBlocks = container.querySelectorAll('.detalle-venta-block');
        detalleBlocks.forEach((block) => {
            const removeButton = block.querySelector('.quitar-detalle-btn');
            if (removeButton) {
                removeButton.style.display = (detalleBlocks.length > 1) ? 'inline-block' : 'none';
            }
        });
    }

    updateRemoveButtonsState();
}

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