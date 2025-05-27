package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.OrdenCompra;
import com.grupo6.stockline.Service.OrdenCompraServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "ordenCompra")
public class OrdenCompraController extends BaseControllerImpl<OrdenCompra,
        OrdenCompraServiceImpl>{
}
