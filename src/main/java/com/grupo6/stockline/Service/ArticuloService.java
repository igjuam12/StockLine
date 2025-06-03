package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Articulo;

import java.util.List;

public interface ArticuloService extends BaseService<Articulo, Long>{

    public List<Articulo> buscarArticulosPorProveedor(Long id);

}
