package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Articulo;
import com.grupo6.stockline.Repositories.ArticuloRepository;
import com.grupo6.stockline.Repositories.BaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo, Long> implements ArticuloService{
    @Autowired
    ArticuloRepository articuloRepository;
    @PersistenceContext
    private EntityManager entityManager;
    public ArticuloServiceImpl(BaseRepository<Articulo, Long> baseRepository,
                               ArticuloRepository articuloRepository){
        super(baseRepository);
    }

}
