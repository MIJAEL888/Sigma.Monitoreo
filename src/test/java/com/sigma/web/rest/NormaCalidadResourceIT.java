package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.NormaCalidad;
import com.sigma.repository.NormaCalidadRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.sigma.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sigma.domain.enumeration.TipoNorma;
/**
 * Integration tests for the {@Link NormaCalidadResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class NormaCalidadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_PUBLICACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_PUBLICACION = LocalDate.now(ZoneId.systemDefault());

    private static final TipoNorma DEFAULT_TIPO = TipoNorma.NACIONAL;
    private static final TipoNorma UPDATED_TIPO = TipoNorma.INTERNACIONAL;

    private static final String DEFAULT_FUENTE = "AAAAAAAAAA";
    private static final String UPDATED_FUENTE = "BBBBBBBBBB";

    private static final String DEFAULT_RUTA_DOC_NORMA = "AAAAAAAAAA";
    private static final String UPDATED_RUTA_DOC_NORMA = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_DOC_NORMA = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_DOC_NORMA = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DOCUMENTO_NORMA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENTO_NORMA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOCUMENTO_NORMA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENTO_NORMA_CONTENT_TYPE = "image/png";

    @Autowired
    private NormaCalidadRepository normaCalidadRepository;

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

    private MockMvc restNormaCalidadMockMvc;

    private NormaCalidad normaCalidad;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NormaCalidadResource normaCalidadResource = new NormaCalidadResource(normaCalidadRepository);
        this.restNormaCalidadMockMvc = MockMvcBuilders.standaloneSetup(normaCalidadResource)
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
    public static NormaCalidad createEntity(EntityManager em) {
        NormaCalidad normaCalidad = new NormaCalidad()
            .nombre(DEFAULT_NOMBRE)
            .codigo(DEFAULT_CODIGO)
            .fechaPublicacion(DEFAULT_FECHA_PUBLICACION)
            .tipo(DEFAULT_TIPO)
            .fuente(DEFAULT_FUENTE)
            .rutaDocNorma(DEFAULT_RUTA_DOC_NORMA)
            .nombreDocNorma(DEFAULT_NOMBRE_DOC_NORMA)
            .documentoNorma(DEFAULT_DOCUMENTO_NORMA)
            .documentoNormaContentType(DEFAULT_DOCUMENTO_NORMA_CONTENT_TYPE);
        return normaCalidad;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NormaCalidad createUpdatedEntity(EntityManager em) {
        NormaCalidad normaCalidad = new NormaCalidad()
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .fechaPublicacion(UPDATED_FECHA_PUBLICACION)
            .tipo(UPDATED_TIPO)
            .fuente(UPDATED_FUENTE)
            .rutaDocNorma(UPDATED_RUTA_DOC_NORMA)
            .nombreDocNorma(UPDATED_NOMBRE_DOC_NORMA)
            .documentoNorma(UPDATED_DOCUMENTO_NORMA)
            .documentoNormaContentType(UPDATED_DOCUMENTO_NORMA_CONTENT_TYPE);
        return normaCalidad;
    }

    @BeforeEach
    public void initTest() {
        normaCalidad = createEntity(em);
    }

    @Test
    @Transactional
    public void createNormaCalidad() throws Exception {
        int databaseSizeBeforeCreate = normaCalidadRepository.findAll().size();

        // Create the NormaCalidad
        restNormaCalidadMockMvc.perform(post("/api/norma-calidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(normaCalidad)))
            .andExpect(status().isCreated());

        // Validate the NormaCalidad in the database
        List<NormaCalidad> normaCalidadList = normaCalidadRepository.findAll();
        assertThat(normaCalidadList).hasSize(databaseSizeBeforeCreate + 1);
        NormaCalidad testNormaCalidad = normaCalidadList.get(normaCalidadList.size() - 1);
        assertThat(testNormaCalidad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testNormaCalidad.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testNormaCalidad.getFechaPublicacion()).isEqualTo(DEFAULT_FECHA_PUBLICACION);
        assertThat(testNormaCalidad.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testNormaCalidad.getFuente()).isEqualTo(DEFAULT_FUENTE);
        assertThat(testNormaCalidad.getRutaDocNorma()).isEqualTo(DEFAULT_RUTA_DOC_NORMA);
        assertThat(testNormaCalidad.getNombreDocNorma()).isEqualTo(DEFAULT_NOMBRE_DOC_NORMA);
        assertThat(testNormaCalidad.getDocumentoNorma()).isEqualTo(DEFAULT_DOCUMENTO_NORMA);
        assertThat(testNormaCalidad.getDocumentoNormaContentType()).isEqualTo(DEFAULT_DOCUMENTO_NORMA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createNormaCalidadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = normaCalidadRepository.findAll().size();

        // Create the NormaCalidad with an existing ID
        normaCalidad.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNormaCalidadMockMvc.perform(post("/api/norma-calidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(normaCalidad)))
            .andExpect(status().isBadRequest());

        // Validate the NormaCalidad in the database
        List<NormaCalidad> normaCalidadList = normaCalidadRepository.findAll();
        assertThat(normaCalidadList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = normaCalidadRepository.findAll().size();
        // set the field null
        normaCalidad.setNombre(null);

        // Create the NormaCalidad, which fails.

        restNormaCalidadMockMvc.perform(post("/api/norma-calidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(normaCalidad)))
            .andExpect(status().isBadRequest());

        List<NormaCalidad> normaCalidadList = normaCalidadRepository.findAll();
        assertThat(normaCalidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNormaCalidads() throws Exception {
        // Initialize the database
        normaCalidadRepository.saveAndFlush(normaCalidad);

        // Get all the normaCalidadList
        restNormaCalidadMockMvc.perform(get("/api/norma-calidads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(normaCalidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
            .andExpect(jsonPath("$.[*].fechaPublicacion").value(hasItem(DEFAULT_FECHA_PUBLICACION.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].fuente").value(hasItem(DEFAULT_FUENTE.toString())))
            .andExpect(jsonPath("$.[*].rutaDocNorma").value(hasItem(DEFAULT_RUTA_DOC_NORMA.toString())))
            .andExpect(jsonPath("$.[*].nombreDocNorma").value(hasItem(DEFAULT_NOMBRE_DOC_NORMA.toString())))
            .andExpect(jsonPath("$.[*].documentoNormaContentType").value(hasItem(DEFAULT_DOCUMENTO_NORMA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentoNorma").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENTO_NORMA))));
    }
    
    @Test
    @Transactional
    public void getNormaCalidad() throws Exception {
        // Initialize the database
        normaCalidadRepository.saveAndFlush(normaCalidad);

        // Get the normaCalidad
        restNormaCalidadMockMvc.perform(get("/api/norma-calidads/{id}", normaCalidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(normaCalidad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.fechaPublicacion").value(DEFAULT_FECHA_PUBLICACION.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.fuente").value(DEFAULT_FUENTE.toString()))
            .andExpect(jsonPath("$.rutaDocNorma").value(DEFAULT_RUTA_DOC_NORMA.toString()))
            .andExpect(jsonPath("$.nombreDocNorma").value(DEFAULT_NOMBRE_DOC_NORMA.toString()))
            .andExpect(jsonPath("$.documentoNormaContentType").value(DEFAULT_DOCUMENTO_NORMA_CONTENT_TYPE))
            .andExpect(jsonPath("$.documentoNorma").value(Base64Utils.encodeToString(DEFAULT_DOCUMENTO_NORMA)));
    }

    @Test
    @Transactional
    public void getNonExistingNormaCalidad() throws Exception {
        // Get the normaCalidad
        restNormaCalidadMockMvc.perform(get("/api/norma-calidads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNormaCalidad() throws Exception {
        // Initialize the database
        normaCalidadRepository.saveAndFlush(normaCalidad);

        int databaseSizeBeforeUpdate = normaCalidadRepository.findAll().size();

        // Update the normaCalidad
        NormaCalidad updatedNormaCalidad = normaCalidadRepository.findById(normaCalidad.getId()).get();
        // Disconnect from session so that the updates on updatedNormaCalidad are not directly saved in db
        em.detach(updatedNormaCalidad);
        updatedNormaCalidad
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .fechaPublicacion(UPDATED_FECHA_PUBLICACION)
            .tipo(UPDATED_TIPO)
            .fuente(UPDATED_FUENTE)
            .rutaDocNorma(UPDATED_RUTA_DOC_NORMA)
            .nombreDocNorma(UPDATED_NOMBRE_DOC_NORMA)
            .documentoNorma(UPDATED_DOCUMENTO_NORMA)
            .documentoNormaContentType(UPDATED_DOCUMENTO_NORMA_CONTENT_TYPE);

        restNormaCalidadMockMvc.perform(put("/api/norma-calidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNormaCalidad)))
            .andExpect(status().isOk());

        // Validate the NormaCalidad in the database
        List<NormaCalidad> normaCalidadList = normaCalidadRepository.findAll();
        assertThat(normaCalidadList).hasSize(databaseSizeBeforeUpdate);
        NormaCalidad testNormaCalidad = normaCalidadList.get(normaCalidadList.size() - 1);
        assertThat(testNormaCalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testNormaCalidad.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testNormaCalidad.getFechaPublicacion()).isEqualTo(UPDATED_FECHA_PUBLICACION);
        assertThat(testNormaCalidad.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testNormaCalidad.getFuente()).isEqualTo(UPDATED_FUENTE);
        assertThat(testNormaCalidad.getRutaDocNorma()).isEqualTo(UPDATED_RUTA_DOC_NORMA);
        assertThat(testNormaCalidad.getNombreDocNorma()).isEqualTo(UPDATED_NOMBRE_DOC_NORMA);
        assertThat(testNormaCalidad.getDocumentoNorma()).isEqualTo(UPDATED_DOCUMENTO_NORMA);
        assertThat(testNormaCalidad.getDocumentoNormaContentType()).isEqualTo(UPDATED_DOCUMENTO_NORMA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingNormaCalidad() throws Exception {
        int databaseSizeBeforeUpdate = normaCalidadRepository.findAll().size();

        // Create the NormaCalidad

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNormaCalidadMockMvc.perform(put("/api/norma-calidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(normaCalidad)))
            .andExpect(status().isBadRequest());

        // Validate the NormaCalidad in the database
        List<NormaCalidad> normaCalidadList = normaCalidadRepository.findAll();
        assertThat(normaCalidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNormaCalidad() throws Exception {
        // Initialize the database
        normaCalidadRepository.saveAndFlush(normaCalidad);

        int databaseSizeBeforeDelete = normaCalidadRepository.findAll().size();

        // Delete the normaCalidad
        restNormaCalidadMockMvc.perform(delete("/api/norma-calidads/{id}", normaCalidad.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NormaCalidad> normaCalidadList = normaCalidadRepository.findAll();
        assertThat(normaCalidadList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NormaCalidad.class);
        NormaCalidad normaCalidad1 = new NormaCalidad();
        normaCalidad1.setId(1L);
        NormaCalidad normaCalidad2 = new NormaCalidad();
        normaCalidad2.setId(normaCalidad1.getId());
        assertThat(normaCalidad1).isEqualTo(normaCalidad2);
        normaCalidad2.setId(2L);
        assertThat(normaCalidad1).isNotEqualTo(normaCalidad2);
        normaCalidad1.setId(null);
        assertThat(normaCalidad1).isNotEqualTo(normaCalidad2);
    }
}
