package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.Paramentro;
import com.sigma.repository.ParamentroRepository;
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
 * Integration tests for the {@Link ParamentroResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class ParamentroResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLAS = "AAAAAAAAAA";
    private static final String UPDATED_SIGLAS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Float DEFAULT_COSTO = 1F;
    private static final Float UPDATED_COSTO = 2F;

    private static final String DEFAULT_METODOLOGIA = "AAAAAAAAAA";
    private static final String UPDATED_METODOLOGIA = "BBBBBBBBBB";

    private static final String DEFAULT_METODO_ENSAYO = "AAAAAAAAAA";
    private static final String UPDATED_METODO_ENSAYO = "BBBBBBBBBB";

    private static final Integer DEFAULT_LIMITE_CUANTIFICACION = 1;
    private static final Integer UPDATED_LIMITE_CUANTIFICACION = 2;

    @Autowired
    private ParamentroRepository paramentroRepository;

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

    private MockMvc restParamentroMockMvc;

    private Paramentro paramentro;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParamentroResource paramentroResource = new ParamentroResource(paramentroRepository);
        this.restParamentroMockMvc = MockMvcBuilders.standaloneSetup(paramentroResource)
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
    public static Paramentro createEntity(EntityManager em) {
        Paramentro paramentro = new Paramentro()
            .nombre(DEFAULT_NOMBRE)
            .siglas(DEFAULT_SIGLAS)
            .descripcion(DEFAULT_DESCRIPCION)
            .costo(DEFAULT_COSTO)
            .metodologia(DEFAULT_METODOLOGIA)
            .metodoEnsayo(DEFAULT_METODO_ENSAYO)
            .limiteCuantificacion(DEFAULT_LIMITE_CUANTIFICACION);
        return paramentro;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paramentro createUpdatedEntity(EntityManager em) {
        Paramentro paramentro = new Paramentro()
            .nombre(UPDATED_NOMBRE)
            .siglas(UPDATED_SIGLAS)
            .descripcion(UPDATED_DESCRIPCION)
            .costo(UPDATED_COSTO)
            .metodologia(UPDATED_METODOLOGIA)
            .metodoEnsayo(UPDATED_METODO_ENSAYO)
            .limiteCuantificacion(UPDATED_LIMITE_CUANTIFICACION);
        return paramentro;
    }

    @BeforeEach
    public void initTest() {
        paramentro = createEntity(em);
    }

    @Test
    @Transactional
    public void createParamentro() throws Exception {
        int databaseSizeBeforeCreate = paramentroRepository.findAll().size();

        // Create the Paramentro
        restParamentroMockMvc.perform(post("/api/paramentros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paramentro)))
            .andExpect(status().isCreated());

        // Validate the Paramentro in the database
        List<Paramentro> paramentroList = paramentroRepository.findAll();
        assertThat(paramentroList).hasSize(databaseSizeBeforeCreate + 1);
        Paramentro testParamentro = paramentroList.get(paramentroList.size() - 1);
        assertThat(testParamentro.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testParamentro.getSiglas()).isEqualTo(DEFAULT_SIGLAS);
        assertThat(testParamentro.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testParamentro.getCosto()).isEqualTo(DEFAULT_COSTO);
        assertThat(testParamentro.getMetodologia()).isEqualTo(DEFAULT_METODOLOGIA);
        assertThat(testParamentro.getMetodoEnsayo()).isEqualTo(DEFAULT_METODO_ENSAYO);
        assertThat(testParamentro.getLimiteCuantificacion()).isEqualTo(DEFAULT_LIMITE_CUANTIFICACION);
    }

    @Test
    @Transactional
    public void createParamentroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paramentroRepository.findAll().size();

        // Create the Paramentro with an existing ID
        paramentro.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParamentroMockMvc.perform(post("/api/paramentros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paramentro)))
            .andExpect(status().isBadRequest());

        // Validate the Paramentro in the database
        List<Paramentro> paramentroList = paramentroRepository.findAll();
        assertThat(paramentroList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = paramentroRepository.findAll().size();
        // set the field null
        paramentro.setNombre(null);

        // Create the Paramentro, which fails.

        restParamentroMockMvc.perform(post("/api/paramentros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paramentro)))
            .andExpect(status().isBadRequest());

        List<Paramentro> paramentroList = paramentroRepository.findAll();
        assertThat(paramentroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSiglasIsRequired() throws Exception {
        int databaseSizeBeforeTest = paramentroRepository.findAll().size();
        // set the field null
        paramentro.setSiglas(null);

        // Create the Paramentro, which fails.

        restParamentroMockMvc.perform(post("/api/paramentros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paramentro)))
            .andExpect(status().isBadRequest());

        List<Paramentro> paramentroList = paramentroRepository.findAll();
        assertThat(paramentroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParamentros() throws Exception {
        // Initialize the database
        paramentroRepository.saveAndFlush(paramentro);

        // Get all the paramentroList
        restParamentroMockMvc.perform(get("/api/paramentros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paramentro.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].siglas").value(hasItem(DEFAULT_SIGLAS.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].costo").value(hasItem(DEFAULT_COSTO.doubleValue())))
            .andExpect(jsonPath("$.[*].metodologia").value(hasItem(DEFAULT_METODOLOGIA.toString())))
            .andExpect(jsonPath("$.[*].metodoEnsayo").value(hasItem(DEFAULT_METODO_ENSAYO.toString())))
            .andExpect(jsonPath("$.[*].limiteCuantificacion").value(hasItem(DEFAULT_LIMITE_CUANTIFICACION)));
    }
    
    @Test
    @Transactional
    public void getParamentro() throws Exception {
        // Initialize the database
        paramentroRepository.saveAndFlush(paramentro);

        // Get the paramentro
        restParamentroMockMvc.perform(get("/api/paramentros/{id}", paramentro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paramentro.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.siglas").value(DEFAULT_SIGLAS.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.costo").value(DEFAULT_COSTO.doubleValue()))
            .andExpect(jsonPath("$.metodologia").value(DEFAULT_METODOLOGIA.toString()))
            .andExpect(jsonPath("$.metodoEnsayo").value(DEFAULT_METODO_ENSAYO.toString()))
            .andExpect(jsonPath("$.limiteCuantificacion").value(DEFAULT_LIMITE_CUANTIFICACION));
    }

    @Test
    @Transactional
    public void getNonExistingParamentro() throws Exception {
        // Get the paramentro
        restParamentroMockMvc.perform(get("/api/paramentros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParamentro() throws Exception {
        // Initialize the database
        paramentroRepository.saveAndFlush(paramentro);

        int databaseSizeBeforeUpdate = paramentroRepository.findAll().size();

        // Update the paramentro
        Paramentro updatedParamentro = paramentroRepository.findById(paramentro.getId()).get();
        // Disconnect from session so that the updates on updatedParamentro are not directly saved in db
        em.detach(updatedParamentro);
        updatedParamentro
            .nombre(UPDATED_NOMBRE)
            .siglas(UPDATED_SIGLAS)
            .descripcion(UPDATED_DESCRIPCION)
            .costo(UPDATED_COSTO)
            .metodologia(UPDATED_METODOLOGIA)
            .metodoEnsayo(UPDATED_METODO_ENSAYO)
            .limiteCuantificacion(UPDATED_LIMITE_CUANTIFICACION);

        restParamentroMockMvc.perform(put("/api/paramentros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParamentro)))
            .andExpect(status().isOk());

        // Validate the Paramentro in the database
        List<Paramentro> paramentroList = paramentroRepository.findAll();
        assertThat(paramentroList).hasSize(databaseSizeBeforeUpdate);
        Paramentro testParamentro = paramentroList.get(paramentroList.size() - 1);
        assertThat(testParamentro.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testParamentro.getSiglas()).isEqualTo(UPDATED_SIGLAS);
        assertThat(testParamentro.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testParamentro.getCosto()).isEqualTo(UPDATED_COSTO);
        assertThat(testParamentro.getMetodologia()).isEqualTo(UPDATED_METODOLOGIA);
        assertThat(testParamentro.getMetodoEnsayo()).isEqualTo(UPDATED_METODO_ENSAYO);
        assertThat(testParamentro.getLimiteCuantificacion()).isEqualTo(UPDATED_LIMITE_CUANTIFICACION);
    }

    @Test
    @Transactional
    public void updateNonExistingParamentro() throws Exception {
        int databaseSizeBeforeUpdate = paramentroRepository.findAll().size();

        // Create the Paramentro

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParamentroMockMvc.perform(put("/api/paramentros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paramentro)))
            .andExpect(status().isBadRequest());

        // Validate the Paramentro in the database
        List<Paramentro> paramentroList = paramentroRepository.findAll();
        assertThat(paramentroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParamentro() throws Exception {
        // Initialize the database
        paramentroRepository.saveAndFlush(paramentro);

        int databaseSizeBeforeDelete = paramentroRepository.findAll().size();

        // Delete the paramentro
        restParamentroMockMvc.perform(delete("/api/paramentros/{id}", paramentro.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paramentro> paramentroList = paramentroRepository.findAll();
        assertThat(paramentroList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paramentro.class);
        Paramentro paramentro1 = new Paramentro();
        paramentro1.setId(1L);
        Paramentro paramentro2 = new Paramentro();
        paramentro2.setId(paramentro1.getId());
        assertThat(paramentro1).isEqualTo(paramentro2);
        paramentro2.setId(2L);
        assertThat(paramentro1).isNotEqualTo(paramentro2);
        paramentro1.setId(null);
        assertThat(paramentro1).isNotEqualTo(paramentro2);
    }
}
