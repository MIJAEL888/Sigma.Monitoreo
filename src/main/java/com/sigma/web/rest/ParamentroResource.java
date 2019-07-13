package com.sigma.web.rest;

import com.sigma.domain.Paramentro;
import com.sigma.repository.ParamentroRepository;
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
 * REST controller for managing {@link com.sigma.domain.Paramentro}.
 */
@RestController
@RequestMapping("/api")
public class ParamentroResource {

    private final Logger log = LoggerFactory.getLogger(ParamentroResource.class);

    private static final String ENTITY_NAME = "monitoreoParamentro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParamentroRepository paramentroRepository;

    public ParamentroResource(ParamentroRepository paramentroRepository) {
        this.paramentroRepository = paramentroRepository;
    }

    /**
     * {@code POST  /paramentros} : Create a new paramentro.
     *
     * @param paramentro the paramentro to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paramentro, or with status {@code 400 (Bad Request)} if the paramentro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paramentros")
    public ResponseEntity<Paramentro> createParamentro(@Valid @RequestBody Paramentro paramentro) throws URISyntaxException {
        log.debug("REST request to save Paramentro : {}", paramentro);
        if (paramentro.getId() != null) {
            throw new BadRequestAlertException("A new paramentro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Paramentro result = paramentroRepository.save(paramentro);
        return ResponseEntity.created(new URI("/api/paramentros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paramentros} : Updates an existing paramentro.
     *
     * @param paramentro the paramentro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paramentro,
     * or with status {@code 400 (Bad Request)} if the paramentro is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paramentro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paramentros")
    public ResponseEntity<Paramentro> updateParamentro(@Valid @RequestBody Paramentro paramentro) throws URISyntaxException {
        log.debug("REST request to update Paramentro : {}", paramentro);
        if (paramentro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Paramentro result = paramentroRepository.save(paramentro);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paramentro.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /paramentros} : get all the paramentros.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paramentros in body.
     */
    @GetMapping("/paramentros")
    public ResponseEntity<List<Paramentro>> getAllParamentros(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Paramentros");
        Page<Paramentro> page = paramentroRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paramentros/:id} : get the "id" paramentro.
     *
     * @param id the id of the paramentro to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paramentro, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paramentros/{id}")
    public ResponseEntity<Paramentro> getParamentro(@PathVariable Long id) {
        log.debug("REST request to get Paramentro : {}", id);
        Optional<Paramentro> paramentro = paramentroRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(paramentro);
    }

    /**
     * {@code DELETE  /paramentros/:id} : delete the "id" paramentro.
     *
     * @param id the id of the paramentro to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paramentros/{id}")
    public ResponseEntity<Void> deleteParamentro(@PathVariable Long id) {
        log.debug("REST request to delete Paramentro : {}", id);
        paramentroRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
