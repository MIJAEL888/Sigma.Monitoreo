package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.Observacion;
import com.sigma.repository.ObservacionRepository;
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
 * Integration tests for the {@Link ObservacionResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class ObservacionResourceIT {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_MONITORISTA = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_MONITORISTA = "BBBBBBBBBB";

    @Autowired
    private ObservacionRepository observacionRepository;

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

    private MockMvc restObservacionMockMvc;

    private Observacion observacion;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ObservacionResource observacionResource = new ObservacionResource(observacionRepository);
        this.restObservacionMockMvc = MockMvcBuilders.standaloneSetup(observacionResource)
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
    public static Observacion createEntity(EntityManager em) {
        Observacion observacion = new Observacion()
            .descripcion(DEFAULT_DESCRIPCION)
            .comentario(DEFAULT_COMENTARIO)
            .codigoMonitorista(DEFAULT_CODIGO_MONITORISTA);
        return observacion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Observacion createUpdatedEntity(EntityManager em) {
        Observacion observacion = new Observacion()
            .descripcion(UPDATED_DESCRIPCION)
            .comentario(UPDATED_COMENTARIO)
            .codigoMonitorista(UPDATED_CODIGO_MONITORISTA);
        return observacion;
    }

    @BeforeEach
    public void initTest() {
        observacion = createEntity(em);
    }

    @Test
    @Transactional
    public void createObservacion() throws Exception {
        int databaseSizeBeforeCreate = observacionRepository.findAll().size();

        // Create the Observacion
        restObservacionMockMvc.perform(post("/api/observacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(observacion)))
            .andExpect(status().isCreated());

        // Validate the Observacion in the database
        List<Observacion> observacionList = observacionRepository.findAll();
        assertThat(observacionList).hasSize(databaseSizeBeforeCreate + 1);
        Observacion testObservacion = observacionList.get(observacionList.size() - 1);
        assertThat(testObservacion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testObservacion.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
        assertThat(testObservacion.getCodigoMonitorista()).isEqualTo(DEFAULT_CODIGO_MONITORISTA);
    }

    @Test
    @Transactional
    public void createObservacionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = observacionRepository.findAll().size();

        // Create the Observacion with an existing ID
        observacion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restObservacionMockMvc.perform(post("/api/observacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(observacion)))
            .andExpect(status().isBadRequest());

        // Validate the Observacion in the database
        List<Observacion> observacionList = observacionRepository.findAll();
        assertThat(observacionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllObservacions() throws Exception {
        // Initialize the database
        observacionRepository.saveAndFlush(observacion);

        // Get all the observacionList
        restObservacionMockMvc.perform(get("/api/observacions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(observacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO.toString())))
            .andExpect(jsonPath("$.[*].codigoMonitorista").value(hasItem(DEFAULT_CODIGO_MONITORISTA.toString())));
    }
    
    @Test
    @Transactional
    public void getObservacion() throws Exception {
        // Initialize the database
        observacionRepository.saveAndFlush(observacion);

        // Get the observacion
        restObservacionMockMvc.perform(get("/api/observacions/{id}", observacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(observacion.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO.toString()))
            .andExpect(jsonPath("$.codigoMonitorista").value(DEFAULT_CODIGO_MONITORISTA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingObservacion() throws Exception {
        // Get the observacion
        restObservacionMockMvc.perform(get("/api/observacions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateObservacion() throws Exception {
        // Initialize the database
        observacionRepository.saveAndFlush(observacion);

        int databaseSizeBeforeUpdate = observacionRepository.findAll().size();

        // Update the observacion
        Observacion updatedObservacion = observacionRepository.findById(observacion.getId()).get();
        // Disconnect from session so that the updates on updatedObservacion are not directly saved in db
        em.detach(updatedObservacion);
        updatedObservacion
            .descripcion(UPDATED_DESCRIPCION)
            .comentario(UPDATED_COMENTARIO)
            .codigoMonitorista(UPDATED_CODIGO_MONITORISTA);

        restObservacionMockMvc.perform(put("/api/observacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedObservacion)))
            .andExpect(status().isOk());

        // Validate the Observacion in the database
        List<Observacion> observacionList = observacionRepository.findAll();
        assertThat(observacionList).hasSize(databaseSizeBeforeUpdate);
        Observacion testObservacion = observacionList.get(observacionList.size() - 1);
        assertThat(testObservacion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testObservacion.getComentario()).isEqualTo(UPDATED_COMENTARIO);
        assertThat(testObservacion.getCodigoMonitorista()).isEqualTo(UPDATED_CODIGO_MONITORISTA);
    }

    @Test
    @Transactional
    public void updateNonExistingObservacion() throws Exception {
        int databaseSizeBeforeUpdate = observacionRepository.findAll().size();

        // Create the Observacion

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObservacionMockMvc.perform(put("/api/observacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(observacion)))
            .andExpect(status().isBadRequest());

        // Validate the Observacion in the database
        List<Observacion> observacionList = observacionRepository.findAll();
        assertThat(observacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteObservacion() throws Exception {
        // Initialize the database
        observacionRepository.saveAndFlush(observacion);

        int databaseSizeBeforeDelete = observacionRepository.findAll().size();

        // Delete the observacion
        restObservacionMockMvc.perform(delete("/api/observacions/{id}", observacion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Observacion> observacionList = observacionRepository.findAll();
        assertThat(observacionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Observacion.class);
        Observacion observacion1 = new Observacion();
        observacion1.setId(1L);
        Observacion observacion2 = new Observacion();
        observacion2.setId(observacion1.getId());
        assertThat(observacion1).isEqualTo(observacion2);
        observacion2.setId(2L);
        assertThat(observacion1).isNotEqualTo(observacion2);
        observacion1.setId(null);
        assertThat(observacion1).isNotEqualTo(observacion2);
    }
}
