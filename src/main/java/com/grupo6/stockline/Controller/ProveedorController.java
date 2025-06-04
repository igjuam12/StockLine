package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.ArticuloProveedor;
import com.grupo6.stockline.Entities.Proveedor;
import com.grupo6.stockline.Service.ArticuloProveedorService;
import com.grupo6.stockline.Service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("proveedor")
public class ProveedorController {

    private final ProveedorService proveedorService;
    private final ArticuloProveedorService articuloProveedorService;

    @GetMapping("/listado")
    public String listarProveedores(Model model) throws Exception {
        List<Proveedor> proveedores = proveedorService.findAll();
        model.addAttribute("listaProveedores", proveedores);
        model.addAttribute("contenido", "proveedores/index :: contenido");
        return "layouts/base";
    }

    @GetMapping("/{id}/articulos")
    public String listarArticulosPorProveedor(Model model, @PathVariable Long id) throws Exception {
        Proveedor proveedor = proveedorService.findById(id);
        List<ArticuloProveedor> articulosProveedor = articuloProveedorService.obtenerArticulosPorProveedor(id);
        model.addAttribute("proveedor", proveedor);
        model.addAttribute("articulosProveedor", articulosProveedor);
        model.addAttribute("contenido", "proveedores/listadoArticulos :: contenido");
        return "layouts/base";
    }

    @PostMapping("{id}/baja")
    public String darDeBajaProveedor(@PathVariable Long id, RedirectAttributes redirectAttributes) throws Exception {
        try {
            proveedorService.delete(id);
            redirectAttributes.addFlashAttribute("exito", "Proveedor dado de baja correctamente.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/proveedor/listado";
    }

}
