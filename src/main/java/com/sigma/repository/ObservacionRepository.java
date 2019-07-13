package com.sigma.repository;

import com.sigma.domain.Observacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Observacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObservacionRepository extends JpaRepository<Observacion, Long> {

}
