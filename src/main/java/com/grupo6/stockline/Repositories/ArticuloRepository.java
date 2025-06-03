package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.Articulo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloRepository extends BaseRepository<Articulo,Long> {

    List<Articulo> findByProveedorPredeterminadoId(Long proveedorId);

}
