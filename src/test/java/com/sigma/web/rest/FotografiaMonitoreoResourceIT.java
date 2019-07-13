package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.FotografiaMonitoreo;
import com.sigma.repository.FotografiaMonitoreoRepository;
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
 * Integration tests for the {@Link FotografiaMonitoreoResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class FotografiaMonitoreoResourceIT {

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
    private FotografiaMonitoreoRepository fotografiaMonitoreoRepository;

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

    private MockMvc restFotografiaMonitoreoMockMvc;

    private FotografiaMonitoreo fotografiaMonitoreo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FotografiaMonitoreoResource fotografiaMonitoreoResource = new FotografiaMonitoreoResource(fotografiaMonitoreoRepository);
        this.restFotografiaMonitoreoMockMvc = MockMvcBuilders.standaloneSetup(fotografiaMonitoreoResource)
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
    public static FotografiaMonitoreo createEntity(EntityManager em) {
        FotografiaMonitoreo fotografiaMonitoreo = new FotografiaMonitoreo()
            .nombre(DEFAULT_NOMBRE)
            .ruta(DEFAULT_RUTA)
            .extension(DEFAULT_EXTENSION)
            .fotografia(DEFAULT_FOTOGRAFIA)
            .fotografiaContentType(DEFAULT_FOTOGRAFIA_CONTENT_TYPE);
        return fotografiaMonitoreo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FotografiaMonitoreo createUpdatedEntity(EntityManager em) {
        FotografiaMonitoreo fotografiaMonitoreo = new FotografiaMonitoreo()
            .nombre(UPDATED_NOMBRE)
            .ruta(UPDATED_RUTA)
            .extension(UPDATED_EXTENSION)
            .fotografia(UPDATED_FOTOGRAFIA)
            .fotografiaContentType(UPDATED_FOTOGRAFIA_CONTENT_TYPE);
        return fotografiaMonitoreo;
    }

    @BeforeEach
    public void initTest() {
        fotografiaMonitoreo = createEntity(em);
    }

    @Test
    @Transactional
    public void createFotografiaMonitoreo() throws Exception {
        int databaseSizeBeforeCreate = fotografiaMonitoreoRepository.findAll().size();

        // Create the FotografiaMonitoreo
        restFotografiaMonitoreoMockMvc.perform(post("/api/fotografia-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fotografiaMonitoreo)))
            .andExpect(status().isCreated());

        // Validate the FotografiaMonitoreo in the database
        List<FotografiaMonitoreo> fotografiaMonitoreoList = fotografiaMonitoreoRepository.findAll();
        assertThat(fotografiaMonitoreoList).hasSize(databaseSizeBeforeCreate + 1);
        FotografiaMonitoreo testFotografiaMonitoreo = fotografiaMonitoreoList.get(fotografiaMonitoreoList.size() - 1);
        assertThat(testFotografiaMonitoreo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testFotografiaMonitoreo.getRuta()).isEqualTo(DEFAULT_RUTA);
        assertThat(testFotografiaMonitoreo.getExtension()).isEqualTo(DEFAULT_EXTENSION);
        assertThat(testFotografiaMonitoreo.getFotografia()).isEqualTo(DEFAULT_FOTOGRAFIA);
        assertThat(testFotografiaMonitoreo.getFotografiaContentType()).isEqualTo(DEFAULT_FOTOGRAFIA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createFotografiaMonitoreoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fotografiaMonitoreoRepository.findAll().size();

        // Create the FotografiaMonitoreo with an existing ID
        fotografiaMonitoreo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFotografiaMonitoreoMockMvc.perform(post("/api/fotografia-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fotografiaMonitoreo)))
            .andExpect(status().isBadRequest());

        // Validate the FotografiaMonitoreo in the database
        List<FotografiaMonitoreo> fotografiaMonitoreoList = fotografiaMonitoreoRepository.findAll();
        assertThat(fotografiaMonitoreoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = fotografiaMonitoreoRepository.findAll().size();
        // set the field null
        fotografiaMonitoreo.setNombre(null);

        // Create the FotografiaMonitoreo, which fails.

        restFotografiaMonitoreoMockMvc.perform(post("/api/fotografia-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fotografiaMonitoreo)))
            .andExpect(status().isBadRequest());

        List<FotografiaMonitoreo> fotografiaMonitoreoList = fotografiaMonitoreoRepository.findAll();
        assertThat(fotografiaMonitoreoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFotografiaMonitoreos() throws Exception {
        // Initialize the database
        fotografiaMonitoreoRepository.saveAndFlush(fotografiaMonitoreo);

        // Get all the fotografiaMonitoreoList
        restFotografiaMonitoreoMockMvc.perform(get("/api/fotografia-monitoreos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fotografiaMonitoreo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].ruta").value(hasItem(DEFAULT_RUTA.toString())))
            .andExpect(jsonPath("$.[*].extension").value(hasItem(DEFAULT_EXTENSION.toString())))
            .andExpect(jsonPath("$.[*].fotografiaContentType").value(hasItem(DEFAULT_FOTOGRAFIA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fotografia").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTOGRAFIA))));
    }
    
    @Test
    @Transactional
    public void getFotografiaMonitoreo() throws Exception {
        // Initialize the database
        fotografiaMonitoreoRepository.saveAndFlush(fotografiaMonitoreo);

        // Get the fotografiaMonitoreo
        restFotografiaMonitoreoMockMvc.perform(get("/api/fotografia-monitoreos/{id}", fotografiaMonitoreo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fotografiaMonitoreo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.ruta").value(DEFAULT_RUTA.toString()))
            .andExpect(jsonPath("$.extension").value(DEFAULT_EXTENSION.toString()))
            .andExpect(jsonPath("$.fotografiaContentType").value(DEFAULT_FOTOGRAFIA_CONTENT_TYPE))
            .andExpect(jsonPath("$.fotografia").value(Base64Utils.encodeToString(DEFAULT_FOTOGRAFIA)));
    }

    @Test
    @Transactional
    public void getNonExistingFotografiaMonitoreo() throws Exception {
        // Get the fotografiaMonitoreo
        restFotografiaMonitoreoMockMvc.perform(get("/api/fotografia-monitoreos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFotografiaMonitoreo() throws Exception {
        // Initialize the database
        fotografiaMonitoreoRepository.saveAndFlush(fotografiaMonitoreo);

        int databaseSizeBeforeUpdate = fotografiaMonitoreoRepository.findAll().size();

        // Update the fotografiaMonitoreo
        FotografiaMonitoreo updatedFotografiaMonitoreo = fotografiaMonitoreoRepository.findById(fotografiaMonitoreo.getId()).get();
        // Disconnect from session so that the updates on updatedFotografiaMonitoreo are not directly saved in db
        em.detach(updatedFotografiaMonitoreo);
        updatedFotografiaMonitoreo
            .nombre(UPDATED_NOMBRE)
            .ruta(UPDATED_RUTA)
            .extension(UPDATED_EXTENSION)
            .fotografia(UPDATED_FOTOGRAFIA)
            .fotografiaContentType(UPDATED_FOTOGRAFIA_CONTENT_TYPE);

        restFotografiaMonitoreoMockMvc.perform(put("/api/fotografia-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFotografiaMonitoreo)))
            .andExpect(status().isOk());

        // Validate the FotografiaMonitoreo in the database
        List<FotografiaMonitoreo> fotografiaMonitoreoList = fotografiaMonitoreoRepository.findAll();
        assertThat(fotografiaMonitoreoList).hasSize(databaseSizeBeforeUpdate);
        FotografiaMonitoreo testFotografiaMonitoreo = fotografiaMonitoreoList.get(fotografiaMonitoreoList.size() - 1);
        assertThat(testFotografiaMonitoreo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFotografiaMonitoreo.getRuta()).isEqualTo(UPDATED_RUTA);
        assertThat(testFotografiaMonitoreo.getExtension()).isEqualTo(UPDATED_EXTENSION);
        assertThat(testFotografiaMonitoreo.getFotografia()).isEqualTo(UPDATED_FOTOGRAFIA);
        assertThat(testFotografiaMonitoreo.getFotografiaContentType()).isEqualTo(UPDATED_FOTOGRAFIA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingFotografiaMonitoreo() throws Exception {
        int databaseSizeBeforeUpdate = fotografiaMonitoreoRepository.findAll().size();

        // Create the FotografiaMonitoreo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFotografiaMonitoreoMockMvc.perform(put("/api/fotografia-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fotografiaMonitoreo)))
            .andExpect(status().isBadRequest());

        // Validate the FotografiaMonitoreo in the database
        List<FotografiaMonitoreo> fotografiaMonitoreoList = fotografiaMonitoreoRepository.findAll();
        assertThat(fotografiaMonitoreoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFotografiaMonitoreo() throws Exception {
        // Initialize the database
        fotografiaMonitoreoRepository.saveAndFlush(fotografiaMonitoreo);

        int databaseSizeBeforeDelete = fotografiaMonitoreoRepository.findAll().size();

        // Delete the fotografiaMonitoreo
        restFotografiaMonitoreoMockMvc.perform(delete("/api/fotografia-monitoreos/{id}", fotografiaMonitoreo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FotografiaMonitoreo> fotografiaMonitoreoList = fotografiaMonitoreoRepository.findAll();
        assertThat(fotografiaMonitoreoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FotografiaMonitoreo.class);
        FotografiaMonitoreo fotografiaMonitoreo1 = new FotografiaMonitoreo();
        fotografiaMonitoreo1.setId(1L);
        FotografiaMonitoreo fotografiaMonitoreo2 = new FotografiaMonitoreo();
        fotografiaMonitoreo2.setId(fotografiaMonitoreo1.getId());
        assertThat(fotografiaMonitoreo1).isEqualTo(fotografiaMonitoreo2);
        fotografiaMonitoreo2.setId(2L);
        assertThat(fotografiaMonitoreo1).isNotEqualTo(fotografiaMonitoreo2);
        fotografiaMonitoreo1.setId(null);
        assertThat(fotografiaMonitoreo1).isNotEqualTo(fotografiaMonitoreo2);
    }
}
