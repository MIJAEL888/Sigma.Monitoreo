package com.sigma.repository;

import com.sigma.domain.NormaCalidad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the NormaCalidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NormaCalidadRepository extends JpaRepository<NormaCalidad, Long> {

}
