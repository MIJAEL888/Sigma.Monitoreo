package com.sigma.web.rest;

import com.sigma.domain.PuntoMonitoreoObs;
import com.sigma.repository.PuntoMonitoreoObsRepository;
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
 * REST controller for managing {@link com.sigma.domain.PuntoMonitoreoObs}.
 */
@RestController
@RequestMapping("/api")
public class PuntoMonitoreoObsResource {

    private final Logger log = LoggerFactory.getLogger(PuntoMonitoreoObsResource.class);

    private static final String ENTITY_NAME = "monitoreoPuntoMonitoreoObs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PuntoMonitoreoObsRepository puntoMonitoreoObsRepository;

    public PuntoMonitoreoObsResource(PuntoMonitoreoObsRepository puntoMonitoreoObsRepository) {
        this.puntoMonitoreoObsRepository = puntoMonitoreoObsRepository;
    }

    /**
     * {@code POST  /punto-monitoreo-obs} : Create a new puntoMonitoreoObs.
     *
     * @param puntoMonitoreoObs the puntoMonitoreoObs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new puntoMonitoreoObs, or with status {@code 400 (Bad Request)} if the puntoMonitoreoObs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/punto-monitoreo-obs")
    public ResponseEntity<PuntoMonitoreoObs> createPuntoMonitoreoObs(@Valid @RequestBody PuntoMonitoreoObs puntoMonitoreoObs) throws URISyntaxException {
        log.debug("REST request to save PuntoMonitoreoObs : {}", puntoMonitoreoObs);
        if (puntoMonitoreoObs.getId() != null) {
            throw new BadRequestAlertException("A new puntoMonitoreoObs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PuntoMonitoreoObs result = puntoMonitoreoObsRepository.save(puntoMonitoreoObs);
        return ResponseEntity.created(new URI("/api/punto-monitoreo-obs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /punto-monitoreo-obs} : Updates an existing puntoMonitoreoObs.
     *
     * @param puntoMonitoreoObs the puntoMonitoreoObs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated puntoMonitoreoObs,
     * or with status {@code 400 (Bad Request)} if the puntoMonitoreoObs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the puntoMonitoreoObs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/punto-monitoreo-obs")
    public ResponseEntity<PuntoMonitoreoObs> updatePuntoMonitoreoObs(@Valid @RequestBody PuntoMonitoreoObs puntoMonitoreoObs) throws URISyntaxException {
        log.debug("REST request to update PuntoMonitoreoObs : {}", puntoMonitoreoObs);
        if (puntoMonitoreoObs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PuntoMonitoreoObs result = puntoMonitoreoObsRepository.save(puntoMonitoreoObs);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, puntoMonitoreoObs.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /punto-monitoreo-obs} : get all the puntoMonitoreoObs.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of puntoMonitoreoObs in body.
     */
    @GetMapping("/punto-monitoreo-obs")
    public ResponseEntity<List<PuntoMonitoreoObs>> getAllPuntoMonitoreoObs(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of PuntoMonitoreoObs");
        Page<PuntoMonitoreoObs> page = puntoMonitoreoObsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /punto-monitoreo-obs/:id} : get the "id" puntoMonitoreoObs.
     *
     * @param id the id of the puntoMonitoreoObs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the puntoMonitoreoObs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/punto-monitoreo-obs/{id}")
    public ResponseEntity<PuntoMonitoreoObs> getPuntoMonitoreoObs(@PathVariable Long id) {
        log.debug("REST request to get PuntoMonitoreoObs : {}", id);
        Optional<PuntoMonitoreoObs> puntoMonitoreoObs = puntoMonitoreoObsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(puntoMonitoreoObs);
    }

    /**
     * {@code DELETE  /punto-monitoreo-obs/:id} : delete the "id" puntoMonitoreoObs.
     *
     * @param id the id of the puntoMonitoreoObs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/punto-monitoreo-obs/{id}")
    public ResponseEntity<Void> deletePuntoMonitoreoObs(@PathVariable Long id) {
        log.debug("REST request to delete PuntoMonitoreoObs : {}", id);
        puntoMonitoreoObsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
