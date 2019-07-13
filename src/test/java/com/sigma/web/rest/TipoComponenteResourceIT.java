package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.TipoComponente;
import com.sigma.repository.TipoComponenteRepository;
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
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sigma.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link TipoComponenteResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class TipoComponenteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private TipoComponenteRepository tipoComponenteRepository;

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

    private MockMvc restTipoComponenteMockMvc;

    private TipoComponente tipoComponente;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipoComponenteResource tipoComponenteResource = new TipoComponenteResource(tipoComponenteRepository);
        this.restTipoComponenteMockMvc = MockMvcBuilders.standaloneSetup(tipoComponenteResource)
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
    public static TipoComponente createEntity(EntityManager em) {
        TipoComponente tipoComponente = new TipoComponente()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION);
        return tipoComponente;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoComponente createUpdatedEntity(EntityManager em) {
        TipoComponente tipoComponente = new TipoComponente()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);
        return tipoComponente;
    }

    @BeforeEach
    public void initTest() {
        tipoComponente = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoComponente() throws Exception {
        int databaseSizeBeforeCreate = tipoComponenteRepository.findAll().size();

        // Create the TipoComponente
        restTipoComponenteMockMvc.perform(post("/api/tipo-componentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoComponente)))
            .andExpect(status().isCreated());

        // Validate the TipoComponente in the database
        List<TipoComponente> tipoComponenteList = tipoComponenteRepository.findAll();
        assertThat(tipoComponenteList).hasSize(databaseSizeBeforeCreate + 1);
        TipoComponente testTipoComponente = tipoComponenteList.get(tipoComponenteList.size() - 1);
        assertThat(testTipoComponente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoComponente.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createTipoComponenteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoComponenteRepository.findAll().size();

        // Create the TipoComponente with an existing ID
        tipoComponente.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoComponenteMockMvc.perform(post("/api/tipo-componentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoComponente)))
            .andExpect(status().isBadRequest());

        // Validate the TipoComponente in the database
        List<TipoComponente> tipoComponenteList = tipoComponenteRepository.findAll();
        assertThat(tipoComponenteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoComponenteRepository.findAll().size();
        // set the field null
        tipoComponente.setNombre(null);

        // Create the TipoComponente, which fails.

        restTipoComponenteMockMvc.perform(post("/api/tipo-componentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoComponente)))
            .andExpect(status().isBadRequest());

        List<TipoComponente> tipoComponenteList = tipoComponenteRepository.findAll();
        assertThat(tipoComponenteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoComponentes() throws Exception {
        // Initialize the database
        tipoComponenteRepository.saveAndFlush(tipoComponente);

        // Get all the tipoComponenteList
        restTipoComponenteMockMvc.perform(get("/api/tipo-componentes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoComponente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }
    
    @Test
    @Transactional
    public void getTipoComponente() throws Exception {
        // Initialize the database
        tipoComponenteRepository.saveAndFlush(tipoComponente);

        // Get the tipoComponente
        restTipoComponenteMockMvc.perform(get("/api/tipo-componentes/{id}", tipoComponente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoComponente.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTipoComponente() throws Exception {
        // Get the tipoComponente
        restTipoComponenteMockMvc.perform(get("/api/tipo-componentes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoComponente() throws Exception {
        // Initialize the database
        tipoComponenteRepository.saveAndFlush(tipoComponente);

        int databaseSizeBeforeUpdate = tipoComponenteRepository.findAll().size();

        // Update the tipoComponente
        TipoComponente updatedTipoComponente = tipoComponenteRepository.findById(tipoComponente.getId()).get();
        // Disconnect from session so that the updates on updatedTipoComponente are not directly saved in db
        em.detach(updatedTipoComponente);
        updatedTipoComponente
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);

        restTipoComponenteMockMvc.perform(put("/api/tipo-componentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipoComponente)))
            .andExpect(status().isOk());

        // Validate the TipoComponente in the database
        List<TipoComponente> tipoComponenteList = tipoComponenteRepository.findAll();
        assertThat(tipoComponenteList).hasSize(databaseSizeBeforeUpdate);
        TipoComponente testTipoComponente = tipoComponenteList.get(tipoComponenteList.size() - 1);
        assertThat(testTipoComponente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoComponente.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoComponente() throws Exception {
        int databaseSizeBeforeUpdate = tipoComponenteRepository.findAll().size();

        // Create the TipoComponente

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoComponenteMockMvc.perform(put("/api/tipo-componentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoComponente)))
            .andExpect(status().isBadRequest());

        // Validate the TipoComponente in the database
        List<TipoComponente> tipoComponenteList = tipoComponenteRepository.findAll();
        assertThat(tipoComponenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTipoComponente() throws Exception {
        // Initialize the database
        tipoComponenteRepository.saveAndFlush(tipoComponente);

        int databaseSizeBeforeDelete = tipoComponenteRepository.findAll().size();

        // Delete the tipoComponente
        restTipoComponenteMockMvc.perform(delete("/api/tipo-componentes/{id}", tipoComponente.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoComponente> tipoComponenteList = tipoComponenteRepository.findAll();
        assertThat(tipoComponenteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoComponente.class);
        TipoComponente tipoComponente1 = new TipoComponente();
        tipoComponente1.setId(1L);
        TipoComponente tipoComponente2 = new TipoComponente();
        tipoComponente2.setId(tipoComponente1.getId());
        assertThat(tipoComponente1).isEqualTo(tipoComponente2);
        tipoComponente2.setId(2L);
        assertThat(tipoComponente1).isNotEqualTo(tipoComponente2);
        tipoComponente1.setId(null);
        assertThat(tipoComponente1).isNotEqualTo(tipoComponente2);
    }
}
