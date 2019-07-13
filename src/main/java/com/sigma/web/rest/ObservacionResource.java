package com.sigma.web.rest;

import com.sigma.domain.Observacion;
import com.sigma.repository.ObservacionRepository;
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
 * REST controller for managing {@link com.sigma.domain.Observacion}.
 */
@RestController
@RequestMapping("/api")
public class ObservacionResource {

    private final Logger log = LoggerFactory.getLogger(ObservacionResource.class);

    private static final String ENTITY_NAME = "monitoreoObservacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObservacionRepository observacionRepository;

    public ObservacionResource(ObservacionRepository observacionRepository) {
        this.observacionRepository = observacionRepository;
    }

    /**
     * {@code POST  /observacions} : Create a new observacion.
     *
     * @param observacion the observacion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new observacion, or with status {@code 400 (Bad Request)} if the observacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/observacions")
    public ResponseEntity<Observacion> createObservacion(@RequestBody Observacion observacion) throws URISyntaxException {
        log.debug("REST request to save Observacion : {}", observacion);
        if (observacion.getId() != null) {
            throw new BadRequestAlertException("A new observacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Observacion result = observacionRepository.save(observacion);
        return ResponseEntity.created(new URI("/api/observacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /observacions} : Updates an existing observacion.
     *
     * @param observacion the observacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated observacion,
     * or with status {@code 400 (Bad Request)} if the observacion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the observacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/observacions")
    public ResponseEntity<Observacion> updateObservacion(@RequestBody Observacion observacion) throws URISyntaxException {
        log.debug("REST request to update Observacion : {}", observacion);
        if (observacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Observacion result = observacionRepository.save(observacion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, observacion.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /observacions} : get all the observacions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of observacions in body.
     */
    @GetMapping("/observacions")
    public List<Observacion> getAllObservacions() {
        log.debug("REST request to get all Observacions");
        return observacionRepository.findAll();
    }

    /**
     * {@code GET  /observacions/:id} : get the "id" observacion.
     *
     * @param id the id of the observacion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the observacion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/observacions/{id}")
    public ResponseEntity<Observacion> getObservacion(@PathVariable Long id) {
        log.debug("REST request to get Observacion : {}", id);
        Optional<Observacion> observacion = observacionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(observacion);
    }

    /**
     * {@code DELETE  /observacions/:id} : delete the "id" observacion.
     *
     * @param id the id of the observacion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/observacions/{id}")
    public ResponseEntity<Void> deleteObservacion(@PathVariable Long id) {
        log.debug("REST request to delete Observacion : {}", id);
        observacionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
