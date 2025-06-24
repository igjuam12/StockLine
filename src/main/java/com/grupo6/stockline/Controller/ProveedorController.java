package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.Articulo;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("proveedor")
public class ProveedorController {

    private final ProveedorService proveedorService;
    private final ArticuloProveedorService articuloProveedorService;
    private final ArticuloService articuloService;

    @GetMapping("/crear")
    public String mostrarFormularioProveedor(Model model) throws Exception {
        List<Articulo> articulos = articuloService.findAll();
        Proveedor proveedor = new Proveedor();
        List<ArticuloProveedor> apList = new ArrayList<>();
        apList.add(new ArticuloProveedor());
        proveedor.setArticuloProveedor(apList);
        model.addAttribute("proveedor", proveedor) ;
        model.addAttribute("listaArticulos", articulos);
        model.addAttribute("isEditMode", false);
        model.addAttribute("contenido", "proveedores/formProveedor :: contenido");
        return "layouts/base";
    }

    @PostMapping("/crear")
    public String procesarProveedor(@ModelAttribute Proveedor proveedor, RedirectAttributes redirectAttributes) throws Exception {
        try {
            proveedorService.save(proveedor);
            redirectAttributes.addFlashAttribute("exito", "Proveedor creado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/proveedor/listado";
    }

    @GetMapping("/{id}/modificar")
    public String mostrarFormularioModificar(@PathVariable("id") Long id, Model model) throws Exception {
        Proveedor proveedor = proveedorService.findById(id);
        model.addAttribute("proveedor", proveedor);
        model.addAttribute("isEditMode", true);
        model.addAttribute("contenido", "proveedores/formProveedor :: contenido");
        return "layouts/base";
    }

    @PostMapping("/{id}/modificar")
    public String modificarProveedor(@PathVariable Long id, @ModelAttribute Proveedor proveedor, RedirectAttributes redirectAttributes) throws Exception {
        try {
            proveedorService.update(id, proveedor);
            redirectAttributes.addFlashAttribute("exito", "Proveedor modificado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/proveedor/listado";
    }

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
