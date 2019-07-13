package com.sigma.web.rest;

import com.sigma.domain.Unidades;
import com.sigma.repository.UnidadesRepository;
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
 * REST controller for managing {@link com.sigma.domain.Unidades}.
 */
@RestController
@RequestMapping("/api")
public class UnidadesResource {

    private final Logger log = LoggerFactory.getLogger(UnidadesResource.class);

    private static final String ENTITY_NAME = "monitoreoUnidades";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnidadesRepository unidadesRepository;

    public UnidadesResource(UnidadesRepository unidadesRepository) {
        this.unidadesRepository = unidadesRepository;
    }

    /**
     * {@code POST  /unidades} : Create a new unidades.
     *
     * @param unidades the unidades to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unidades, or with status {@code 400 (Bad Request)} if the unidades has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unidades")
    public ResponseEntity<Unidades> createUnidades(@RequestBody Unidades unidades) throws URISyntaxException {
        log.debug("REST request to save Unidades : {}", unidades);
        if (unidades.getId() != null) {
            throw new BadRequestAlertException("A new unidades cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Unidades result = unidadesRepository.save(unidades);
        return ResponseEntity.created(new URI("/api/unidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unidades} : Updates an existing unidades.
     *
     * @param unidades the unidades to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unidades,
     * or with status {@code 400 (Bad Request)} if the unidades is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unidades couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unidades")
    public ResponseEntity<Unidades> updateUnidades(@RequestBody Unidades unidades) throws URISyntaxException {
        log.debug("REST request to update Unidades : {}", unidades);
        if (unidades.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Unidades result = unidadesRepository.save(unidades);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, unidades.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /unidades} : get all the unidades.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unidades in body.
     */
    @GetMapping("/unidades")
    public List<Unidades> getAllUnidades() {
        log.debug("REST request to get all Unidades");
        return unidadesRepository.findAll();
    }

    /**
     * {@code GET  /unidades/:id} : get the "id" unidades.
     *
     * @param id the id of the unidades to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unidades, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unidades/{id}")
    public ResponseEntity<Unidades> getUnidades(@PathVariable Long id) {
        log.debug("REST request to get Unidades : {}", id);
        Optional<Unidades> unidades = unidadesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(unidades);
    }

    /**
     * {@code DELETE  /unidades/:id} : delete the "id" unidades.
     *
     * @param id the id of the unidades to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unidades/{id}")
    public ResponseEntity<Void> deleteUnidades(@PathVariable Long id) {
        log.debug("REST request to delete Unidades : {}", id);
        unidadesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
