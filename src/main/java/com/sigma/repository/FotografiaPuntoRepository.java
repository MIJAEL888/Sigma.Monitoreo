package com.sigma.repository;

import com.sigma.domain.FotografiaPunto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FotografiaPunto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FotografiaPuntoRepository extends JpaRepository<FotografiaPunto, Long> {

}
