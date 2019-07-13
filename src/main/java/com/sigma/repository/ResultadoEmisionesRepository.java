package com.sigma.repository;

import com.sigma.domain.ResultadoEmisiones;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ResultadoEmisiones entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResultadoEmisionesRepository extends JpaRepository<ResultadoEmisiones, Long> {

}
