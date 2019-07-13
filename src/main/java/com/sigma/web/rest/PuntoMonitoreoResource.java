package com.sigma.web.rest;

import com.sigma.domain.PuntoMonitoreo;
import com.sigma.repository.PuntoMonitoreoRepository;
import com.sigma.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sigma.domain.PuntoMonitoreo}.
 */
@RestController
@RequestMapping("/api")
public class PuntoMonitoreoResource {

    private final Logger log = LoggerFactory.getLogger(PuntoMonitoreoResource.class);

    private static final String ENTITY_NAME = "monitoreoPuntoMonitoreo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PuntoMonitoreoRepository puntoMonitoreoRepository;

    public PuntoMonitoreoResource(PuntoMonitoreoRepository puntoMonitoreoRepository) {
        this.puntoMonitoreoRepository = puntoMonitoreoRepository;
    }

    /**
     * {@code POST  /punto-monitoreos} : Create a new puntoMonitoreo.
     *
     * @param puntoMonitoreo the puntoMonitoreo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new puntoMonitoreo, or with status {@code 400 (Bad Request)} if the puntoMonitoreo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/punto-monitoreos")
    public ResponseEntity<PuntoMonitoreo> createPuntoMonitoreo(@Valid @RequestBody PuntoMonitoreo puntoMonitoreo) throws URISyntaxException {
        log.debug("REST request to save PuntoMonitoreo : {}", puntoMonitoreo);
        if (puntoMonitoreo.getId() != null) {
            throw new BadRequestAlertException("A new puntoMonitoreo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PuntoMonitoreo result = puntoMonitoreoRepository.save(puntoMonitoreo);
        return ResponseEntity.created(new URI("/api/punto-monitoreos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /punto-monitoreos} : Updates an existing puntoMonitoreo.
     *
     * @param puntoMonitoreo the puntoMonitoreo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated puntoMonitoreo,
     * or with status {@code 400 (Bad Request)} if the puntoMonitoreo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the puntoMonitoreo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/punto-monitoreos")
    public ResponseEntity<PuntoMonitoreo> updatePuntoMonitoreo(@Valid @RequestBody PuntoMonitoreo puntoMonitoreo) throws URISyntaxException {
        log.debug("REST request to update PuntoMonitoreo : {}", puntoMonitoreo);
        if (puntoMonitoreo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PuntoMonitoreo result = puntoMonitoreoRepository.save(puntoMonitoreo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, puntoMonitoreo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /punto-monitoreos} : get all the puntoMonitoreos.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of puntoMonitoreos in body.
     */
    @GetMapping("/punto-monitoreos")
    public ResponseEntity<List<PuntoMonitoreo>> getAllPuntoMonitoreos(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of PuntoMonitoreos");
        Page<PuntoMonitoreo> page = puntoMonitoreoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /punto-monitoreos/:id} : get the "id" puntoMonitoreo.
     *
     * @param id the id of the puntoMonitoreo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the puntoMonitoreo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/punto-monitoreos/{id}")
    public ResponseEntity<PuntoMonitoreo> getPuntoMonitoreo(@PathVariable Long id) {
        log.debug("REST request to get PuntoMonitoreo : {}", id);
        Optional<PuntoMonitoreo> puntoMonitoreo = puntoMonitoreoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(puntoMonitoreo);
    }

    /**
     * {@code DELETE  /punto-monitoreos/:id} : delete the "id" puntoMonitoreo.
     *
     * @param id the id of the puntoMonitoreo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/punto-monitoreos/{id}")
    public ResponseEntity<Void> deletePuntoMonitoreo(@PathVariable Long id) {
        log.debug("REST request to delete PuntoMonitoreo : {}", id);
        puntoMonitoreoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
