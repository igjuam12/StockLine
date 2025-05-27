package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.DetalleOrdenCompra;
import com.grupo6.stockline.Repositories.BaseRepository;
import com.grupo6.stockline.Repositories.DetalleOrdenCompraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleOrdenCompraServiceImpl extends BaseServiceImpl<DetalleOrdenCompra, Long> implements DetalleOrdenCompraService {
    @Autowired
    DetalleOrdenCompraRepository detalleOrdenCompraRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public DetalleOrdenCompraServiceImpl(BaseRepository<DetalleOrdenCompra, Long> baseRepository,
                                         DetalleOrdenCompraRepository detalleOrdenCompraRepository) {
        super(baseRepository);
    }
}
