package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.DatosModeloInventario;
import com.grupo6.stockline.Repositories.BaseRepository;
import com.grupo6.stockline.Repositories.DatosModeloInventarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatosModeloInventarioServiceImpl extends BaseServiceImpl<DatosModeloInventario, Long> implements DatosModeloInventarioService{
    @Autowired
    DatosModeloInventarioRepository datosModeloInventarioRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public DatosModeloInventarioServiceImpl(BaseRepository<DatosModeloInventario, Long> baseRepository,
                                            DatosModeloInventarioRepository datosModeloInventarioRepository) {
        super(baseRepository);
    }
}
