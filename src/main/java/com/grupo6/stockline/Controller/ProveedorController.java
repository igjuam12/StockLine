package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.Proveedor;
import com.grupo6.stockline.Service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("proveedor")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping("/listado")
    public String listarProveedores(Model model) throws Exception {
        List<Proveedor> proveedores = proveedorService.findAll();
        model.addAttribute("listaProveedores", proveedores);
        model.addAttribute("contenido", "proveedores/index :: contenido");
        return "layouts/base";
    }


}
