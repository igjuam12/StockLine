package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Service.ArticuloService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("articulo")
public class ArticuloController {

    private final ArticuloService articuloService;

    @GetMapping("/listado")
    public String listarArticulos(Model model) throws Exception {
        List<Articulo> articulos = articuloService.findAll();
        model.addAttribute("listaArticulos", articulos);
        model.addAttribute("contenido", "articulos/index :: contenido");
        return "layouts/base";
    }

}
