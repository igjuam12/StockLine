package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Venta;
import com.grupo6.stockline.Repositories.BaseRepository;
import com.grupo6.stockline.Repositories.VentaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VentaServiceImpl extends BaseServiceImpl<Venta, Long> implements VentaService {
    @Autowired
    VentaRepository ventaRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public VentaServiceImpl(BaseRepository<Venta, Long> baseRepository,
                            VentaRepository ventaRepository) {
        super(baseRepository);
    }
}
