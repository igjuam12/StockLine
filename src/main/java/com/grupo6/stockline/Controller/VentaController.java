package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.Venta;
import com.grupo6.stockline.Service.VentaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "venta")
public class VentaController extends BaseControllerImpl<Venta, VentaServiceImpl>{
}
