package com.sigma.web.rest;

import com.sigma.domain.TipoComponente;
import com.sigma.repository.TipoComponenteRepository;
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
 * REST controller for managing {@link com.sigma.domain.TipoComponente}.
 */
@RestController
@RequestMapping("/api")
public class TipoComponenteResource {

    private final Logger log = LoggerFactory.getLogger(TipoComponenteResource.class);

    private static final String ENTITY_NAME = "monitoreoTipoComponente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoComponenteRepository tipoComponenteRepository;

    public TipoComponenteResource(TipoComponenteRepository tipoComponenteRepository) {
        this.tipoComponenteRepository = tipoComponenteRepository;
    }

    /**
     * {@code POST  /tipo-componentes} : Create a new tipoComponente.
     *
     * @param tipoComponente the tipoComponente to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoComponente, or with status {@code 400 (Bad Request)} if the tipoComponente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-componentes")
    public ResponseEntity<TipoComponente> createTipoComponente(@Valid @RequestBody TipoComponente tipoComponente) throws URISyntaxException {
        log.debug("REST request to save TipoComponente : {}", tipoComponente);
        if (tipoComponente.getId() != null) {
            throw new BadRequestAlertException("A new tipoComponente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoComponente result = tipoComponenteRepository.save(tipoComponente);
        return ResponseEntity.created(new URI("/api/tipo-componentes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-componentes} : Updates an existing tipoComponente.
     *
     * @param tipoComponente the tipoComponente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoComponente,
     * or with status {@code 400 (Bad Request)} if the tipoComponente is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoComponente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-componentes")
    public ResponseEntity<TipoComponente> updateTipoComponente(@Valid @RequestBody TipoComponente tipoComponente) throws URISyntaxException {
        log.debug("REST request to update TipoComponente : {}", tipoComponente);
        if (tipoComponente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TipoComponente result = tipoComponenteRepository.save(tipoComponente);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoComponente.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tipo-componentes} : get all the tipoComponentes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoComponentes in body.
     */
    @GetMapping("/tipo-componentes")
    public List<TipoComponente> getAllTipoComponentes() {
        log.debug("REST request to get all TipoComponentes");
        return tipoComponenteRepository.findAll();
    }

    /**
     * {@code GET  /tipo-componentes/:id} : get the "id" tipoComponente.
     *
     * @param id the id of the tipoComponente to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoComponente, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-componentes/{id}")
    public ResponseEntity<TipoComponente> getTipoComponente(@PathVariable Long id) {
        log.debug("REST request to get TipoComponente : {}", id);
        Optional<TipoComponente> tipoComponente = tipoComponenteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tipoComponente);
    }

    /**
     * {@code DELETE  /tipo-componentes/:id} : delete the "id" tipoComponente.
     *
     * @param id the id of the tipoComponente to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-componentes/{id}")
    public ResponseEntity<Void> deleteTipoComponente(@PathVariable Long id) {
        log.debug("REST request to delete TipoComponente : {}", id);
        tipoComponenteRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
