package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.Articulo;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloRepository extends BaseRepository<Articulo,Long> {

    boolean existsByProveedorPredeterminadoId(Long proveedorId);

}
