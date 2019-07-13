package com.sigma.repository;

import com.sigma.domain.FotografiaMonitoreo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FotografiaMonitoreo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FotografiaMonitoreoRepository extends JpaRepository<FotografiaMonitoreo, Long> {

}
