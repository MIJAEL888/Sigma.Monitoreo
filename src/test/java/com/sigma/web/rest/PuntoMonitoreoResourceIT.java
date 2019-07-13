package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.PuntoMonitoreo;
import com.sigma.repository.PuntoMonitoreoRepository;
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
 * Integration tests for the {@Link PuntoMonitoreoResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class PuntoMonitoreoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_SEDE = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_SEDE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_CLIENTE = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_CLIENTE = "BBBBBBBBBB";

    private static final String DEFAULT_COORDENADA_NORTE = "AAAAAAAAAA";
    private static final String UPDATED_COORDENADA_NORTE = "BBBBBBBBBB";

    private static final String DEFAULT_COORDENADA_ESTE = "AAAAAAAAAA";
    private static final String UPDATED_COORDENADA_ESTE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    private static final Float DEFAULT_LATITUD = 1F;
    private static final Float UPDATED_LATITUD = 2F;

    private static final Float DEFAULT_LONGITUD = 1F;
    private static final Float UPDATED_LONGITUD = 2F;

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    @Autowired
    private PuntoMonitoreoRepository puntoMonitoreoRepository;

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

    private MockMvc restPuntoMonitoreoMockMvc;

    private PuntoMonitoreo puntoMonitoreo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PuntoMonitoreoResource puntoMonitoreoResource = new PuntoMonitoreoResource(puntoMonitoreoRepository);
        this.restPuntoMonitoreoMockMvc = MockMvcBuilders.standaloneSetup(puntoMonitoreoResource)
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
    public static PuntoMonitoreo createEntity(EntityManager em) {
        PuntoMonitoreo puntoMonitoreo = new PuntoMonitoreo()
            .codigo(DEFAULT_CODIGO)
            .codigoSede(DEFAULT_CODIGO_SEDE)
            .codigoCliente(DEFAULT_CODIGO_CLIENTE)
            .coordenadaNorte(DEFAULT_COORDENADA_NORTE)
            .coordenadaEste(DEFAULT_COORDENADA_ESTE)
            .descripcion(DEFAULT_DESCRIPCION)
            .comentario(DEFAULT_COMENTARIO)
            .latitud(DEFAULT_LATITUD)
            .longitud(DEFAULT_LONGITUD)
            .observacion(DEFAULT_OBSERVACION);
        return puntoMonitoreo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PuntoMonitoreo createUpdatedEntity(EntityManager em) {
        PuntoMonitoreo puntoMonitoreo = new PuntoMonitoreo()
            .codigo(UPDATED_CODIGO)
            .codigoSede(UPDATED_CODIGO_SEDE)
            .codigoCliente(UPDATED_CODIGO_CLIENTE)
            .coordenadaNorte(UPDATED_COORDENADA_NORTE)
            .coordenadaEste(UPDATED_COORDENADA_ESTE)
            .descripcion(UPDATED_DESCRIPCION)
            .comentario(UPDATED_COMENTARIO)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .observacion(UPDATED_OBSERVACION);
        return puntoMonitoreo;
    }

    @BeforeEach
    public void initTest() {
        puntoMonitoreo = createEntity(em);
    }

    @Test
    @Transactional
    public void createPuntoMonitoreo() throws Exception {
        int databaseSizeBeforeCreate = puntoMonitoreoRepository.findAll().size();

        // Create the PuntoMonitoreo
        restPuntoMonitoreoMockMvc.perform(post("/api/punto-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(puntoMonitoreo)))
            .andExpect(status().isCreated());

        // Validate the PuntoMonitoreo in the database
        List<PuntoMonitoreo> puntoMonitoreoList = puntoMonitoreoRepository.findAll();
        assertThat(puntoMonitoreoList).hasSize(databaseSizeBeforeCreate + 1);
        PuntoMonitoreo testPuntoMonitoreo = puntoMonitoreoList.get(puntoMonitoreoList.size() - 1);
        assertThat(testPuntoMonitoreo.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testPuntoMonitoreo.getCodigoSede()).isEqualTo(DEFAULT_CODIGO_SEDE);
        assertThat(testPuntoMonitoreo.getCodigoCliente()).isEqualTo(DEFAULT_CODIGO_CLIENTE);
        assertThat(testPuntoMonitoreo.getCoordenadaNorte()).isEqualTo(DEFAULT_COORDENADA_NORTE);
        assertThat(testPuntoMonitoreo.getCoordenadaEste()).isEqualTo(DEFAULT_COORDENADA_ESTE);
        assertThat(testPuntoMonitoreo.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPuntoMonitoreo.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
        assertThat(testPuntoMonitoreo.getLatitud()).isEqualTo(DEFAULT_LATITUD);
        assertThat(testPuntoMonitoreo.getLongitud()).isEqualTo(DEFAULT_LONGITUD);
        assertThat(testPuntoMonitoreo.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
    }

    @Test
    @Transactional
    public void createPuntoMonitoreoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = puntoMonitoreoRepository.findAll().size();

        // Create the PuntoMonitoreo with an existing ID
        puntoMonitoreo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPuntoMonitoreoMockMvc.perform(post("/api/punto-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(puntoMonitoreo)))
            .andExpect(status().isBadRequest());

        // Validate the PuntoMonitoreo in the database
        List<PuntoMonitoreo> puntoMonitoreoList = puntoMonitoreoRepository.findAll();
        assertThat(puntoMonitoreoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = puntoMonitoreoRepository.findAll().size();
        // set the field null
        puntoMonitoreo.setCodigo(null);

        // Create the PuntoMonitoreo, which fails.

        restPuntoMonitoreoMockMvc.perform(post("/api/punto-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(puntoMonitoreo)))
            .andExpect(status().isBadRequest());

        List<PuntoMonitoreo> puntoMonitoreoList = puntoMonitoreoRepository.findAll();
        assertThat(puntoMonitoreoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodigoSedeIsRequired() throws Exception {
        int databaseSizeBeforeTest = puntoMonitoreoRepository.findAll().size();
        // set the field null
        puntoMonitoreo.setCodigoSede(null);

        // Create the PuntoMonitoreo, which fails.

        restPuntoMonitoreoMockMvc.perform(post("/api/punto-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(puntoMonitoreo)))
            .andExpect(status().isBadRequest());

        List<PuntoMonitoreo> puntoMonitoreoList = puntoMonitoreoRepository.findAll();
        assertThat(puntoMonitoreoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodigoClienteIsRequired() throws Exception {
        int databaseSizeBeforeTest = puntoMonitoreoRepository.findAll().size();
        // set the field null
        puntoMonitoreo.setCodigoCliente(null);

        // Create the PuntoMonitoreo, which fails.

        restPuntoMonitoreoMockMvc.perform(post("/api/punto-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(puntoMonitoreo)))
            .andExpect(status().isBadRequest());

        List<PuntoMonitoreo> puntoMonitoreoList = puntoMonitoreoRepository.findAll();
        assertThat(puntoMonitoreoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCoordenadaNorteIsRequired() throws Exception {
        int databaseSizeBeforeTest = puntoMonitoreoRepository.findAll().size();
        // set the field null
        puntoMonitoreo.setCoordenadaNorte(null);

        // Create the PuntoMonitoreo, which fails.

        restPuntoMonitoreoMockMvc.perform(post("/api/punto-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(puntoMonitoreo)))
            .andExpect(status().isBadRequest());

        List<PuntoMonitoreo> puntoMonitoreoList = puntoMonitoreoRepository.findAll();
        assertThat(puntoMonitoreoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCoordenadaEsteIsRequired() throws Exception {
        int databaseSizeBeforeTest = puntoMonitoreoRepository.findAll().size();
        // set the field null
        puntoMonitoreo.setCoordenadaEste(null);

        // Create the PuntoMonitoreo, which fails.

        restPuntoMonitoreoMockMvc.perform(post("/api/punto-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(puntoMonitoreo)))
            .andExpect(status().isBadRequest());

        List<PuntoMonitoreo> puntoMonitoreoList = puntoMonitoreoRepository.findAll();
        assertThat(puntoMonitoreoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPuntoMonitoreos() throws Exception {
        // Initialize the database
        puntoMonitoreoRepository.saveAndFlush(puntoMonitoreo);

        // Get all the puntoMonitoreoList
        restPuntoMonitoreoMockMvc.perform(get("/api/punto-monitoreos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(puntoMonitoreo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
            .andExpect(jsonPath("$.[*].codigoSede").value(hasItem(DEFAULT_CODIGO_SEDE.toString())))
            .andExpect(jsonPath("$.[*].codigoCliente").value(hasItem(DEFAULT_CODIGO_CLIENTE.toString())))
            .andExpect(jsonPath("$.[*].coordenadaNorte").value(hasItem(DEFAULT_COORDENADA_NORTE.toString())))
            .andExpect(jsonPath("$.[*].coordenadaEste").value(hasItem(DEFAULT_COORDENADA_ESTE.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO.toString())))
            .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD.doubleValue())))
            .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD.doubleValue())))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION.toString())));
    }
    
    @Test
    @Transactional
    public void getPuntoMonitoreo() throws Exception {
        // Initialize the database
        puntoMonitoreoRepository.saveAndFlush(puntoMonitoreo);

        // Get the puntoMonitoreo
        restPuntoMonitoreoMockMvc.perform(get("/api/punto-monitoreos/{id}", puntoMonitoreo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(puntoMonitoreo.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.codigoSede").value(DEFAULT_CODIGO_SEDE.toString()))
            .andExpect(jsonPath("$.codigoCliente").value(DEFAULT_CODIGO_CLIENTE.toString()))
            .andExpect(jsonPath("$.coordenadaNorte").value(DEFAULT_COORDENADA_NORTE.toString()))
            .andExpect(jsonPath("$.coordenadaEste").value(DEFAULT_COORDENADA_ESTE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO.toString()))
            .andExpect(jsonPath("$.latitud").value(DEFAULT_LATITUD.doubleValue()))
            .andExpect(jsonPath("$.longitud").value(DEFAULT_LONGITUD.doubleValue()))
            .andExpect(jsonPath("$.observacion").value(DEFAULT_OBSERVACION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPuntoMonitoreo() throws Exception {
        // Get the puntoMonitoreo
        restPuntoMonitoreoMockMvc.perform(get("/api/punto-monitoreos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePuntoMonitoreo() throws Exception {
        // Initialize the database
        puntoMonitoreoRepository.saveAndFlush(puntoMonitoreo);

        int databaseSizeBeforeUpdate = puntoMonitoreoRepository.findAll().size();

        // Update the puntoMonitoreo
        PuntoMonitoreo updatedPuntoMonitoreo = puntoMonitoreoRepository.findById(puntoMonitoreo.getId()).get();
        // Disconnect from session so that the updates on updatedPuntoMonitoreo are not directly saved in db
        em.detach(updatedPuntoMonitoreo);
        updatedPuntoMonitoreo
            .codigo(UPDATED_CODIGO)
            .codigoSede(UPDATED_CODIGO_SEDE)
            .codigoCliente(UPDATED_CODIGO_CLIENTE)
            .coordenadaNorte(UPDATED_COORDENADA_NORTE)
            .coordenadaEste(UPDATED_COORDENADA_ESTE)
            .descripcion(UPDATED_DESCRIPCION)
            .comentario(UPDATED_COMENTARIO)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .observacion(UPDATED_OBSERVACION);

        restPuntoMonitoreoMockMvc.perform(put("/api/punto-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPuntoMonitoreo)))
            .andExpect(status().isOk());

        // Validate the PuntoMonitoreo in the database
        List<PuntoMonitoreo> puntoMonitoreoList = puntoMonitoreoRepository.findAll();
        assertThat(puntoMonitoreoList).hasSize(databaseSizeBeforeUpdate);
        PuntoMonitoreo testPuntoMonitoreo = puntoMonitoreoList.get(puntoMonitoreoList.size() - 1);
        assertThat(testPuntoMonitoreo.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testPuntoMonitoreo.getCodigoSede()).isEqualTo(UPDATED_CODIGO_SEDE);
        assertThat(testPuntoMonitoreo.getCodigoCliente()).isEqualTo(UPDATED_CODIGO_CLIENTE);
        assertThat(testPuntoMonitoreo.getCoordenadaNorte()).isEqualTo(UPDATED_COORDENADA_NORTE);
        assertThat(testPuntoMonitoreo.getCoordenadaEste()).isEqualTo(UPDATED_COORDENADA_ESTE);
        assertThat(testPuntoMonitoreo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPuntoMonitoreo.getComentario()).isEqualTo(UPDATED_COMENTARIO);
        assertThat(testPuntoMonitoreo.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testPuntoMonitoreo.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testPuntoMonitoreo.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
    }

    @Test
    @Transactional
    public void updateNonExistingPuntoMonitoreo() throws Exception {
        int databaseSizeBeforeUpdate = puntoMonitoreoRepository.findAll().size();

        // Create the PuntoMonitoreo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPuntoMonitoreoMockMvc.perform(put("/api/punto-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(puntoMonitoreo)))
            .andExpect(status().isBadRequest());

        // Validate the PuntoMonitoreo in the database
        List<PuntoMonitoreo> puntoMonitoreoList = puntoMonitoreoRepository.findAll();
        assertThat(puntoMonitoreoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePuntoMonitoreo() throws Exception {
        // Initialize the database
        puntoMonitoreoRepository.saveAndFlush(puntoMonitoreo);

        int databaseSizeBeforeDelete = puntoMonitoreoRepository.findAll().size();

        // Delete the puntoMonitoreo
        restPuntoMonitoreoMockMvc.perform(delete("/api/punto-monitoreos/{id}", puntoMonitoreo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PuntoMonitoreo> puntoMonitoreoList = puntoMonitoreoRepository.findAll();
        assertThat(puntoMonitoreoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PuntoMonitoreo.class);
        PuntoMonitoreo puntoMonitoreo1 = new PuntoMonitoreo();
        puntoMonitoreo1.setId(1L);
        PuntoMonitoreo puntoMonitoreo2 = new PuntoMonitoreo();
        puntoMonitoreo2.setId(puntoMonitoreo1.getId());
        assertThat(puntoMonitoreo1).isEqualTo(puntoMonitoreo2);
        puntoMonitoreo2.setId(2L);
        assertThat(puntoMonitoreo1).isNotEqualTo(puntoMonitoreo2);
        puntoMonitoreo1.setId(null);
        assertThat(puntoMonitoreo1).isNotEqualTo(puntoMonitoreo2);
    }
}
