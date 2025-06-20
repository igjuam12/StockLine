package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Articulo;

import java.util.List;

public interface ArticuloService extends BaseService<Articulo, Long>{

    void bajaArticulo(Long id) throws Exception;
    List<Articulo> listarArticulosReponer() throws Exception;
    List<Articulo> listarArticulosFaltantes() throws Exception;
    void asignarProveedorPredeterminado(Articulo articulo, Long idProveedor) throws Exception;
    Double calcularCGI(Long id) throws Exception;
    void realizarAjuste(Long id, Integer cantidadAjuste) throws Exception;
    void calcularModeloInventario(Long id) throws Exception;

}