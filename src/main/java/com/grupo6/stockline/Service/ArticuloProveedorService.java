package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.ArticuloProveedor;

import java.util.List;

public interface ArticuloProveedorService extends BaseService<ArticuloProveedor, Long>{

    public List<ArticuloProveedor> obtenerArticulosPorProveedor(Long proveedorId);

}
