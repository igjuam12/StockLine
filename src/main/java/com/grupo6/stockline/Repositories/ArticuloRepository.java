package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.DTOs.DTOArticulo;
import com.grupo6.stockline.Entities.Articulo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloRepository extends BaseRepository<Articulo,Long> {

    @Query("SELECT a FROM Articulo a " +
            "JOIN a.datosModeloInventario dmi " +
            "LEFT JOIN a.proveedorPredeterminado p " +
            "ORDER BY a.id")
    List<Articulo> listarArticulos();

    @Query("SELECT a.id, a.nombreArticulo, a.stockActual, dmi.inventarioMaximo, dmi.loteOptimo, dmi.puntoPedido, dmi.stockSeguridad, p.nombreProveedor " +
            "FROM Articulo a " +
            "JOIN a.datosModeloInventario dmi " +
            "LEFT JOIN a.proveedorPredeterminado p " +
            "WHERE a.id = :articuloId AND a.fechaBaja IS NULL " +
            "ORDER BY a.id")
    DTOArticulo listarArticuloById(@Param("articuloId") Long articuloId);

}
