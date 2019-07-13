package com.sigma.repository;

import com.sigma.domain.Resultado;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Resultado entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

}
