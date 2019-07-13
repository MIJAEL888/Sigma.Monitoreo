package com.sigma.web.rest;

import com.sigma.domain.NormaCalidad;
import com.sigma.repository.NormaCalidadRepository;
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
 * REST controller for managing {@link com.sigma.domain.NormaCalidad}.
 */
@RestController
@RequestMapping("/api")
public class NormaCalidadResource {

    private final Logger log = LoggerFactory.getLogger(NormaCalidadResource.class);

    private static final String ENTITY_NAME = "monitoreoNormaCalidad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NormaCalidadRepository normaCalidadRepository;

    public NormaCalidadResource(NormaCalidadRepository normaCalidadRepository) {
        this.normaCalidadRepository = normaCalidadRepository;
    }

    /**
     * {@code POST  /norma-calidads} : Create a new normaCalidad.
     *
     * @param normaCalidad the normaCalidad to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new normaCalidad, or with status {@code 400 (Bad Request)} if the normaCalidad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/norma-calidads")
    public ResponseEntity<NormaCalidad> createNormaCalidad(@Valid @RequestBody NormaCalidad normaCalidad) throws URISyntaxException {
        log.debug("REST request to save NormaCalidad : {}", normaCalidad);
        if (normaCalidad.getId() != null) {
            throw new BadRequestAlertException("A new normaCalidad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NormaCalidad result = normaCalidadRepository.save(normaCalidad);
        return ResponseEntity.created(new URI("/api/norma-calidads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /norma-calidads} : Updates an existing normaCalidad.
     *
     * @param normaCalidad the normaCalidad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated normaCalidad,
     * or with status {@code 400 (Bad Request)} if the normaCalidad is not valid,
     * or with status {@code 500 (Internal Server Error)} if the normaCalidad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/norma-calidads")
    public ResponseEntity<NormaCalidad> updateNormaCalidad(@Valid @RequestBody NormaCalidad normaCalidad) throws URISyntaxException {
        log.debug("REST request to update NormaCalidad : {}", normaCalidad);
        if (normaCalidad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NormaCalidad result = normaCalidadRepository.save(normaCalidad);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, normaCalidad.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /norma-calidads} : get all the normaCalidads.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of normaCalidads in body.
     */
    @GetMapping("/norma-calidads")
    public List<NormaCalidad> getAllNormaCalidads() {
        log.debug("REST request to get all NormaCalidads");
        return normaCalidadRepository.findAll();
    }

    /**
     * {@code GET  /norma-calidads/:id} : get the "id" normaCalidad.
     *
     * @param id the id of the normaCalidad to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the normaCalidad, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/norma-calidads/{id}")
    public ResponseEntity<NormaCalidad> getNormaCalidad(@PathVariable Long id) {
        log.debug("REST request to get NormaCalidad : {}", id);
        Optional<NormaCalidad> normaCalidad = normaCalidadRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(normaCalidad);
    }

    /**
     * {@code DELETE  /norma-calidads/:id} : delete the "id" normaCalidad.
     *
     * @param id the id of the normaCalidad to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/norma-calidads/{id}")
    public ResponseEntity<Void> deleteNormaCalidad(@PathVariable Long id) {
        log.debug("REST request to delete NormaCalidad : {}", id);
        normaCalidadRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
