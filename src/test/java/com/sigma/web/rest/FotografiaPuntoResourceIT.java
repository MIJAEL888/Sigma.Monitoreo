package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.FotografiaPunto;
import com.sigma.repository.FotografiaPuntoRepository;
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
 * Integration tests for the {@Link FotografiaPuntoResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class FotografiaPuntoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_RUTA = "AAAAAAAAAA";
    private static final String UPDATED_RUTA = "BBBBBBBBBB";

    private static final String DEFAULT_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_EXTENSION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTOGRAFIA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTOGRAFIA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTOGRAFIA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTOGRAFIA_CONTENT_TYPE = "image/png";

    @Autowired
    private FotografiaPuntoRepository fotografiaPuntoRepository;

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

    private MockMvc restFotografiaPuntoMockMvc;

    private FotografiaPunto fotografiaPunto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FotografiaPuntoResource fotografiaPuntoResource = new FotografiaPuntoResource(fotografiaPuntoRepository);
        this.restFotografiaPuntoMockMvc = MockMvcBuilders.standaloneSetup(fotografiaPuntoResource)
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
    public static FotografiaPunto createEntity(EntityManager em) {
        FotografiaPunto fotografiaPunto = new FotografiaPunto()
            .nombre(DEFAULT_NOMBRE)
            .ruta(DEFAULT_RUTA)
            .extension(DEFAULT_EXTENSION)
            .fotografia(DEFAULT_FOTOGRAFIA)
            .fotografiaContentType(DEFAULT_FOTOGRAFIA_CONTENT_TYPE);
        return fotografiaPunto;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FotografiaPunto createUpdatedEntity(EntityManager em) {
        FotografiaPunto fotografiaPunto = new FotografiaPunto()
            .nombre(UPDATED_NOMBRE)
            .ruta(UPDATED_RUTA)
            .extension(UPDATED_EXTENSION)
            .fotografia(UPDATED_FOTOGRAFIA)
            .fotografiaContentType(UPDATED_FOTOGRAFIA_CONTENT_TYPE);
        return fotografiaPunto;
    }

    @BeforeEach
    public void initTest() {
        fotografiaPunto = createEntity(em);
    }

    @Test
    @Transactional
    public void createFotografiaPunto() throws Exception {
        int databaseSizeBeforeCreate = fotografiaPuntoRepository.findAll().size();

        // Create the FotografiaPunto
        restFotografiaPuntoMockMvc.perform(post("/api/fotografia-puntos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fotografiaPunto)))
            .andExpect(status().isCreated());

        // Validate the FotografiaPunto in the database
        List<FotografiaPunto> fotografiaPuntoList = fotografiaPuntoRepository.findAll();
        assertThat(fotografiaPuntoList).hasSize(databaseSizeBeforeCreate + 1);
        FotografiaPunto testFotografiaPunto = fotografiaPuntoList.get(fotografiaPuntoList.size() - 1);
        assertThat(testFotografiaPunto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testFotografiaPunto.getRuta()).isEqualTo(DEFAULT_RUTA);
        assertThat(testFotografiaPunto.getExtension()).isEqualTo(DEFAULT_EXTENSION);
        assertThat(testFotografiaPunto.getFotografia()).isEqualTo(DEFAULT_FOTOGRAFIA);
        assertThat(testFotografiaPunto.getFotografiaContentType()).isEqualTo(DEFAULT_FOTOGRAFIA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createFotografiaPuntoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fotografiaPuntoRepository.findAll().size();

        // Create the FotografiaPunto with an existing ID
        fotografiaPunto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFotografiaPuntoMockMvc.perform(post("/api/fotografia-puntos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fotografiaPunto)))
            .andExpect(status().isBadRequest());

        // Validate the FotografiaPunto in the database
        List<FotografiaPunto> fotografiaPuntoList = fotografiaPuntoRepository.findAll();
        assertThat(fotografiaPuntoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = fotografiaPuntoRepository.findAll().size();
        // set the field null
        fotografiaPunto.setNombre(null);

        // Create the FotografiaPunto, which fails.

        restFotografiaPuntoMockMvc.perform(post("/api/fotografia-puntos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fotografiaPunto)))
            .andExpect(status().isBadRequest());

        List<FotografiaPunto> fotografiaPuntoList = fotografiaPuntoRepository.findAll();
        assertThat(fotografiaPuntoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFotografiaPuntos() throws Exception {
        // Initialize the database
        fotografiaPuntoRepository.saveAndFlush(fotografiaPunto);

        // Get all the fotografiaPuntoList
        restFotografiaPuntoMockMvc.perform(get("/api/fotografia-puntos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fotografiaPunto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].ruta").value(hasItem(DEFAULT_RUTA.toString())))
            .andExpect(jsonPath("$.[*].extension").value(hasItem(DEFAULT_EXTENSION.toString())))
            .andExpect(jsonPath("$.[*].fotografiaContentType").value(hasItem(DEFAULT_FOTOGRAFIA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fotografia").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTOGRAFIA))));
    }
    
    @Test
    @Transactional
    public void getFotografiaPunto() throws Exception {
        // Initialize the database
        fotografiaPuntoRepository.saveAndFlush(fotografiaPunto);

        // Get the fotografiaPunto
        restFotografiaPuntoMockMvc.perform(get("/api/fotografia-puntos/{id}", fotografiaPunto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fotografiaPunto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.ruta").value(DEFAULT_RUTA.toString()))
            .andExpect(jsonPath("$.extension").value(DEFAULT_EXTENSION.toString()))
            .andExpect(jsonPath("$.fotografiaContentType").value(DEFAULT_FOTOGRAFIA_CONTENT_TYPE))
            .andExpect(jsonPath("$.fotografia").value(Base64Utils.encodeToString(DEFAULT_FOTOGRAFIA)));
    }

    @Test
    @Transactional
    public void getNonExistingFotografiaPunto() throws Exception {
        // Get the fotografiaPunto
        restFotografiaPuntoMockMvc.perform(get("/api/fotografia-puntos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFotografiaPunto() throws Exception {
        // Initialize the database
        fotografiaPuntoRepository.saveAndFlush(fotografiaPunto);

        int databaseSizeBeforeUpdate = fotografiaPuntoRepository.findAll().size();

        // Update the fotografiaPunto
        FotografiaPunto updatedFotografiaPunto = fotografiaPuntoRepository.findById(fotografiaPunto.getId()).get();
        // Disconnect from session so that the updates on updatedFotografiaPunto are not directly saved in db
        em.detach(updatedFotografiaPunto);
        updatedFotografiaPunto
            .nombre(UPDATED_NOMBRE)
            .ruta(UPDATED_RUTA)
            .extension(UPDATED_EXTENSION)
            .fotografia(UPDATED_FOTOGRAFIA)
            .fotografiaContentType(UPDATED_FOTOGRAFIA_CONTENT_TYPE);

        restFotografiaPuntoMockMvc.perform(put("/api/fotografia-puntos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFotografiaPunto)))
            .andExpect(status().isOk());

        // Validate the FotografiaPunto in the database
        List<FotografiaPunto> fotografiaPuntoList = fotografiaPuntoRepository.findAll();
        assertThat(fotografiaPuntoList).hasSize(databaseSizeBeforeUpdate);
        FotografiaPunto testFotografiaPunto = fotografiaPuntoList.get(fotografiaPuntoList.size() - 1);
        assertThat(testFotografiaPunto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFotografiaPunto.getRuta()).isEqualTo(UPDATED_RUTA);
        assertThat(testFotografiaPunto.getExtension()).isEqualTo(UPDATED_EXTENSION);
        assertThat(testFotografiaPunto.getFotografia()).isEqualTo(UPDATED_FOTOGRAFIA);
        assertThat(testFotografiaPunto.getFotografiaContentType()).isEqualTo(UPDATED_FOTOGRAFIA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingFotografiaPunto() throws Exception {
        int databaseSizeBeforeUpdate = fotografiaPuntoRepository.findAll().size();

        // Create the FotografiaPunto

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFotografiaPuntoMockMvc.perform(put("/api/fotografia-puntos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fotografiaPunto)))
            .andExpect(status().isBadRequest());

        // Validate the FotografiaPunto in the database
        List<FotografiaPunto> fotografiaPuntoList = fotografiaPuntoRepository.findAll();
        assertThat(fotografiaPuntoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFotografiaPunto() throws Exception {
        // Initialize the database
        fotografiaPuntoRepository.saveAndFlush(fotografiaPunto);

        int databaseSizeBeforeDelete = fotografiaPuntoRepository.findAll().size();

        // Delete the fotografiaPunto
        restFotografiaPuntoMockMvc.perform(delete("/api/fotografia-puntos/{id}", fotografiaPunto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FotografiaPunto> fotografiaPuntoList = fotografiaPuntoRepository.findAll();
        assertThat(fotografiaPuntoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FotografiaPunto.class);
        FotografiaPunto fotografiaPunto1 = new FotografiaPunto();
        fotografiaPunto1.setId(1L);
        FotografiaPunto fotografiaPunto2 = new FotografiaPunto();
        fotografiaPunto2.setId(fotografiaPunto1.getId());
        assertThat(fotografiaPunto1).isEqualTo(fotografiaPunto2);
        fotografiaPunto2.setId(2L);
        assertThat(fotografiaPunto1).isNotEqualTo(fotografiaPunto2);
        fotografiaPunto1.setId(null);
        assertThat(fotografiaPunto1).isNotEqualTo(fotografiaPunto2);
    }
}
