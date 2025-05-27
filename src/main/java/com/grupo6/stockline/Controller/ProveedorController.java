package com.grupo6.stockline.Controller;

import com.grupo6.stockline.Entities.Proveedor;
import com.grupo6.stockline.Service.ProveedorServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "proveedor")
public class ProveedorController extends BaseControllerImpl<Proveedor,
        ProveedorServiceImpl>{
}
