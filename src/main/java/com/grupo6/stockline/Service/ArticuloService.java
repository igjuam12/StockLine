package com.grupo6.stockline.Service;

import com.grupo6.stockline.DTOs.DTOArticulo;
import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.Proveedor;

import java.util.List;

public interface ArticuloService extends BaseService<Articulo, Long>{

    public void bajaArticulo(Long id) throws Exception;
    public List<Articulo> listarArticulosReponer() throws Exception;
    public List<Articulo> listarArticulosFaltantes() throws Exception;
    public void asignarProveedorPredeterminado(Articulo articulo, Long idProveedor) throws Exception;
    public Double calcularCGI(Long id) throws Exception;
    public void realizarAjuste(Long id, Integer cantidadAjuste) throws Exception;
    public void calcularModeloInventario(Long id) throws Exception;

}
