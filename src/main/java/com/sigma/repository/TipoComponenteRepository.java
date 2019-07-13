package com.sigma.repository;

import com.sigma.domain.TipoComponente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TipoComponente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoComponenteRepository extends JpaRepository<TipoComponente, Long> {

}
