package com.sigma.web.rest;

import com.sigma.domain.EquipoMonitoreo;
import com.sigma.repository.EquipoMonitoreoRepository;
import com.sigma.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sigma.domain.EquipoMonitoreo}.
 */
@RestController
@RequestMapping("/api")
public class EquipoMonitoreoResource {

    private final Logger log = LoggerFactory.getLogger(EquipoMonitoreoResource.class);

    private static final String ENTITY_NAME = "monitoreoEquipoMonitoreo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipoMonitoreoRepository equipoMonitoreoRepository;

    public EquipoMonitoreoResource(EquipoMonitoreoRepository equipoMonitoreoRepository) {
        this.equipoMonitoreoRepository = equipoMonitoreoRepository;
    }

    /**
     * {@code POST  /equipo-monitoreos} : Create a new equipoMonitoreo.
     *
     * @param equipoMonitoreo the equipoMonitoreo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipoMonitoreo, or with status {@code 400 (Bad Request)} if the equipoMonitoreo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipo-monitoreos")
    public ResponseEntity<EquipoMonitoreo> createEquipoMonitoreo(@RequestBody EquipoMonitoreo equipoMonitoreo) throws URISyntaxException {
        log.debug("REST request to save EquipoMonitoreo : {}", equipoMonitoreo);
        if (equipoMonitoreo.getId() != null) {
            throw new BadRequestAlertException("A new equipoMonitoreo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EquipoMonitoreo result = equipoMonitoreoRepository.save(equipoMonitoreo);
        return ResponseEntity.created(new URI("/api/equipo-monitoreos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equipo-monitoreos} : Updates an existing equipoMonitoreo.
     *
     * @param equipoMonitoreo the equipoMonitoreo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipoMonitoreo,
     * or with status {@code 400 (Bad Request)} if the equipoMonitoreo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipoMonitoreo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipo-monitoreos")
    public ResponseEntity<EquipoMonitoreo> updateEquipoMonitoreo(@RequestBody EquipoMonitoreo equipoMonitoreo) throws URISyntaxException {
        log.debug("REST request to update EquipoMonitoreo : {}", equipoMonitoreo);
        if (equipoMonitoreo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EquipoMonitoreo result = equipoMonitoreoRepository.save(equipoMonitoreo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, equipoMonitoreo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /equipo-monitoreos} : get all the equipoMonitoreos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipoMonitoreos in body.
     */
    @GetMapping("/equipo-monitoreos")
    public List<EquipoMonitoreo> getAllEquipoMonitoreos() {
        log.debug("REST request to get all EquipoMonitoreos");
        return equipoMonitoreoRepository.findAll();
    }

    /**
     * {@code GET  /equipo-monitoreos/:id} : get the "id" equipoMonitoreo.
     *
     * @param id the id of the equipoMonitoreo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipoMonitoreo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipo-monitoreos/{id}")
    public ResponseEntity<EquipoMonitoreo> getEquipoMonitoreo(@PathVariable Long id) {
        log.debug("REST request to get EquipoMonitoreo : {}", id);
        Optional<EquipoMonitoreo> equipoMonitoreo = equipoMonitoreoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(equipoMonitoreo);
    }

    /**
     * {@code DELETE  /equipo-monitoreos/:id} : delete the "id" equipoMonitoreo.
     *
     * @param id the id of the equipoMonitoreo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equipo-monitoreos/{id}")
    public ResponseEntity<Void> deleteEquipoMonitoreo(@PathVariable Long id) {
        log.debug("REST request to delete EquipoMonitoreo : {}", id);
        equipoMonitoreoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
