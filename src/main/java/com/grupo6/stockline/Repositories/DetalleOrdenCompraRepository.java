package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.DetalleOrdenCompra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleOrdenCompraRepository extends BaseRepository<DetalleOrdenCompra, Long>{

    @Query("SELECT d FROM DetalleOrdenCompra d WHERE d.articulo.id = :idArticulo")
    List<DetalleOrdenCompra> obtenerDetallesPorArticulo(@Param("idArticulo") Long idArticulo);

}
