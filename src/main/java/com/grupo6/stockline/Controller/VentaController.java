package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.Venta;
import com.grupo6.stockline.Service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("venta")
public class VentaController {

    private final VentaService ventaService;

    @GetMapping("/listado")
    public String listarVentas(Model model) throws Exception {
        List<Venta> ventas = ventaService.findAll();
        model.addAttribute("listaVentas", ventas);
        model.addAttribute("contenido", "ventas/index :: contenido");
        return "layouts/base";
    }

}
