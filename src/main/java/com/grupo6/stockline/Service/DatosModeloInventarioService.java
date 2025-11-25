package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.DatosModeloInventario;

public interface DatosModeloInventarioService extends BaseService<DatosModeloInventario, Long>{

    DatosModeloInventario obtenerDatosModeloInventarioActivo(Articulo articulo);
}
