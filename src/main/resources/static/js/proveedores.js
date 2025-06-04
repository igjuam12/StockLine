document.addEventListener('DOMContentLoaded', function () {
    const container = document.getElementById('articulos-asociados-container');
    const addButton = document.getElementById('agregar-articulo-btn');

    if (!container || !addButton) {
        console.warn("El contenedor de artículos asociados o el botón de agregar no fueron encontrados.");
        return;
    }

    let articleIndex = container.children.length;

    // Definición de clases de Tailwind para reutilizar en elementos clonados
    const labelClasses = 'block text-sm font-medium text-gray-700 mb-1';
    const inputBaseClasses = 'block w-full px-3 py-2 bg-white border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm text-gray-900';
    const inputPlaceholderClasses = 'placeholder-gray-400'; // Para inputs con placeholder
    const selectClasses = `${inputBaseClasses} articulo-select`; // 'articulo-select' es una clase marcadora si la necesitas en JS
    const inputClasses = `${inputBaseClasses} ${inputPlaceholderClasses}`;
    const quitarBtnClasses = 'bg-red-100 hover:bg-red-200 text-red-600 font-medium py-1 px-2 rounded-md shadow-sm text-xs transition-colors duration-150 ease-in-out quitar-articulo-btn';
    const articuloBlockClasses = 'mt-4 p-4 border border-gray-200 rounded-lg bg-gray-50 articulo-asociado-block';


    addButton.addEventListener('click', function () {
        const lastBlock = container.querySelector('.articulo-asociado-block:last-of-type');

        if (!lastBlock) {
            console.error("No se encontró un bloque de artículo base para clonar.");
            return;
        }

        const newBlock = lastBlock.cloneNode(true);
        newBlock.className = articuloBlockClasses; // Aplicar clases al div principal del bloque clonado
        newBlock.setAttribute('data-index', articleIndex);

        newBlock.querySelectorAll('[id], [name], [for]').forEach(el => {
            if (el.id) {
                el.id = el.id.replace(/\d+$/, articleIndex);
            }
            if (el.name) {
                el.name = el.name.replace(/\[\d+\]/, `[${articleIndex}]`);
            }
            if (el.htmlFor) {
                el.htmlFor = el.htmlFor.replace(/\d+$/, articleIndex);
            }
        });

        // Re-aplicar clases a los elementos internos del bloque clonado y limpiar valores
        newBlock.querySelectorAll('label').forEach(label => {
            label.className = labelClasses;
        });

        newBlock.querySelectorAll('input, select').forEach(input => {
            if (input.tagName === 'SELECT') {
                input.className = selectClasses;
                if (input.options.length > 0) {
                    input.selectedIndex = 0;
                }
            } else {
                input.className = inputClasses; // Para inputs de tipo number, text, email, etc.
                if (input.type === 'checkbox' || input.type === 'radio') {
                    input.checked = false;
                } else if (input.type !== 'hidden') {
                    input.value = '';
                }
            }
        });

        const titulo = newBlock.querySelector('h3');
        if (titulo) {
            titulo.textContent = `Artículo ${articleIndex + 1}`;
        }

        let quitarBtn = newBlock.querySelector('.quitar-articulo-btn');
        if (!quitarBtn) {
            const flexContainer = newBlock.querySelector('.flex.justify-between.items-center');
            if (flexContainer) {
                const btn = document.createElement('button');
                btn.type = "button";
                btn.className = quitarBtnClasses;
                btn.innerHTML = '<i class="fas fa-times mr-1"></i>Quitar';
                flexContainer.appendChild(btn);
            }
        } else {
            // Asegurar que el botón clonado también tenga las clases correctas si ya existía
            quitarBtn.className = quitarBtnClasses;
        }

        container.appendChild(newBlock);
        articleIndex++;
    });

    container.addEventListener('click', function (event) {
        const quitarButton = event.target.closest('.quitar-articulo-btn');
        if (!quitarButton) return;

        const blocks = container.querySelectorAll('.articulo-asociado-block');
        if (blocks.length <= 1) {
            return;
        }

        quitarButton.closest('.articulo-asociado-block').remove();

        const updatedBlocks = container.querySelectorAll('.articulo-asociado-block');
        updatedBlocks.forEach((block, i) => {
            block.setAttribute('data-index', i);
            const titulo = block.querySelector('h3');
            if (titulo) {
                titulo.textContent = `Artículo ${i + 1}`;
            }

            block.querySelectorAll('[id], [name], [for]').forEach(el => {
                if (el.id) {
                    el.id = el.id.replace(/\d+$/, i);
                }
                if (el.name) {
                    el.name = el.name.replace(/\[\d+\]/, `[${i}]`);
                }
                if (el.htmlFor) {
                    el.htmlFor = el.htmlFor.replace(/\d+$/, i);
                }
            });

            const currentQuitarBtn = block.querySelector('.quitar-articulo-btn');
            if (currentQuitarBtn && i === 0) {
                currentQuitarBtn.remove();
            } else if (!currentQuitarBtn && i > 0) {
                const flexContainer = block.querySelector('.flex.justify-between.items-center');
                if (flexContainer) {
                    const btn = document.createElement('button');
                    btn.type = "button";
                    btn.className = quitarBtnClasses;
                    btn.innerHTML = '<i class="fas fa-times mr-1"></i>Quitar';
                    flexContainer.appendChild(btn);
                }
            }
        });

        articleIndex = updatedBlocks.length;
    });
});
