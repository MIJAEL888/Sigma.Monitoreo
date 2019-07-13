package com.sigma.web.rest;

import com.sigma.domain.FotografiaPunto;
import com.sigma.repository.FotografiaPuntoRepository;
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
 * REST controller for managing {@link com.sigma.domain.FotografiaPunto}.
 */
@RestController
@RequestMapping("/api")
public class FotografiaPuntoResource {

    private final Logger log = LoggerFactory.getLogger(FotografiaPuntoResource.class);

    private static final String ENTITY_NAME = "monitoreoFotografiaPunto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FotografiaPuntoRepository fotografiaPuntoRepository;

    public FotografiaPuntoResource(FotografiaPuntoRepository fotografiaPuntoRepository) {
        this.fotografiaPuntoRepository = fotografiaPuntoRepository;
    }

    /**
     * {@code POST  /fotografia-puntos} : Create a new fotografiaPunto.
     *
     * @param fotografiaPunto the fotografiaPunto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fotografiaPunto, or with status {@code 400 (Bad Request)} if the fotografiaPunto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fotografia-puntos")
    public ResponseEntity<FotografiaPunto> createFotografiaPunto(@Valid @RequestBody FotografiaPunto fotografiaPunto) throws URISyntaxException {
        log.debug("REST request to save FotografiaPunto : {}", fotografiaPunto);
        if (fotografiaPunto.getId() != null) {
            throw new BadRequestAlertException("A new fotografiaPunto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FotografiaPunto result = fotografiaPuntoRepository.save(fotografiaPunto);
        return ResponseEntity.created(new URI("/api/fotografia-puntos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fotografia-puntos} : Updates an existing fotografiaPunto.
     *
     * @param fotografiaPunto the fotografiaPunto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fotografiaPunto,
     * or with status {@code 400 (Bad Request)} if the fotografiaPunto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fotografiaPunto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fotografia-puntos")
    public ResponseEntity<FotografiaPunto> updateFotografiaPunto(@Valid @RequestBody FotografiaPunto fotografiaPunto) throws URISyntaxException {
        log.debug("REST request to update FotografiaPunto : {}", fotografiaPunto);
        if (fotografiaPunto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FotografiaPunto result = fotografiaPuntoRepository.save(fotografiaPunto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fotografiaPunto.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fotografia-puntos} : get all the fotografiaPuntos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fotografiaPuntos in body.
     */
    @GetMapping("/fotografia-puntos")
    public List<FotografiaPunto> getAllFotografiaPuntos() {
        log.debug("REST request to get all FotografiaPuntos");
        return fotografiaPuntoRepository.findAll();
    }

    /**
     * {@code GET  /fotografia-puntos/:id} : get the "id" fotografiaPunto.
     *
     * @param id the id of the fotografiaPunto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fotografiaPunto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fotografia-puntos/{id}")
    public ResponseEntity<FotografiaPunto> getFotografiaPunto(@PathVariable Long id) {
        log.debug("REST request to get FotografiaPunto : {}", id);
        Optional<FotografiaPunto> fotografiaPunto = fotografiaPuntoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fotografiaPunto);
    }

    /**
     * {@code DELETE  /fotografia-puntos/:id} : delete the "id" fotografiaPunto.
     *
     * @param id the id of the fotografiaPunto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fotografia-puntos/{id}")
    public ResponseEntity<Void> deleteFotografiaPunto(@PathVariable Long id) {
        log.debug("REST request to delete FotografiaPunto : {}", id);
        fotografiaPuntoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
