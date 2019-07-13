package com.sigma.web.rest;

import com.sigma.domain.ResultadoMetereologia;
import com.sigma.repository.ResultadoMetereologiaRepository;
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
 * REST controller for managing {@link com.sigma.domain.ResultadoMetereologia}.
 */
@RestController
@RequestMapping("/api")
public class ResultadoMetereologiaResource {

    private final Logger log = LoggerFactory.getLogger(ResultadoMetereologiaResource.class);

    private static final String ENTITY_NAME = "monitoreoResultadoMetereologia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResultadoMetereologiaRepository resultadoMetereologiaRepository;

    public ResultadoMetereologiaResource(ResultadoMetereologiaRepository resultadoMetereologiaRepository) {
        this.resultadoMetereologiaRepository = resultadoMetereologiaRepository;
    }

    /**
     * {@code POST  /resultado-metereologias} : Create a new resultadoMetereologia.
     *
     * @param resultadoMetereologia the resultadoMetereologia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resultadoMetereologia, or with status {@code 400 (Bad Request)} if the resultadoMetereologia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resultado-metereologias")
    public ResponseEntity<ResultadoMetereologia> createResultadoMetereologia(@RequestBody ResultadoMetereologia resultadoMetereologia) throws URISyntaxException {
        log.debug("REST request to save ResultadoMetereologia : {}", resultadoMetereologia);
        if (resultadoMetereologia.getId() != null) {
            throw new BadRequestAlertException("A new resultadoMetereologia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResultadoMetereologia result = resultadoMetereologiaRepository.save(resultadoMetereologia);
        return ResponseEntity.created(new URI("/api/resultado-metereologias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resultado-metereologias} : Updates an existing resultadoMetereologia.
     *
     * @param resultadoMetereologia the resultadoMetereologia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resultadoMetereologia,
     * or with status {@code 400 (Bad Request)} if the resultadoMetereologia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resultadoMetereologia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resultado-metereologias")
    public ResponseEntity<ResultadoMetereologia> updateResultadoMetereologia(@RequestBody ResultadoMetereologia resultadoMetereologia) throws URISyntaxException {
        log.debug("REST request to update ResultadoMetereologia : {}", resultadoMetereologia);
        if (resultadoMetereologia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResultadoMetereologia result = resultadoMetereologiaRepository.save(resultadoMetereologia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resultadoMetereologia.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resultado-metereologias} : get all the resultadoMetereologias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resultadoMetereologias in body.
     */
    @GetMapping("/resultado-metereologias")
    public List<ResultadoMetereologia> getAllResultadoMetereologias() {
        log.debug("REST request to get all ResultadoMetereologias");
        return resultadoMetereologiaRepository.findAll();
    }

    /**
     * {@code GET  /resultado-metereologias/:id} : get the "id" resultadoMetereologia.
     *
     * @param id the id of the resultadoMetereologia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resultadoMetereologia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resultado-metereologias/{id}")
    public ResponseEntity<ResultadoMetereologia> getResultadoMetereologia(@PathVariable Long id) {
        log.debug("REST request to get ResultadoMetereologia : {}", id);
        Optional<ResultadoMetereologia> resultadoMetereologia = resultadoMetereologiaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resultadoMetereologia);
    }

    /**
     * {@code DELETE  /resultado-metereologias/:id} : delete the "id" resultadoMetereologia.
     *
     * @param id the id of the resultadoMetereologia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resultado-metereologias/{id}")
    public ResponseEntity<Void> deleteResultadoMetereologia(@PathVariable Long id) {
        log.debug("REST request to delete ResultadoMetereologia : {}", id);
        resultadoMetereologiaRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
