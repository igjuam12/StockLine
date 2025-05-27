package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.DetalleVenta;
import com.grupo6.stockline.Service.DetalleVentaServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "detalleVenta")
public class DetalleVentaContoller extends BaseControllerImpl<DetalleVenta,
        DetalleVentaServiceImpl>{
}
