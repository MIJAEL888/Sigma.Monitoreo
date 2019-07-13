package com.sigma.web.rest;

import com.sigma.domain.FotografiaMonitoreo;
import com.sigma.repository.FotografiaMonitoreoRepository;
import com.sigma.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sigma.domain.FotografiaMonitoreo}.
 */
@RestController
@RequestMapping("/api")
public class FotografiaMonitoreoResource {

    private final Logger log = LoggerFactory.getLogger(FotografiaMonitoreoResource.class);

    private static final String ENTITY_NAME = "monitoreoFotografiaMonitoreo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FotografiaMonitoreoRepository fotografiaMonitoreoRepository;

    public FotografiaMonitoreoResource(FotografiaMonitoreoRepository fotografiaMonitoreoRepository) {
        this.fotografiaMonitoreoRepository = fotografiaMonitoreoRepository;
    }

    /**
     * {@code POST  /fotografia-monitoreos} : Create a new fotografiaMonitoreo.
     *
     * @param fotografiaMonitoreo the fotografiaMonitoreo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fotografiaMonitoreo, or with status {@code 400 (Bad Request)} if the fotografiaMonitoreo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fotografia-monitoreos")
    public ResponseEntity<FotografiaMonitoreo> createFotografiaMonitoreo(@Valid @RequestBody FotografiaMonitoreo fotografiaMonitoreo) throws URISyntaxException {
        log.debug("REST request to save FotografiaMonitoreo : {}", fotografiaMonitoreo);
        if (fotografiaMonitoreo.getId() != null) {
            throw new BadRequestAlertException("A new fotografiaMonitoreo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FotografiaMonitoreo result = fotografiaMonitoreoRepository.save(fotografiaMonitoreo);
        return ResponseEntity.created(new URI("/api/fotografia-monitoreos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fotografia-monitoreos} : Updates an existing fotografiaMonitoreo.
     *
     * @param fotografiaMonitoreo the fotografiaMonitoreo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fotografiaMonitoreo,
     * or with status {@code 400 (Bad Request)} if the fotografiaMonitoreo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fotografiaMonitoreo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fotografia-monitoreos")
    public ResponseEntity<FotografiaMonitoreo> updateFotografiaMonitoreo(@Valid @RequestBody FotografiaMonitoreo fotografiaMonitoreo) throws URISyntaxException {
        log.debug("REST request to update FotografiaMonitoreo : {}", fotografiaMonitoreo);
        if (fotografiaMonitoreo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FotografiaMonitoreo result = fotografiaMonitoreoRepository.save(fotografiaMonitoreo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fotografiaMonitoreo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fotografia-monitoreos} : get all the fotografiaMonitoreos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fotografiaMonitoreos in body.
     */
    @GetMapping("/fotografia-monitoreos")
    public List<FotografiaMonitoreo> getAllFotografiaMonitoreos() {
        log.debug("REST request to get all FotografiaMonitoreos");
        return fotografiaMonitoreoRepository.findAll();
    }

    /**
     * {@code GET  /fotografia-monitoreos/:id} : get the "id" fotografiaMonitoreo.
     *
     * @param id the id of the fotografiaMonitoreo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fotografiaMonitoreo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fotografia-monitoreos/{id}")
    public ResponseEntity<FotografiaMonitoreo> getFotografiaMonitoreo(@PathVariable Long id) {
        log.debug("REST request to get FotografiaMonitoreo : {}", id);
        Optional<FotografiaMonitoreo> fotografiaMonitoreo = fotografiaMonitoreoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fotografiaMonitoreo);
    }

    /**
     * {@code DELETE  /fotografia-monitoreos/:id} : delete the "id" fotografiaMonitoreo.
     *
     * @param id the id of the fotografiaMonitoreo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fotografia-monitoreos/{id}")
    public ResponseEntity<Void> deleteFotografiaMonitoreo(@PathVariable Long id) {
        log.debug("REST request to delete FotografiaMonitoreo : {}", id);
        fotografiaMonitoreoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
