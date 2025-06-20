package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.ArticuloProveedor;

import java.util.List;

public interface ArticuloProveedorService extends BaseService<ArticuloProveedor, Long>{

    List<ArticuloProveedor> obtenerArticulosPorProveedor(Long proveedorId);
    List<ArticuloProveedor> obtenerProveedoresPorArticulo(Long articuloId);

}
