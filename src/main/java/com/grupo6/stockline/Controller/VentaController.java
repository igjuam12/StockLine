package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.DetalleVenta;
import com.grupo6.stockline.Entities.Venta;
import com.grupo6.stockline.Service.ArticuloService;
import com.grupo6.stockline.Service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("venta")
public class VentaController {

    private final VentaService ventaService;
    private final ArticuloService articuloService;

    @GetMapping("/listado")
    public String listarVentas(Model model) throws Exception {
        List<Venta> ventas = ventaService.findAll();
        model.addAttribute("listaVentas", ventas);
        model.addAttribute("contenido", "ventas/index :: contenido");
        return "layouts/base";
    }

    @GetMapping("/crear")
    public String mostrarFormularioVenta(Model model) throws Exception {
        Venta venta = new Venta();
        venta.getDetalleVenta().add(new DetalleVenta());
        List<Articulo> listaArticulos = articuloService.findAll();

        model.addAttribute("venta", venta);
        model.addAttribute("listaArticulos", listaArticulos);
        model.addAttribute("contenido", "ventas/formVenta :: contenido");

        return "layouts/base";
    }

    @PostMapping("/crear")
    public String procesarVenta(@ModelAttribute Venta venta, RedirectAttributes redirectAttributes) {
        try {
            ventaService.crearVenta(venta);
            redirectAttributes.addFlashAttribute("exito", "Venta registrada correctamente.");
            return "redirect:/venta/listado";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar la venta: " + e.getMessage());
            return "redirect:/venta/crear";
        }
    }

    @GetMapping("/{id}/detalle")
    public String verDetalleVenta(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) throws Exception {
        Venta venta = ventaService.findById(id);
        model.addAttribute("venta", venta);
        model.addAttribute("contenido", "ventas/detalleVenta :: contenido");
        return "layouts/base";
    }

}
