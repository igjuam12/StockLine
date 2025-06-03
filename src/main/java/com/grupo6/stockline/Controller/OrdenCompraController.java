package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.OrdenCompra;
import com.grupo6.stockline.Service.OrdenCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("ordenCompra")
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;

    @GetMapping("/listado")
    public String listarOrdenes(Model model) throws Exception {
        List<OrdenCompra> ordenes = ordenCompraService.findAll();
        model.addAttribute("listaOrdenes", ordenes);
        model.addAttribute("contenido", "compras/index :: contenido");
        return "layouts/base";
    }

}
