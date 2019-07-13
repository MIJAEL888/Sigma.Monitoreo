package com.sigma.repository;

import com.sigma.domain.Paramentro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Paramentro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParamentroRepository extends JpaRepository<Paramentro, Long> {

}
