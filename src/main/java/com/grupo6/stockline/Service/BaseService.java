package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Base;

import java.io.Serializable;
import java.util.List;

public interface BaseService<E extends Base, ID extends Serializable>{

    public List<E> findAll() throws Exception;
    public E findById(ID id) throws Exception;
    public void save(E entity) throws Exception;
    public void update(ID id, E entity) throws Exception;
    public boolean delete(ID id) throws Exception;

}
