package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Base;
import com.grupo6.stockline.Repositories.BaseRepository;
import jakarta.transaction.Transactional;

import java.util.List;

public abstract class BaseServiceImpl<E extends Base, ID extends Long> implements BaseService<E, ID> {

    protected BaseRepository<E, ID> baseRepository;

    public BaseServiceImpl(BaseRepository<E, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }

    public List<E> findAll() throws Exception {
        try {
            return baseRepository.findAll();
        } catch (Exception e) {
            throw new Exception("Error al obtener los datos: " + e.getMessage(), e);
        }
    }

    public List<E> findAllActive() throws Exception {
        try {
            return baseRepository.findAllActive();
        } catch (Exception e) {
            throw new Exception("Error al obtener los datos: " + e.getMessage(), e);
        }
    }

    @Transactional
    public E findById(ID id) throws Exception {
        try {
            return baseRepository.findById(id)
                    .orElseThrow(() -> new Exception("Entidad no encontrada con ID: " + id));
        } catch (Exception e) {
            throw new Exception("Error al buscar por ID: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void save(E entity) throws Exception {
        try {
            baseRepository.save(entity);
        } catch (Exception e) {
            throw new Exception("Error al guardar la entidad: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void update(ID id, E entity) throws Exception {
        try {
            if (!baseRepository.existsById(id)) {
                throw new Exception("No se puede actualizar: entidad no encontrada con ID: " + id);
            }
            entity.setId(id);
            baseRepository.save(entity);
        } catch (Exception e) {
            throw new Exception("Error al actualizar la entidad: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void delete(ID id) throws Exception {
        try {
            if (baseRepository.existsById(id)) {
                baseRepository.darDeBajaPorId(id);
            } else {
                throw new Exception("Entidad no encontrada con ID: " + id);
            }
        } catch (Exception e) {
            throw new Exception("Error al eliminar la entidad: " + e.getMessage(), e);
        }
    }
}
