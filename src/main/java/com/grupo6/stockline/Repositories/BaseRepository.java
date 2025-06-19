package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<E extends Base, ID extends Serializable> extends JpaRepository<E,ID> {

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} e SET e.fechaBaja = CURRENT_TIMESTAMP WHERE e.id = :id AND e.fechaBaja IS NULL")
    void darDeBajaPorId(ID id);

    @Query("SELECT e FROM #{#entityName} e WHERE e.fechaBaja IS NULL")
    List<E> findAllActive();

}