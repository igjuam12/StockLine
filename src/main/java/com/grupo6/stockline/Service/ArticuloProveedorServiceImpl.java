package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.ArticuloProveedor;
import com.grupo6.stockline.Repositories.ArticuloProveedorRepository;
import com.grupo6.stockline.Repositories.BaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloProveedorServiceImpl extends BaseServiceImpl<ArticuloProveedor, Long> implements ArticuloProveedorService{
    @Autowired
    ArticuloProveedorRepository articuloProveedorRepository;
    @PersistenceContext
    private EntityManager entityManager;
    public ArticuloProveedorServiceImpl(BaseRepository<ArticuloProveedor, Long> baseRepository,
                                        ArticuloProveedorRepository articuloProveedorRepository) {
        super(baseRepository);
    }

    @Override
    public List<ArticuloProveedor> obtenerProveedoresPorArticulo(Long articuloId) {
        return articuloProveedorRepository.findByArticuloId(articuloId);
    }
}