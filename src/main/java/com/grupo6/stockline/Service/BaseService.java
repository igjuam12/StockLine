package com.grupo6.stockline.Service;

import com.grupo6.stockline.Entities.Base;

import java.io.Serializable;
import java.util.List;

public interface BaseService<E extends Base, ID extends Long>{

     List<E> findAll() throws Exception;
     E findById(ID id) throws Exception;
     void save(E entity) throws Exception;
     void update(ID id, E entity) throws Exception;
     void delete(ID id) throws Exception;
     List<E> findAllActive() throws Exception;

}
