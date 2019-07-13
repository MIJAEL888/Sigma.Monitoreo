package com.sigma.repository;

import com.sigma.domain.EquipoMonitoreo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EquipoMonitoreo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipoMonitoreoRepository extends JpaRepository<EquipoMonitoreo, Long> {

}
