package com.sigma.web.rest;

import com.sigma.domain.LaboratorioMonitoreo;
import com.sigma.repository.LaboratorioMonitoreoRepository;
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
 * REST controller for managing {@link com.sigma.domain.LaboratorioMonitoreo}.
 */
@RestController
@RequestMapping("/api")
public class LaboratorioMonitoreoResource {

    private final Logger log = LoggerFactory.getLogger(LaboratorioMonitoreoResource.class);

    private static final String ENTITY_NAME = "monitoreoLaboratorioMonitoreo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LaboratorioMonitoreoRepository laboratorioMonitoreoRepository;

    public LaboratorioMonitoreoResource(LaboratorioMonitoreoRepository laboratorioMonitoreoRepository) {
        this.laboratorioMonitoreoRepository = laboratorioMonitoreoRepository;
    }

    /**
     * {@code POST  /laboratorio-monitoreos} : Create a new laboratorioMonitoreo.
     *
     * @param laboratorioMonitoreo the laboratorioMonitoreo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new laboratorioMonitoreo, or with status {@code 400 (Bad Request)} if the laboratorioMonitoreo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/laboratorio-monitoreos")
    public ResponseEntity<LaboratorioMonitoreo> createLaboratorioMonitoreo(@RequestBody LaboratorioMonitoreo laboratorioMonitoreo) throws URISyntaxException {
        log.debug("REST request to save LaboratorioMonitoreo : {}", laboratorioMonitoreo);
        if (laboratorioMonitoreo.getId() != null) {
            throw new BadRequestAlertException("A new laboratorioMonitoreo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LaboratorioMonitoreo result = laboratorioMonitoreoRepository.save(laboratorioMonitoreo);
        return ResponseEntity.created(new URI("/api/laboratorio-monitoreos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /laboratorio-monitoreos} : Updates an existing laboratorioMonitoreo.
     *
     * @param laboratorioMonitoreo the laboratorioMonitoreo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated laboratorioMonitoreo,
     * or with status {@code 400 (Bad Request)} if the laboratorioMonitoreo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the laboratorioMonitoreo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/laboratorio-monitoreos")
    public ResponseEntity<LaboratorioMonitoreo> updateLaboratorioMonitoreo(@RequestBody LaboratorioMonitoreo laboratorioMonitoreo) throws URISyntaxException {
        log.debug("REST request to update LaboratorioMonitoreo : {}", laboratorioMonitoreo);
        if (laboratorioMonitoreo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LaboratorioMonitoreo result = laboratorioMonitoreoRepository.save(laboratorioMonitoreo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, laboratorioMonitoreo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /laboratorio-monitoreos} : get all the laboratorioMonitoreos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of laboratorioMonitoreos in body.
     */
    @GetMapping("/laboratorio-monitoreos")
    public List<LaboratorioMonitoreo> getAllLaboratorioMonitoreos() {
        log.debug("REST request to get all LaboratorioMonitoreos");
        return laboratorioMonitoreoRepository.findAll();
    }

    /**
     * {@code GET  /laboratorio-monitoreos/:id} : get the "id" laboratorioMonitoreo.
     *
     * @param id the id of the laboratorioMonitoreo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the laboratorioMonitoreo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/laboratorio-monitoreos/{id}")
    public ResponseEntity<LaboratorioMonitoreo> getLaboratorioMonitoreo(@PathVariable Long id) {
        log.debug("REST request to get LaboratorioMonitoreo : {}", id);
        Optional<LaboratorioMonitoreo> laboratorioMonitoreo = laboratorioMonitoreoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(laboratorioMonitoreo);
    }

    /**
     * {@code DELETE  /laboratorio-monitoreos/:id} : delete the "id" laboratorioMonitoreo.
     *
     * @param id the id of the laboratorioMonitoreo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/laboratorio-monitoreos/{id}")
    public ResponseEntity<Void> deleteLaboratorioMonitoreo(@PathVariable Long id) {
        log.debug("REST request to delete LaboratorioMonitoreo : {}", id);
        laboratorioMonitoreoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
