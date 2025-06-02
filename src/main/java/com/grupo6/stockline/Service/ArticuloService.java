package com.grupo6.stockline.Service;

import com.grupo6.stockline.DTOs.DTOArticulo;
import com.grupo6.stockline.Entities.Articulo;

import java.util.List;

public interface ArticuloService extends BaseService<Articulo, Long>{

    public void bajaArticulo(Long id) throws Exception;
    public List<DTOArticulo> listarArticulos() throws Exception;
    public DTOArticulo listarArticuloById(Long id) throws Exception;
    public List<DTOArticulo> listarArticulosReponer() throws Exception;
    public List<DTOArticulo> listarArticulosFaltantes() throws Exception;
}
