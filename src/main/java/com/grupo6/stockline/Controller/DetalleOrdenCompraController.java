package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.DetalleOrdenCompra;
import com.grupo6.stockline.Service.DetalleOrdenCompraServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "detalleOrdenCompra")
public class DetalleOrdenCompraController extends BaseControllerImpl<DetalleOrdenCompra,
        DetalleOrdenCompraServiceImpl>{
}
