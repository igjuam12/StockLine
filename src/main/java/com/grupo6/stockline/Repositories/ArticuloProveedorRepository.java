package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.ArticuloProveedor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloProveedorRepository extends BaseRepository<ArticuloProveedor,Long>{

    List<ArticuloProveedor> findByArticuloId(Long id);
}
