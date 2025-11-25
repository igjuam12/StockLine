package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.DetalleOrdenCompra;
import com.grupo6.stockline.Entities.OrdenCompra;
import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import com.grupo6.stockline.Service.ArticuloService;
import com.grupo6.stockline.Service.OrdenCompraService;
import com.grupo6.stockline.Service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ordenCompra")
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;
    private final ArticuloService articuloService; // Inyectado para poblar el formulario
    private final ProveedorService proveedorService; // Inyectado para poblar el formulario

    @GetMapping("/listado")
    public String listarOrdenesCompra(Model model) throws Exception {
        List<OrdenCompra> ordenes = ordenCompraService.findAll();
        model.addAttribute("listaOrdenes", ordenes);
        model.addAttribute("estados", EstadoOrdenCompra.values());
        model.addAttribute("listaArticulos", articuloService.findAll());
        model.addAttribute("titulo", "Listado de Órdenes de Compra");
        model.addAttribute("contenido", "compras/index :: contenido");
        return "layouts/base";
    }

    @GetMapping("/crear")
    public String mostrarFormularioNuevaOrdenCompra(Model model) throws Exception {
        OrdenCompra ordenCompra = new OrdenCompra();
        List<DetalleOrdenCompra> detalles = new ArrayList<>();
        DetalleOrdenCompra detalleInicial = new DetalleOrdenCompra();
        detalles.add(detalleInicial);
        ordenCompra.setDetalleOrdenCompra(detalles);

        model.addAttribute("ordenCompra", ordenCompra);
        model.addAttribute("listaArticulos", articuloService.findAllActive());
        model.addAttribute("listaProveedores", proveedorService.findAllActive());
        model.addAttribute("titulo", "Crear Orden de Compra");
        model.addAttribute("isEditMode", false);
        model.addAttribute("contenido", "compras/formOrdenCompra :: contenido");
        return "layouts/base";
    }

    @PostMapping("/crear")
    public String procesarNuevaOrdenCompra(@ModelAttribute OrdenCompra ordenCompra, RedirectAttributes redirectAttributes) {
        try {
            Long articuloIdParaValidacion = null;

            if (ordenCompra.getDetalleOrdenCompra() != null && !ordenCompra.getDetalleOrdenCompra().isEmpty() &&
                    ordenCompra.getDetalleOrdenCompra().get(0).getArticulo() != null &&
                    ordenCompra.getDetalleOrdenCompra().get(0).getArticulo().getId() != null) {
                articuloIdParaValidacion = ordenCompra.getDetalleOrdenCompra().get(0).getArticulo().getId();
            } else {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar un artículo válido para la orden de compra.");
                return "redirect:/ordenCompra/crear";
            }
            if(ordenCompra.getDetalleOrdenCompra() != null){
                for(DetalleOrdenCompra detalle : ordenCompra.getDetalleOrdenCompra()){
                    detalle.setOrdenCompra(ordenCompra);
                }
            }

            ordenCompraService.crearOrdenCompra(ordenCompra, articuloIdParaValidacion);
            redirectAttributes.addFlashAttribute("exito", "Orden de Compra creada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/ordenCompra/crear";
        }
        return "redirect:/ordenCompra/listado";
    }

    @GetMapping("/{id}/modificar")
    public String mostrarFormularioModificarOrdenCompra(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            OrdenCompra ordenCompra = ordenCompraService.findById(id);
            if (ordenCompra.getEstadoOrdenCompra() != EstadoOrdenCompra.PENDIENTE) {
                redirectAttributes.addFlashAttribute("error", "Solo se pueden modificar órdenes en estado PENDIENTE.");
                return "redirect:/ordenCompra/listado";
            }
            if (ordenCompra.getDetalleOrdenCompra() == null || ordenCompra.getDetalleOrdenCompra().isEmpty()) {
                List<DetalleOrdenCompra> detalles = new ArrayList<>();
                DetalleOrdenCompra detalleVacio = new DetalleOrdenCompra();
                detalles.add(detalleVacio);
                ordenCompra.setDetalleOrdenCompra(detalles);
            }

            model.addAttribute("ordenCompra", ordenCompra);
            model.addAttribute("listaProveedores", proveedorService.findAllActive());
            model.addAttribute("listaArticulos", articuloService.findAllActive()); // Para mostrar info o si se permite cambiar
            model.addAttribute("titulo", "Modificar Orden de Compra");
            model.addAttribute("isEditMode", true);
            model.addAttribute("contenido", "compras/formOrdenCompra :: contenido");
            return "layouts/base";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/ordenCompra/listado";
        }
    }

    @PostMapping("/{id}/modificar")
    public String procesarModificarOrdenCompra(@PathVariable Long id, @ModelAttribute OrdenCompra ordenCompra, RedirectAttributes redirectAttributes) {
        try {
            Long proveedorId = (ordenCompra.getProveedor() != null) ? ordenCompra.getProveedor().getId() : null;
            Integer cantidad = null;
            if (ordenCompra.getDetalleOrdenCompra() != null && !ordenCompra.getDetalleOrdenCompra().isEmpty()) {
                cantidad = ordenCompra.getDetalleOrdenCompra().get(0).getCantidad();
            }

            ordenCompraService.modificarOrdenCompra(id, proveedorId, cantidad);
            redirectAttributes.addFlashAttribute("exito", "Orden de Compra modificada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/ordenCompra/" + id + "/modificar";
        }
        return "redirect:/ordenCompra/listado";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelarOrden(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ordenCompraService.cancelarOrdenCompra(id);
            redirectAttributes.addFlashAttribute("exito", "Orden de Compra cancelada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ordenCompra/listado";
    }

    @PostMapping("/{id}/enviar")
    public String enviarOrden(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ordenCompraService.enviarOrdenCompra(id);
            redirectAttributes.addFlashAttribute("exito", "Orden de Compra enviada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ordenCompra/listado";
    }

    @PostMapping("/{id}/finalizar")
    public String finalizarOrden(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ordenCompraService.finalizarOrdenCompra(id);
            redirectAttributes.addFlashAttribute("exito", "Orden de Compra finalizada exitosamente. Stock actualizado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ordenCompra/listado";
    }

    @GetMapping("/filtro")
    public String filtrarOrdenes(@RequestParam(value = "estado", required = false) EstadoOrdenCompra estado,
                                 @RequestParam(value = "articuloId", required = false) Long articuloId,
                                 Model model) throws Exception {
        List<OrdenCompra> ordenes = ordenCompraService.findOrdenCompraByEstadoAndArticulo(estado, articuloId);
        model.addAttribute("listaOrdenes", ordenes);
        model.addAttribute("estados", EstadoOrdenCompra.values());
        model.addAttribute("listaArticulos", articuloService.findAllActive());
        model.addAttribute("titulo", "Órdenes de Compra Filtradas");
        model.addAttribute("contenido", "compras/index :: contenido");
        return "layouts/base";
    }
}


