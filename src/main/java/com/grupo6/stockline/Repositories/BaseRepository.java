package com.grupo6.stockline.Repositories;

import com.grupo6.stockline.Entities.Base;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<E extends Base, ID extends Serializable> extends JpaRepository<E,ID> {
    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} e SET e.fechaBaja = CURRENT_DATE WHERE e.id = :id AND e.fechaBaja IS NULL")
    void darDeBajaPorId(ID id);

}
