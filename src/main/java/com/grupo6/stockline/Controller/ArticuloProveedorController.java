package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.ArticuloProveedor;
import com.grupo6.stockline.Entities.Proveedor;
import com.grupo6.stockline.Service.ArticuloProveedorService;
import com.grupo6.stockline.Service.ArticuloService;
import com.grupo6.stockline.Service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("articulo-proveedor")
public class ArticuloProveedorController {

    private final ArticuloProveedorService articuloProveedorService;
    private final ProveedorService proveedorService;
    private final ArticuloService articuloService;

    @GetMapping("/agregar")
    public String mostrarFormularioCrearParaProveedor(@RequestParam("proveedorId") Long proveedorId, Model model) throws Exception {
        Proveedor proveedor = proveedorService.findById(proveedorId);
        ArticuloProveedor asociacion = new ArticuloProveedor();
        asociacion.setProveedor(proveedor);
        model.addAttribute("asociacion", asociacion);
        model.addAttribute("listaArticulos", articuloService.findAll());
        model.addAttribute("isEditMode", false);
        model.addAttribute("contenido", "proveedores/formArticuloProveedor :: contenido");
        return "layouts/base";
    }

    @PostMapping("/agregar")
    public String crearAsociacion(@ModelAttribute("asociacion") ArticuloProveedor asociacion,
                                  RedirectAttributes redirectAttributes) throws Exception {
        try {
            articuloProveedorService.save(asociacion);
            redirectAttributes.addFlashAttribute("exito", "Artículo agregado correctamente.");
            return "redirect:/proveedor/" + asociacion.getProveedor().getId() + "/articulos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar el artículo: " + e.getMessage());
            redirectAttributes.addAttribute("proveedorId", asociacion.getProveedor().getId());
            return "redirect:/articulo-proveedor/agregar";
        }
    }
    @GetMapping("/{id}/modificar")
    public String mostrarFormularioModificar(@PathVariable("id") Long id, Model model) throws Exception {
        ArticuloProveedor asociacion = articuloProveedorService.findById(id);
        model.addAttribute("asociacion", asociacion);
        model.addAttribute("isEditMode", true);
        model.addAttribute("contenido", "proveedores/formArticuloProveedor :: contenido");
        return "layouts/base";
    }

    @PostMapping("/{id}/modificar")
    public String modificarAsociacion(@PathVariable("id") Long id,
                                      @ModelAttribute("asociacion") ArticuloProveedor asociacionForm,
                                      @RequestParam("proveedorId") Long proveedorId,
                                      RedirectAttributes redirectAttributes) {
        try {
            articuloProveedorService.update(id, asociacionForm);
            redirectAttributes.addFlashAttribute("exito", "Asociación modificada correctamente.");
            return "redirect:/proveedor/" + proveedorId + "/articulos";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al modificar la asociación: " + e.getMessage());
            return "redirect:/articulo-proveedor/" + id + "/modificar";
        }
    }

    @PostMapping("{id}/baja")
    public String darDeBajaArticuloProveedor(@RequestParam("proveedorId") Long proveedorId, @PathVariable Long id, RedirectAttributes redirectAttributes) throws Exception {
        try {
            articuloProveedorService.delete(id);
            redirectAttributes.addFlashAttribute("exito", "Artículo desasociado correctamente.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/proveedor/" + proveedorId + "/articulos";
    }

}
