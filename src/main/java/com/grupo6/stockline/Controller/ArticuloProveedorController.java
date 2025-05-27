package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.ArticuloProveedor;
import com.grupo6.stockline.Service.ArticuloProveedorServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "articuloProveedor")
public class ArticuloProveedorController extends BaseControllerImpl<ArticuloProveedor,
        ArticuloProveedorServiceImpl>{
}
