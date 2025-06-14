package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.ArticuloProveedor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloProveedorRepository extends BaseRepository<ArticuloProveedor,Long>{

    List<ArticuloProveedor> findByProveedorId(Long proveedorId);

    long countByArticuloIdAndFechaBajaIsNull(Long articuloId);

}
