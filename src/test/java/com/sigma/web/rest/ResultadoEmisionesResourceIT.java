package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.ResultadoEmisiones;
import com.sigma.repository.ResultadoEmisionesRepository;
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
 * Integration tests for the {@Link ResultadoEmisionesResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class ResultadoEmisionesResourceIT {

    private static final String DEFAULT_TIPO_EQUIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_EQUIPO = "BBBBBBBBBB";

    private static final String DEFAULT_COMBUSTIBLE = "AAAAAAAAAA";
    private static final String UPDATED_COMBUSTIBLE = "BBBBBBBBBB";

    private static final Float DEFAULT_CONSUMO = 1F;
    private static final Float UPDATED_CONSUMO = 2F;

    private static final Float DEFAULT_HORA_POR_MES = 1F;
    private static final Float UPDATED_HORA_POR_MES = 2F;

    private static final Float DEFAULT_ALTURA = 1F;
    private static final Float UPDATED_ALTURA = 2F;

    private static final Float DEFAULT_DIAMETRO = 1F;
    private static final Float UPDATED_DIAMETRO = 2F;

    private static final String DEFAULT_SECCION = "AAAAAAAAAA";
    private static final String UPDATED_SECCION = "BBBBBBBBBB";

    @Autowired
    private ResultadoEmisionesRepository resultadoEmisionesRepository;

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

    private MockMvc restResultadoEmisionesMockMvc;

    private ResultadoEmisiones resultadoEmisiones;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResultadoEmisionesResource resultadoEmisionesResource = new ResultadoEmisionesResource(resultadoEmisionesRepository);
        this.restResultadoEmisionesMockMvc = MockMvcBuilders.standaloneSetup(resultadoEmisionesResource)
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
    public static ResultadoEmisiones createEntity(EntityManager em) {
        ResultadoEmisiones resultadoEmisiones = new ResultadoEmisiones()
            .tipoEquipo(DEFAULT_TIPO_EQUIPO)
            .combustible(DEFAULT_COMBUSTIBLE)
            .consumo(DEFAULT_CONSUMO)
            .horaPorMes(DEFAULT_HORA_POR_MES)
            .altura(DEFAULT_ALTURA)
            .diametro(DEFAULT_DIAMETRO)
            .seccion(DEFAULT_SECCION);
        return resultadoEmisiones;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultadoEmisiones createUpdatedEntity(EntityManager em) {
        ResultadoEmisiones resultadoEmisiones = new ResultadoEmisiones()
            .tipoEquipo(UPDATED_TIPO_EQUIPO)
            .combustible(UPDATED_COMBUSTIBLE)
            .consumo(UPDATED_CONSUMO)
            .horaPorMes(UPDATED_HORA_POR_MES)
            .altura(UPDATED_ALTURA)
            .diametro(UPDATED_DIAMETRO)
            .seccion(UPDATED_SECCION);
        return resultadoEmisiones;
    }

    @BeforeEach
    public void initTest() {
        resultadoEmisiones = createEntity(em);
    }

    @Test
    @Transactional
    public void createResultadoEmisiones() throws Exception {
        int databaseSizeBeforeCreate = resultadoEmisionesRepository.findAll().size();

        // Create the ResultadoEmisiones
        restResultadoEmisionesMockMvc.perform(post("/api/resultado-emisiones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resultadoEmisiones)))
            .andExpect(status().isCreated());

        // Validate the ResultadoEmisiones in the database
        List<ResultadoEmisiones> resultadoEmisionesList = resultadoEmisionesRepository.findAll();
        assertThat(resultadoEmisionesList).hasSize(databaseSizeBeforeCreate + 1);
        ResultadoEmisiones testResultadoEmisiones = resultadoEmisionesList.get(resultadoEmisionesList.size() - 1);
        assertThat(testResultadoEmisiones.getTipoEquipo()).isEqualTo(DEFAULT_TIPO_EQUIPO);
        assertThat(testResultadoEmisiones.getCombustible()).isEqualTo(DEFAULT_COMBUSTIBLE);
        assertThat(testResultadoEmisiones.getConsumo()).isEqualTo(DEFAULT_CONSUMO);
        assertThat(testResultadoEmisiones.getHoraPorMes()).isEqualTo(DEFAULT_HORA_POR_MES);
        assertThat(testResultadoEmisiones.getAltura()).isEqualTo(DEFAULT_ALTURA);
        assertThat(testResultadoEmisiones.getDiametro()).isEqualTo(DEFAULT_DIAMETRO);
        assertThat(testResultadoEmisiones.getSeccion()).isEqualTo(DEFAULT_SECCION);
    }

    @Test
    @Transactional
    public void createResultadoEmisionesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resultadoEmisionesRepository.findAll().size();

        // Create the ResultadoEmisiones with an existing ID
        resultadoEmisiones.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultadoEmisionesMockMvc.perform(post("/api/resultado-emisiones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resultadoEmisiones)))
            .andExpect(status().isBadRequest());

        // Validate the ResultadoEmisiones in the database
        List<ResultadoEmisiones> resultadoEmisionesList = resultadoEmisionesRepository.findAll();
        assertThat(resultadoEmisionesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllResultadoEmisiones() throws Exception {
        // Initialize the database
        resultadoEmisionesRepository.saveAndFlush(resultadoEmisiones);

        // Get all the resultadoEmisionesList
        restResultadoEmisionesMockMvc.perform(get("/api/resultado-emisiones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultadoEmisiones.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoEquipo").value(hasItem(DEFAULT_TIPO_EQUIPO.toString())))
            .andExpect(jsonPath("$.[*].combustible").value(hasItem(DEFAULT_COMBUSTIBLE.toString())))
            .andExpect(jsonPath("$.[*].consumo").value(hasItem(DEFAULT_CONSUMO.doubleValue())))
            .andExpect(jsonPath("$.[*].horaPorMes").value(hasItem(DEFAULT_HORA_POR_MES.doubleValue())))
            .andExpect(jsonPath("$.[*].altura").value(hasItem(DEFAULT_ALTURA.doubleValue())))
            .andExpect(jsonPath("$.[*].diametro").value(hasItem(DEFAULT_DIAMETRO.doubleValue())))
            .andExpect(jsonPath("$.[*].seccion").value(hasItem(DEFAULT_SECCION.toString())));
    }
    
    @Test
    @Transactional
    public void getResultadoEmisiones() throws Exception {
        // Initialize the database
        resultadoEmisionesRepository.saveAndFlush(resultadoEmisiones);

        // Get the resultadoEmisiones
        restResultadoEmisionesMockMvc.perform(get("/api/resultado-emisiones/{id}", resultadoEmisiones.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resultadoEmisiones.getId().intValue()))
            .andExpect(jsonPath("$.tipoEquipo").value(DEFAULT_TIPO_EQUIPO.toString()))
            .andExpect(jsonPath("$.combustible").value(DEFAULT_COMBUSTIBLE.toString()))
            .andExpect(jsonPath("$.consumo").value(DEFAULT_CONSUMO.doubleValue()))
            .andExpect(jsonPath("$.horaPorMes").value(DEFAULT_HORA_POR_MES.doubleValue()))
            .andExpect(jsonPath("$.altura").value(DEFAULT_ALTURA.doubleValue()))
            .andExpect(jsonPath("$.diametro").value(DEFAULT_DIAMETRO.doubleValue()))
            .andExpect(jsonPath("$.seccion").value(DEFAULT_SECCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResultadoEmisiones() throws Exception {
        // Get the resultadoEmisiones
        restResultadoEmisionesMockMvc.perform(get("/api/resultado-emisiones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResultadoEmisiones() throws Exception {
        // Initialize the database
        resultadoEmisionesRepository.saveAndFlush(resultadoEmisiones);

        int databaseSizeBeforeUpdate = resultadoEmisionesRepository.findAll().size();

        // Update the resultadoEmisiones
        ResultadoEmisiones updatedResultadoEmisiones = resultadoEmisionesRepository.findById(resultadoEmisiones.getId()).get();
        // Disconnect from session so that the updates on updatedResultadoEmisiones are not directly saved in db
        em.detach(updatedResultadoEmisiones);
        updatedResultadoEmisiones
            .tipoEquipo(UPDATED_TIPO_EQUIPO)
            .combustible(UPDATED_COMBUSTIBLE)
            .consumo(UPDATED_CONSUMO)
            .horaPorMes(UPDATED_HORA_POR_MES)
            .altura(UPDATED_ALTURA)
            .diametro(UPDATED_DIAMETRO)
            .seccion(UPDATED_SECCION);

        restResultadoEmisionesMockMvc.perform(put("/api/resultado-emisiones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResultadoEmisiones)))
            .andExpect(status().isOk());

        // Validate the ResultadoEmisiones in the database
        List<ResultadoEmisiones> resultadoEmisionesList = resultadoEmisionesRepository.findAll();
        assertThat(resultadoEmisionesList).hasSize(databaseSizeBeforeUpdate);
        ResultadoEmisiones testResultadoEmisiones = resultadoEmisionesList.get(resultadoEmisionesList.size() - 1);
        assertThat(testResultadoEmisiones.getTipoEquipo()).isEqualTo(UPDATED_TIPO_EQUIPO);
        assertThat(testResultadoEmisiones.getCombustible()).isEqualTo(UPDATED_COMBUSTIBLE);
        assertThat(testResultadoEmisiones.getConsumo()).isEqualTo(UPDATED_CONSUMO);
        assertThat(testResultadoEmisiones.getHoraPorMes()).isEqualTo(UPDATED_HORA_POR_MES);
        assertThat(testResultadoEmisiones.getAltura()).isEqualTo(UPDATED_ALTURA);
        assertThat(testResultadoEmisiones.getDiametro()).isEqualTo(UPDATED_DIAMETRO);
        assertThat(testResultadoEmisiones.getSeccion()).isEqualTo(UPDATED_SECCION);
    }

    @Test
    @Transactional
    public void updateNonExistingResultadoEmisiones() throws Exception {
        int databaseSizeBeforeUpdate = resultadoEmisionesRepository.findAll().size();

        // Create the ResultadoEmisiones

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultadoEmisionesMockMvc.perform(put("/api/resultado-emisiones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resultadoEmisiones)))
            .andExpect(status().isBadRequest());

        // Validate the ResultadoEmisiones in the database
        List<ResultadoEmisiones> resultadoEmisionesList = resultadoEmisionesRepository.findAll();
        assertThat(resultadoEmisionesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteResultadoEmisiones() throws Exception {
        // Initialize the database
        resultadoEmisionesRepository.saveAndFlush(resultadoEmisiones);

        int databaseSizeBeforeDelete = resultadoEmisionesRepository.findAll().size();

        // Delete the resultadoEmisiones
        restResultadoEmisionesMockMvc.perform(delete("/api/resultado-emisiones/{id}", resultadoEmisiones.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResultadoEmisiones> resultadoEmisionesList = resultadoEmisionesRepository.findAll();
        assertThat(resultadoEmisionesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResultadoEmisiones.class);
        ResultadoEmisiones resultadoEmisiones1 = new ResultadoEmisiones();
        resultadoEmisiones1.setId(1L);
        ResultadoEmisiones resultadoEmisiones2 = new ResultadoEmisiones();
        resultadoEmisiones2.setId(resultadoEmisiones1.getId());
        assertThat(resultadoEmisiones1).isEqualTo(resultadoEmisiones2);
        resultadoEmisiones2.setId(2L);
        assertThat(resultadoEmisiones1).isNotEqualTo(resultadoEmisiones2);
        resultadoEmisiones1.setId(null);
        assertThat(resultadoEmisiones1).isNotEqualTo(resultadoEmisiones2);
    }
}
