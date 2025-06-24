package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Entities.ArticuloProveedor;
import com.grupo6.stockline.Entities.Proveedor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloProveedorRepository extends BaseRepository<ArticuloProveedor,Long>{

    List<ArticuloProveedor> findByProveedorId(Long proveedorId);

    long countByArticuloIdAndFechaBajaIsNull(Long articuloId);

    @Query("SELECT ap FROM ArticuloProveedor ap WHERE ap.proveedor.id = :proveedorId AND ap.articulo.id = :articuloId")
    ArticuloProveedor findByProveedorAndArticulo(@Param("proveedorId") Long proveedorId, @Param("articuloId") Long articuloId);

    List<ArticuloProveedor> findByArticuloId(Long id);

    boolean existsByProveedorAndArticuloAndFechaBajaIsNull(Proveedor proveedor, Articulo articulo);

}
