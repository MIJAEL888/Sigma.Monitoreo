package com.sigma.repository;

import com.sigma.domain.LaboratorioMonitoreo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LaboratorioMonitoreo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LaboratorioMonitoreoRepository extends JpaRepository<LaboratorioMonitoreo, Long> {

}
