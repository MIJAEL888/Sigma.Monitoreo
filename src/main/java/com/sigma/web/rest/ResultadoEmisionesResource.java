package com.sigma.web.rest;

import com.sigma.domain.ResultadoEmisiones;
import com.sigma.repository.ResultadoEmisionesRepository;
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
 * REST controller for managing {@link com.sigma.domain.ResultadoEmisiones}.
 */
@RestController
@RequestMapping("/api")
public class ResultadoEmisionesResource {

    private final Logger log = LoggerFactory.getLogger(ResultadoEmisionesResource.class);

    private static final String ENTITY_NAME = "monitoreoResultadoEmisiones";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResultadoEmisionesRepository resultadoEmisionesRepository;

    public ResultadoEmisionesResource(ResultadoEmisionesRepository resultadoEmisionesRepository) {
        this.resultadoEmisionesRepository = resultadoEmisionesRepository;
    }

    /**
     * {@code POST  /resultado-emisiones} : Create a new resultadoEmisiones.
     *
     * @param resultadoEmisiones the resultadoEmisiones to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resultadoEmisiones, or with status {@code 400 (Bad Request)} if the resultadoEmisiones has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resultado-emisiones")
    public ResponseEntity<ResultadoEmisiones> createResultadoEmisiones(@RequestBody ResultadoEmisiones resultadoEmisiones) throws URISyntaxException {
        log.debug("REST request to save ResultadoEmisiones : {}", resultadoEmisiones);
        if (resultadoEmisiones.getId() != null) {
            throw new BadRequestAlertException("A new resultadoEmisiones cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResultadoEmisiones result = resultadoEmisionesRepository.save(resultadoEmisiones);
        return ResponseEntity.created(new URI("/api/resultado-emisiones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resultado-emisiones} : Updates an existing resultadoEmisiones.
     *
     * @param resultadoEmisiones the resultadoEmisiones to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resultadoEmisiones,
     * or with status {@code 400 (Bad Request)} if the resultadoEmisiones is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resultadoEmisiones couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resultado-emisiones")
    public ResponseEntity<ResultadoEmisiones> updateResultadoEmisiones(@RequestBody ResultadoEmisiones resultadoEmisiones) throws URISyntaxException {
        log.debug("REST request to update ResultadoEmisiones : {}", resultadoEmisiones);
        if (resultadoEmisiones.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResultadoEmisiones result = resultadoEmisionesRepository.save(resultadoEmisiones);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resultadoEmisiones.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resultado-emisiones} : get all the resultadoEmisiones.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resultadoEmisiones in body.
     */
    @GetMapping("/resultado-emisiones")
    public List<ResultadoEmisiones> getAllResultadoEmisiones() {
        log.debug("REST request to get all ResultadoEmisiones");
        return resultadoEmisionesRepository.findAll();
    }

    /**
     * {@code GET  /resultado-emisiones/:id} : get the "id" resultadoEmisiones.
     *
     * @param id the id of the resultadoEmisiones to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resultadoEmisiones, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resultado-emisiones/{id}")
    public ResponseEntity<ResultadoEmisiones> getResultadoEmisiones(@PathVariable Long id) {
        log.debug("REST request to get ResultadoEmisiones : {}", id);
        Optional<ResultadoEmisiones> resultadoEmisiones = resultadoEmisionesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resultadoEmisiones);
    }

    /**
     * {@code DELETE  /resultado-emisiones/:id} : delete the "id" resultadoEmisiones.
     *
     * @param id the id of the resultadoEmisiones to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resultado-emisiones/{id}")
    public ResponseEntity<Void> deleteResultadoEmisiones(@PathVariable Long id) {
        log.debug("REST request to delete ResultadoEmisiones : {}", id);
        resultadoEmisionesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
