package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.OrdenCompra;
import com.grupo6.stockline.Enum.EstadoOrdenCompra;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenCompraRepository extends BaseRepository<OrdenCompra, Long>{

    boolean existsByProveedorIdAndEstadoOrdenCompraIn(Long proveedorId, List<EstadoOrdenCompra> estados);

}
