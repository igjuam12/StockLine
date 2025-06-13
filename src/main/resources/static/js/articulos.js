document.addEventListener('DOMContentLoaded', function () {
    const container = document.getElementById('modelos-inventario-container');
    const addButton = document.getElementById('agregar-modelo-btn');

    if (!container || !addButton) {
        console.warn("El contenedor de modelos de inventario o el botón de agregar no fueron encontrados.");
        return;
    }

    let modeloIndex = container.children.length;

    const labelClasses = 'block text-sm font-medium text-gray-700 mb-1';
    const inputBaseClasses = 'block w-full px-3 py-2 bg-white border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm text-gray-900';
    const selectClasses = `${inputBaseClasses}`;
    const inputClasses = `${inputBaseClasses} placeholder-gray-400`;
    const quitarBtnClasses = 'bg-red-100 hover:bg-red-200 text-red-600 font-medium py-1 px-2 rounded-md shadow-sm text-xs transition-colors duration-150 ease-in-out quitar-modelo-btn';
    const modeloBlockClasses = 'mt-4 p-4 border border-gray-200 rounded-lg bg-gray-50 modelo-inventario-block';

    addButton.addEventListener('click', function () {
        const lastBlock = container.querySelector('.modelo-inventario-block:last-of-type');

        if (!lastBlock) {
            console.error("No se encontró un bloque base de modelo de inventario para clonar.");
            return;
        }

        const newBlock = lastBlock.cloneNode(true);
        newBlock.className = modeloBlockClasses;
        newBlock.setAttribute('data-index', modeloIndex);

        newBlock.querySelectorAll('[id], [name], [for]').forEach(el => {
            if (el.id) el.id = el.id.replace(/\d+/, modeloIndex);
            if (el.name) el.name = el.name.replace(/\[\d+\]/, `[${modeloIndex}]`);
            if (el.htmlFor) el.htmlFor = el.htmlFor.replace(/\d+/, modeloIndex);
        });

        newBlock.querySelectorAll('label').forEach(label => {
            label.className = labelClasses;
        });

        newBlock.querySelectorAll('input, select').forEach(input => {
            if (input.tagName === 'SELECT') {
                input.className = selectClasses;
                input.selectedIndex = 0;
            } else {
                input.className = inputClasses;
                input.value = '';
            }
        });

        let quitarBtn = newBlock.querySelector('.quitar-modelo-btn');
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
            quitarBtn.className = quitarBtnClasses;
        }

        container.appendChild(newBlock);
        modeloIndex++;
    });

    container.addEventListener('click', function (event) {
        const quitarButton = event.target.closest('.quitar-modelo-btn');
        if (!quitarButton) return;

        const blocks = container.querySelectorAll('.modelo-inventario-block');
        if (blocks.length <= 1) return;

        quitarButton.closest('.modelo-inventario-block').remove();

        const updatedBlocks = container.querySelectorAll('.modelo-inventario-block');
        updatedBlocks.forEach((block, i) => {
            block.setAttribute('data-index', i);
            block.querySelectorAll('[id], [name], [for]').forEach(el => {
                if (el.id) el.id = el.id.replace(/\d+/, i);
                if (el.name) el.name = el.name.replace(/\[\d+\]/, `[${i}]`);
                if (el.htmlFor) el.htmlFor = el.htmlFor.replace(/\d+/, i);
            });

            const currentQuitarBtn = block.querySelector('.quitar-modelo-btn');
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

        modeloIndex = updatedBlocks.length;
    });
});
