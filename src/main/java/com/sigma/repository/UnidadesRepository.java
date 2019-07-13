package com.sigma.repository;

import com.sigma.domain.Unidades;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Unidades entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnidadesRepository extends JpaRepository<Unidades, Long> {

}
