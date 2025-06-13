package com.grupo6.stockline.Controller;
import com.grupo6.stockline.DTOs.DTOArticulo;
import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.ArticuloProveedor;
import com.grupo6.stockline.Entities.DatosModeloInventario;
import com.grupo6.stockline.Entities.Proveedor;
import com.grupo6.stockline.Enum.ModeloInventario;
import com.grupo6.stockline.Service.ArticuloProveedorService;
import com.grupo6.stockline.Service.ArticuloService;
import com.grupo6.stockline.Service.ArticuloServiceImpl;
import com.grupo6.stockline.Service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("articulo")
public class ArticuloController{

    private final ArticuloService articuloService;
    private final ProveedorService proveedorService;
    private final ArticuloProveedorService articuloProveedorService;

    @GetMapping("/crear")
    public String mostrarFormularioArticulo(Model model) throws Exception{
        Articulo articulo = new Articulo();
        List<DatosModeloInventario> dmiList = new ArrayList<>();
        dmiList.add(new DatosModeloInventario());
        articulo.setDatosModeloInventario(dmiList);
        model.addAttribute("modelosInventario",
                Arrays.asList(ModeloInventario.values()));
        model.addAttribute("articulo", articulo);
        model.addAttribute("contenido", "articulos/formArticulo :: contenido");
        model.addAttribute("isEditMode", false);


        return "layouts/base";
    }

    @PostMapping("/crear")
    public String crearArticulo(@ModelAttribute Articulo articulo) throws Exception{
        try {
            articulo.asociarDatosModeloArticulo();
            articuloService.save(articulo);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return "redirect:/articulo/listado";
    }

    @GetMapping("/{id}/modificar")
    public String mostrarFormularioArticulo(Model model,@PathVariable Long id) throws Exception{
        try {
            Articulo articulo = articuloService.findById(id);
            model.addAttribute("articulo", articulo);
            model.addAttribute("modelosInventario",
                    Arrays.asList(ModeloInventario.values()));
            model.addAttribute("isEditMode", true);
            model.addAttribute("contenido", "articulos/formArticulo :: contenido");

        }catch (Exception e){

        }
        return "layouts/base";
    }

    @PostMapping("/{id}/modificar")
    public String modficarArticulo(@PathVariable Long id, @ModelAttribute Articulo articulo) throws Exception{
        try {
            Articulo articuloExistente = articuloService.findById(id);
            articulo.setProveedorPredeterminado(articuloExistente.getProveedorPredeterminado());

            articuloService.update(id, articulo);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return "redirect:/articulo/listado";
    }

    @PostMapping("/{id}/baja")
    public String bajaArticulo(@PathVariable Long id, RedirectAttributes redirectAttributes) throws Exception{
        try {
            articuloService.bajaArticulo(id);
            redirectAttributes.addFlashAttribute("exito", "Articulo dado de baja correctamente.");
        }catch (IllegalStateException  e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/articulo/listado";
    }

    @GetMapping("/listado")
    public String listarArticulos(Model model) throws Exception {
        List<Articulo> articulos = articuloService.findAll();
        model.addAttribute("articulos", articulos);
        model.addAttribute("contenido", "articulos/index :: contenido");
        return "layouts/base";
    }

    @GetMapping("/listadoReponer")
    public String listarArticulosReponer(Model model) throws Exception {
        List<Articulo> articulosReponer = articuloService.listarArticulosReponer();
        model.addAttribute("articulos", articulosReponer);
        model.addAttribute("contenido", "articulos/index :: contenido");
        return "layouts/base";
    }

    @GetMapping("/listadoFaltantes")
    public String listarArticulosFaltantes(Model model) throws Exception {
        List<Articulo> articulosFaltantes  = articuloService.listarArticulosFaltantes();
        model.addAttribute("articulos", articulosFaltantes);
        model.addAttribute("contenido", "articulos/index :: contenido");
        return "layouts/base";
    }

    @GetMapping("/{id}/proveedores")
    public String listarProveedoresPorArticulo(Model model, @PathVariable Long id) throws Exception {
        Articulo articulo = articuloService.findById(id);
        List<ArticuloProveedor> articuloProveedores = articuloProveedorService.obtenerProveedoresPorArticulo(id);
        model.addAttribute("articulo", articulo);
        model.addAttribute("articuloProveedores", articuloProveedores);
        model.addAttribute("contenido", "articulos/listadoProveedores :: contenido");
        return "layouts/base";
    }

    @PostMapping("/{idArticulo}/asignar-proveedor")
    public String asignarProveedorPredeterminado(@PathVariable Long idArticulo,
                                                 @RequestParam Long idProveedor,
                                                 RedirectAttributes redirectAttributes) {
        try {
            Articulo articulo = articuloService.findById(idArticulo);
            articuloService.asignarProveedorPredeterminado(articulo, idProveedor);
            redirectAttributes.addFlashAttribute("success", "Proveedor asignado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al asignar proveedor: " + e.getMessage());
        }
        return "redirect:/articulo/" + idArticulo + "/proveedores";
    }

}
