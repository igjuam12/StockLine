package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Enum.ModeloInventario;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloRepository extends BaseRepository<Articulo,Long> {

    boolean existsByProveedorPredeterminadoId(Long proveedorId);

    List<Articulo> findByFechaBajaIsNullAndModeloInventario(ModeloInventario modeloInventario);

}
