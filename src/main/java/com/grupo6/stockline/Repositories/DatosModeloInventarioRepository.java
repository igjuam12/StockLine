package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.DatosModeloInventario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DatosModeloInventarioRepository extends BaseRepository<DatosModeloInventario, Long>{
    Optional<DatosModeloInventario> findByArticulo(Articulo articulo);
}
