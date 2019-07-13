package com.sigma.repository;

import com.sigma.domain.PuntoMonitoreoObs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PuntoMonitoreoObs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PuntoMonitoreoObsRepository extends JpaRepository<PuntoMonitoreoObs, Long> {

}
