package com.sigma.repository;

import com.sigma.domain.ResultadoMetereologia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ResultadoMetereologia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResultadoMetereologiaRepository extends JpaRepository<ResultadoMetereologia, Long> {

}
