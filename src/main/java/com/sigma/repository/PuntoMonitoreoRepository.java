package com.sigma.repository;

import com.sigma.domain.PuntoMonitoreo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PuntoMonitoreo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PuntoMonitoreoRepository extends JpaRepository<PuntoMonitoreo, Long> {

}
