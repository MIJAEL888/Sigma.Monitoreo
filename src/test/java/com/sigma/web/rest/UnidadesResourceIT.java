package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.Unidades;
import com.sigma.repository.UnidadesRepository;
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
 * Integration tests for the {@Link UnidadesResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class UnidadesResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private UnidadesRepository unidadesRepository;

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

    private MockMvc restUnidadesMockMvc;

    private Unidades unidades;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UnidadesResource unidadesResource = new UnidadesResource(unidadesRepository);
        this.restUnidadesMockMvc = MockMvcBuilders.standaloneSetup(unidadesResource)
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
    public static Unidades createEntity(EntityManager em) {
        Unidades unidades = new Unidades()
            .nombre(DEFAULT_NOMBRE)
            .codigo(DEFAULT_CODIGO)
            .descripcion(DEFAULT_DESCRIPCION);
        return unidades;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unidades createUpdatedEntity(EntityManager em) {
        Unidades unidades = new Unidades()
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION);
        return unidades;
    }

    @BeforeEach
    public void initTest() {
        unidades = createEntity(em);
    }

    @Test
    @Transactional
    public void createUnidades() throws Exception {
        int databaseSizeBeforeCreate = unidadesRepository.findAll().size();

        // Create the Unidades
        restUnidadesMockMvc.perform(post("/api/unidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidades)))
            .andExpect(status().isCreated());

        // Validate the Unidades in the database
        List<Unidades> unidadesList = unidadesRepository.findAll();
        assertThat(unidadesList).hasSize(databaseSizeBeforeCreate + 1);
        Unidades testUnidades = unidadesList.get(unidadesList.size() - 1);
        assertThat(testUnidades.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testUnidades.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testUnidades.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createUnidadesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = unidadesRepository.findAll().size();

        // Create the Unidades with an existing ID
        unidades.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnidadesMockMvc.perform(post("/api/unidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidades)))
            .andExpect(status().isBadRequest());

        // Validate the Unidades in the database
        List<Unidades> unidadesList = unidadesRepository.findAll();
        assertThat(unidadesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUnidades() throws Exception {
        // Initialize the database
        unidadesRepository.saveAndFlush(unidades);

        // Get all the unidadesList
        restUnidadesMockMvc.perform(get("/api/unidades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unidades.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }
    
    @Test
    @Transactional
    public void getUnidades() throws Exception {
        // Initialize the database
        unidadesRepository.saveAndFlush(unidades);

        // Get the unidades
        restUnidadesMockMvc.perform(get("/api/unidades/{id}", unidades.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(unidades.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUnidades() throws Exception {
        // Get the unidades
        restUnidadesMockMvc.perform(get("/api/unidades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnidades() throws Exception {
        // Initialize the database
        unidadesRepository.saveAndFlush(unidades);

        int databaseSizeBeforeUpdate = unidadesRepository.findAll().size();

        // Update the unidades
        Unidades updatedUnidades = unidadesRepository.findById(unidades.getId()).get();
        // Disconnect from session so that the updates on updatedUnidades are not directly saved in db
        em.detach(updatedUnidades);
        updatedUnidades
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION);

        restUnidadesMockMvc.perform(put("/api/unidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUnidades)))
            .andExpect(status().isOk());

        // Validate the Unidades in the database
        List<Unidades> unidadesList = unidadesRepository.findAll();
        assertThat(unidadesList).hasSize(databaseSizeBeforeUpdate);
        Unidades testUnidades = unidadesList.get(unidadesList.size() - 1);
        assertThat(testUnidades.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUnidades.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testUnidades.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingUnidades() throws Exception {
        int databaseSizeBeforeUpdate = unidadesRepository.findAll().size();

        // Create the Unidades

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnidadesMockMvc.perform(put("/api/unidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unidades)))
            .andExpect(status().isBadRequest());

        // Validate the Unidades in the database
        List<Unidades> unidadesList = unidadesRepository.findAll();
        assertThat(unidadesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUnidades() throws Exception {
        // Initialize the database
        unidadesRepository.saveAndFlush(unidades);

        int databaseSizeBeforeDelete = unidadesRepository.findAll().size();

        // Delete the unidades
        restUnidadesMockMvc.perform(delete("/api/unidades/{id}", unidades.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Unidades> unidadesList = unidadesRepository.findAll();
        assertThat(unidadesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Unidades.class);
        Unidades unidades1 = new Unidades();
        unidades1.setId(1L);
        Unidades unidades2 = new Unidades();
        unidades2.setId(unidades1.getId());
        assertThat(unidades1).isEqualTo(unidades2);
        unidades2.setId(2L);
        assertThat(unidades1).isNotEqualTo(unidades2);
        unidades1.setId(null);
        assertThat(unidades1).isNotEqualTo(unidades2);
    }
}
