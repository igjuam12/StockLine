package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Enum.ModeloInventario;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloRepository extends BaseRepository<Articulo,Long> {
    List<Articulo> findByFechaBajaIsNullAndModeloInventario(ModeloInventario modeloInventario);
}
