package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.DetalleVenta;
import com.grupo6.stockline.Repositories.BaseRepository;
import com.grupo6.stockline.Repositories.DetalleVentaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleVentaServiceImpl extends BaseServiceImpl<DetalleVenta, Long> implements DetalleVentaService{
    @Autowired
    DetalleVentaRepository detalleVentaRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public DetalleVentaServiceImpl(BaseRepository<DetalleVenta, Long> baseRepository,
                                   DetalleVentaRepository detalleVentaRepository) {
        super(baseRepository);
    }
}

