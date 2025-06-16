package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.OrdenCompra;
import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenCompraRepository extends BaseRepository<OrdenCompra, Long> {


    // Verificar si existe una Orden de Compra activa (Pendiente o Enviada) para un artículo específico
    @Query("SELECT CASE WHEN COUNT(ordenCompra) > 0 THEN true ELSE false END FROM OrdenCompra ordenCompra " +
            "JOIN ordenCompra.detalleOrdenCompra detalleOrdenCompra " +
            "WHERE detalleOrdenCompra.articulo.id = :articuloId " +
            "AND (ordenCompra.estadoOrdenCompra = :estadoPendiente OR ordenCompra.estadoOrdenCompra = :estadoEnviada) " +
            "AND ordenCompra.fechaBaja IS NULL")
    boolean existeOrdenCompraActivaArticulo(
            @Param("articuloId") Long articuloId,
            @Param("estadoPendiente") EstadoOrdenCompra estadoPendiente,
            @Param("estadoEnviada") EstadoOrdenCompra estadoEnviada
    );

// Encontrar Ordenes de Compra por estado o artículo
    @Query("SELECT DISTINCT oc FROM OrdenCompra oc " +
            "LEFT JOIN oc.detalleOrdenCompra doc " +
            "WHERE (:estado IS NULL OR oc.estadoOrdenCompra = :estado) " +
            "AND (:articuloId IS NULL OR doc.articulo.id = :articuloId) " +
            "AND (oc.fechaBaja IS NULL " +
            "     OR oc.estadoOrdenCompra = com.grupo6.stockline.Enum.EstadoOrdenCompra.CANCELADA)")
    List<OrdenCompra> findOrdenCompraByEstadoAndArticulo(
            @Param("estado") EstadoOrdenCompra estado,
            @Param("articuloId") Long articuloId
    );
}