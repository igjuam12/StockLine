package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.DatosModeloInventario;
import com.grupo6.stockline.Service.DatosModeloInventarioServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "datosModeloInventario")
public class DatosModeloInventarioController extends BaseControllerImpl<DatosModeloInventario,
        DatosModeloInventarioServiceImpl>{
}
