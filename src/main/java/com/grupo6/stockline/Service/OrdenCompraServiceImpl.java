package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.OrdenCompra;
import com.grupo6.stockline.Repositories.BaseRepository;
import com.grupo6.stockline.Repositories.OrdenCompraRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdenCompraServiceImpl extends BaseServiceImpl<OrdenCompra, Long> implements OrdenCompraService{
    @Autowired
    OrdenCompraRepository ordenCompraRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public OrdenCompraServiceImpl(BaseRepository<OrdenCompra, Long> baseRepository,
                                  OrdenCompraRepository ordenCompraRepository) {
        super(baseRepository);
    }
}
