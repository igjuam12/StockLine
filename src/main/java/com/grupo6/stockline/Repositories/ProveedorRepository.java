package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.Proveedor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProveedorRepository extends BaseRepository<Proveedor, Long> {

}
