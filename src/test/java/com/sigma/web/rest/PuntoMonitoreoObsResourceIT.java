package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.PuntoMonitoreoObs;
import com.sigma.repository.PuntoMonitoreoObsRepository;
import com.sigma.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sigma.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link PuntoMonitoreoObsResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class PuntoMonitoreoObsResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    @Autowired
    private PuntoMonitoreoObsRepository puntoMonitoreoObsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPuntoMonitoreoObsMockMvc;

    private PuntoMonitoreoObs puntoMonitoreoObs;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PuntoMonitoreoObsResource puntoMonitoreoObsResource = new PuntoMonitoreoObsResource(puntoMonitoreoObsRepository);
        this.restPuntoMonitoreoObsMockMvc = MockMvcBuilders.standaloneSetup(puntoMonitoreoObsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PuntoMonitoreoObs createEntity(EntityManager em) {
        PuntoMonitoreoObs puntoMonitoreoObs = new PuntoMonitoreoObs()
            .codigo(DEFAULT_CODIGO)
            .descripcion(DEFAULT_DESCRIPCION)
            .comentario(DEFAULT_COMENTARIO)
            .observacion(DEFAULT_OBSERVACION);
        return puntoMonitoreoObs;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PuntoMonitoreoObs createUpdatedEntity(EntityManager em) {
        PuntoMonitoreoObs puntoMonitoreoObs = new PuntoMonitoreoObs()
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .comentario(UPDATED_COMENTARIO)
            .observacion(UPDATED_OBSERVACION);
        return puntoMonitoreoObs;
    }

    @BeforeEach
    public void initTest() {
        puntoMonitoreoObs = createEntity(em);
    }

    @Test
    @Transactional
    public void createPuntoMonitoreoObs() throws Exception {
        int databaseSizeBeforeCreate = puntoMonitoreoObsRepository.findAll().size();

        // Create the PuntoMonitoreoObs
        restPuntoMonitoreoObsMockMvc.perform(post("/api/punto-monitoreo-obs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(puntoMonitoreoObs)))
            .andExpect(status().isCreated());

        // Validate the PuntoMonitoreoObs in the database
        List<PuntoMonitoreoObs> puntoMonitoreoObsList = puntoMonitoreoObsRepository.findAll();
        assertThat(puntoMonitoreoObsList).hasSize(databaseSizeBeforeCreate + 1);
        PuntoMonitoreoObs testPuntoMonitoreoObs = puntoMonitoreoObsList.get(puntoMonitoreoObsList.size() - 1);
        assertThat(testPuntoMonitoreoObs.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testPuntoMonitoreoObs.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPuntoMonitoreoObs.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
        assertThat(testPuntoMonitoreoObs.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
    }

    @Test
    @Transactional
    public void createPuntoMonitoreoObsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = puntoMonitoreoObsRepository.findAll().size();

        // Create the PuntoMonitoreoObs with an existing ID
        puntoMonitoreoObs.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPuntoMonitoreoObsMockMvc.perform(post("/api/punto-monitoreo-obs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(puntoMonitoreoObs)))
            .andExpect(status().isBadRequest());

        // Validate the PuntoMonitoreoObs in the database
        List<PuntoMonitoreoObs> puntoMonitoreoObsList = puntoMonitoreoObsRepository.findAll();
        assertThat(puntoMonitoreoObsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = puntoMonitoreoObsRepository.findAll().size();
        // set the field null
        puntoMonitoreoObs.setCodigo(null);

        // Create the PuntoMonitoreoObs, which fails.

        restPuntoMonitoreoObsMockMvc.perform(post("/api/punto-monitoreo-obs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(puntoMonitoreoObs)))
            .andExpect(status().isBadRequest());

        List<PuntoMonitoreoObs> puntoMonitoreoObsList = puntoMonitoreoObsRepository.findAll();
        assertThat(puntoMonitoreoObsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPuntoMonitoreoObs() throws Exception {
        // Initialize the database
        puntoMonitoreoObsRepository.saveAndFlush(puntoMonitoreoObs);

        // Get all the puntoMonitoreoObsList
        restPuntoMonitoreoObsMockMvc.perform(get("/api/punto-monitoreo-obs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(puntoMonitoreoObs.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO.toString())))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION.toString())));
    }
    
    @Test
    @Transactional
    public void getPuntoMonitoreoObs() throws Exception {
        // Initialize the database
        puntoMonitoreoObsRepository.saveAndFlush(puntoMonitoreoObs);

        // Get the puntoMonitoreoObs
        restPuntoMonitoreoObsMockMvc.perform(get("/api/punto-monitoreo-obs/{id}", puntoMonitoreoObs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(puntoMonitoreoObs.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO.toString()))
            .andExpect(jsonPath("$.observacion").value(DEFAULT_OBSERVACION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPuntoMonitoreoObs() throws Exception {
        // Get the puntoMonitoreoObs
        restPuntoMonitoreoObsMockMvc.perform(get("/api/punto-monitoreo-obs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePuntoMonitoreoObs() throws Exception {
        // Initialize the database
        puntoMonitoreoObsRepository.saveAndFlush(puntoMonitoreoObs);

        int databaseSizeBeforeUpdate = puntoMonitoreoObsRepository.findAll().size();

        // Update the puntoMonitoreoObs
        PuntoMonitoreoObs updatedPuntoMonitoreoObs = puntoMonitoreoObsRepository.findById(puntoMonitoreoObs.getId()).get();
        // Disconnect from session so that the updates on updatedPuntoMonitoreoObs are not directly saved in db
        em.detach(updatedPuntoMonitoreoObs);
        updatedPuntoMonitoreoObs
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .comentario(UPDATED_COMENTARIO)
            .observacion(UPDATED_OBSERVACION);

        restPuntoMonitoreoObsMockMvc.perform(put("/api/punto-monitoreo-obs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPuntoMonitoreoObs)))
            .andExpect(status().isOk());

        // Validate the PuntoMonitoreoObs in the database
        List<PuntoMonitoreoObs> puntoMonitoreoObsList = puntoMonitoreoObsRepository.findAll();
        assertThat(puntoMonitoreoObsList).hasSize(databaseSizeBeforeUpdate);
        PuntoMonitoreoObs testPuntoMonitoreoObs = puntoMonitoreoObsList.get(puntoMonitoreoObsList.size() - 1);
        assertThat(testPuntoMonitoreoObs.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testPuntoMonitoreoObs.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPuntoMonitoreoObs.getComentario()).isEqualTo(UPDATED_COMENTARIO);
        assertThat(testPuntoMonitoreoObs.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    public void updateNonExistingPuntoMonitoreoObs() throws Exception {
        int databaseSizeBeforeUpdate = puntoMonitoreoObsRepository.findAll().size();

        // Create the PuntoMonitoreoObs

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPuntoMonitoreoObsMockMvc.perform(put("/api/punto-monitoreo-obs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(puntoMonitoreoObs)))
            .andExpect(status().isBadRequest());

        // Validate the PuntoMonitoreoObs in the database
        List<PuntoMonitoreoObs> puntoMonitoreoObsList = puntoMonitoreoObsRepository.findAll();
        assertThat(puntoMonitoreoObsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePuntoMonitoreoObs() throws Exception {
        // Initialize the database
        puntoMonitoreoObsRepository.saveAndFlush(puntoMonitoreoObs);

        int databaseSizeBeforeDelete = puntoMonitoreoObsRepository.findAll().size();

        // Delete the puntoMonitoreoObs
        restPuntoMonitoreoObsMockMvc.perform(delete("/api/punto-monitoreo-obs/{id}", puntoMonitoreoObs.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PuntoMonitoreoObs> puntoMonitoreoObsList = puntoMonitoreoObsRepository.findAll();
        assertThat(puntoMonitoreoObsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PuntoMonitoreoObs.class);
        PuntoMonitoreoObs puntoMonitoreoObs1 = new PuntoMonitoreoObs();
        puntoMonitoreoObs1.setId(1L);
        PuntoMonitoreoObs puntoMonitoreoObs2 = new PuntoMonitoreoObs();
        puntoMonitoreoObs2.setId(puntoMonitoreoObs1.getId());
        assertThat(puntoMonitoreoObs1).isEqualTo(puntoMonitoreoObs2);
        puntoMonitoreoObs2.setId(2L);
        assertThat(puntoMonitoreoObs1).isNotEqualTo(puntoMonitoreoObs2);
        puntoMonitoreoObs1.setId(null);
        assertThat(puntoMonitoreoObs1).isNotEqualTo(puntoMonitoreoObs2);
    }
}
