package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Proveedor;
import com.grupo6.stockline.Repositories.BaseRepository;
import com.grupo6.stockline.Repositories.ProveedorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveedorServiceImpl extends BaseServiceImpl<Proveedor, Long> implements ProveedorService{
    @Autowired
    ProveedorRepository proveedorRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public ProveedorServiceImpl(BaseRepository<Proveedor, Long> baseRepository,
                                ProveedorRepository proveedorRepository) {
        super(baseRepository);
    }
}
