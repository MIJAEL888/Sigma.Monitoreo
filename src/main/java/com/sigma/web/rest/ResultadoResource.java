package com.sigma.web.rest;

import com.sigma.domain.Resultado;
import com.sigma.repository.ResultadoRepository;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.sigma.domain.Resultado}.
 */
@RestController
@RequestMapping("/api")
public class ResultadoResource {

    private final Logger log = LoggerFactory.getLogger(ResultadoResource.class);

    private static final String ENTITY_NAME = "monitoreoResultado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResultadoRepository resultadoRepository;

    public ResultadoResource(ResultadoRepository resultadoRepository) {
        this.resultadoRepository = resultadoRepository;
    }

    /**
     * {@code POST  /resultados} : Create a new resultado.
     *
     * @param resultado the resultado to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resultado, or with status {@code 400 (Bad Request)} if the resultado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resultados")
    public ResponseEntity<Resultado> createResultado(@RequestBody Resultado resultado) throws URISyntaxException {
        log.debug("REST request to save Resultado : {}", resultado);
        if (resultado.getId() != null) {
            throw new BadRequestAlertException("A new resultado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Resultado result = resultadoRepository.save(resultado);
        return ResponseEntity.created(new URI("/api/resultados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resultados} : Updates an existing resultado.
     *
     * @param resultado the resultado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resultado,
     * or with status {@code 400 (Bad Request)} if the resultado is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resultado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resultados")
    public ResponseEntity<Resultado> updateResultado(@RequestBody Resultado resultado) throws URISyntaxException {
        log.debug("REST request to update Resultado : {}", resultado);
        if (resultado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Resultado result = resultadoRepository.save(resultado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resultado.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resultados} : get all the resultados.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resultados in body.
     */
    @GetMapping("/resultados")
    public ResponseEntity<List<Resultado>> getAllResultados(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder, @RequestParam(required = false) String filter) {
        if ("puntomonitoreoobs-is-null".equals(filter)) {
            log.debug("REST request to get all Resultados where puntoMonitoreoObs is null");
            return new ResponseEntity<>(StreamSupport
                .stream(resultadoRepository.findAll().spliterator(), false)
                .filter(resultado -> resultado.getPuntoMonitoreoObs() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Resultados");
        Page<Resultado> page = resultadoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /resultados/:id} : get the "id" resultado.
     *
     * @param id the id of the resultado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resultado, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resultados/{id}")
    public ResponseEntity<Resultado> getResultado(@PathVariable Long id) {
        log.debug("REST request to get Resultado : {}", id);
        Optional<Resultado> resultado = resultadoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resultado);
    }

    /**
     * {@code DELETE  /resultados/:id} : delete the "id" resultado.
     *
     * @param id the id of the resultado to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resultados/{id}")
    public ResponseEntity<Void> deleteResultado(@PathVariable Long id) {
        log.debug("REST request to delete Resultado : {}", id);
        resultadoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
