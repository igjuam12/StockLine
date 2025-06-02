package com.grupo6.stockline.Controller;
import com.grupo6.stockline.DTOs.DTOArticulo;
import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.Proveedor;
import com.grupo6.stockline.Enum.ModeloInventario;
import com.grupo6.stockline.Service.ArticuloService;
import com.grupo6.stockline.Service.ArticuloServiceImpl;
import com.grupo6.stockline.Service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("articulo")
public class ArticuloController{

    private final ArticuloService articuloService;
    private final ProveedorService proveedorService;

    @GetMapping("/crear")
    public String mostrarFormularioArticulo(Model model) throws Exception{
        try {
            List<Proveedor> proveedores = proveedorService.findAll();
            model.addAttribute("listaProveedores", proveedores);
            model.addAttribute("modelosInventario",
                    Arrays.asList(ModeloInventario.values()));
        }catch (Exception e){

        }
        return "articulos/formArticulo";
    }

    @PostMapping("/crear")
    public String crearArticulo(@ModelAttribute Articulo articulo) throws Exception{
        try {
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
            List<Proveedor> proveedores = proveedorService.findAll();
            model.addAttribute("listaProveedores", proveedores);
            model.addAttribute("modelosInventario",
                    Arrays.asList(ModeloInventario.values()));
        }catch (Exception e){

        }
        return "articulos/formArticulo";
    }

    @PostMapping("/{id}/modificar")
    public String modficarArticulo(@PathVariable Long id, @ModelAttribute Articulo articulo) throws Exception{
        try {
            articuloService.update(id, articulo);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return "redirect:/articulo/listado";
    }

    @PostMapping("/baja/{id}")
    public String bajaArticulo(@PathVariable Long id) throws Exception{
        try {
            articuloService.bajaArticulo(id);
        }catch (Exception e){
            return e.getMessage();
        }
        return "redirect:/articulo/listarArticulo";
    }

    @GetMapping("/listado")
    public String listarArticulos(Model model) throws Exception {
        List<DTOArticulo> articulos = articuloService.listarArticulos();
        model.addAttribute("articulos", articulos);
        return "listarArticulos";
    }

    @GetMapping("/filtrarArticuloId/{id}")
    public String listarArticuloById(Model model, @PathVariable Long id) throws Exception {
        DTOArticulo articulo = articuloService.listarArticuloById(id);
        model.addAttribute("articulo", articulo);
        return "listarArticulo";
    }

    @GetMapping("/listadoReponer")
    public String listarArticulosReponer(Model model) throws Exception {
        List<DTOArticulo> articulosReponer = articuloService.listarArticulosReponer();
        model.addAttribute("articulosReponer", articulosReponer);
        return "listarArticulosReponer";
    }

    @GetMapping("/listadoFaltantes")
    public String listarArticulosFaltantes(Model model) throws Exception {
        List<DTOArticulo> articulosFaltantes  = articuloService.listarArticulosFaltantes();
        model.addAttribute("articulosFaltantes", articulosFaltantes);
        return "listarArticulosFaltantes";
    }

}
