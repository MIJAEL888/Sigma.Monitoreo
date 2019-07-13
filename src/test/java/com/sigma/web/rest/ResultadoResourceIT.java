package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.Resultado;
import com.sigma.repository.ResultadoRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.sigma.web.rest.TestUtil.sameInstant;
import static com.sigma.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ResultadoResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class ResultadoResourceIT {

    private static final ZonedDateTime DEFAULT_FECHA_INICIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_INICIO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FEHCA_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FEHCA_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_VALOR_MINIMO = "AAAAAAAAAA";
    private static final String UPDATED_VALOR_MINIMO = "BBBBBBBBBB";

    private static final String DEFAULT_VALOR_MAXIMO = "AAAAAAAAAA";
    private static final String UPDATED_VALOR_MAXIMO = "BBBBBBBBBB";

    private static final String DEFAULT_VALOR_FINAL = "AAAAAAAAAA";
    private static final String UPDATED_VALOR_FINAL = "BBBBBBBBBB";

    private static final Float DEFAULT_VALOR_FINAL_NUM = 1F;
    private static final Float UPDATED_VALOR_FINAL_NUM = 2F;

    private static final String DEFAULT_CODIGO_LABORATORIO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_LABORATORIO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_EQUIPO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_EQUIPO = "BBBBBBBBBB";

    @Autowired
    private ResultadoRepository resultadoRepository;

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

    private MockMvc restResultadoMockMvc;

    private Resultado resultado;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResultadoResource resultadoResource = new ResultadoResource(resultadoRepository);
        this.restResultadoMockMvc = MockMvcBuilders.standaloneSetup(resultadoResource)
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
    public static Resultado createEntity(EntityManager em) {
        Resultado resultado = new Resultado()
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fehcaFin(DEFAULT_FEHCA_FIN)
            .valorMinimo(DEFAULT_VALOR_MINIMO)
            .valorMaximo(DEFAULT_VALOR_MAXIMO)
            .valorFinal(DEFAULT_VALOR_FINAL)
            .valorFinalNum(DEFAULT_VALOR_FINAL_NUM)
            .codigoLaboratorio(DEFAULT_CODIGO_LABORATORIO)
            .codigoEquipo(DEFAULT_CODIGO_EQUIPO);
        return resultado;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resultado createUpdatedEntity(EntityManager em) {
        Resultado resultado = new Resultado()
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fehcaFin(UPDATED_FEHCA_FIN)
            .valorMinimo(UPDATED_VALOR_MINIMO)
            .valorMaximo(UPDATED_VALOR_MAXIMO)
            .valorFinal(UPDATED_VALOR_FINAL)
            .valorFinalNum(UPDATED_VALOR_FINAL_NUM)
            .codigoLaboratorio(UPDATED_CODIGO_LABORATORIO)
            .codigoEquipo(UPDATED_CODIGO_EQUIPO);
        return resultado;
    }

    @BeforeEach
    public void initTest() {
        resultado = createEntity(em);
    }

    @Test
    @Transactional
    public void createResultado() throws Exception {
        int databaseSizeBeforeCreate = resultadoRepository.findAll().size();

        // Create the Resultado
        restResultadoMockMvc.perform(post("/api/resultados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resultado)))
            .andExpect(status().isCreated());

        // Validate the Resultado in the database
        List<Resultado> resultadoList = resultadoRepository.findAll();
        assertThat(resultadoList).hasSize(databaseSizeBeforeCreate + 1);
        Resultado testResultado = resultadoList.get(resultadoList.size() - 1);
        assertThat(testResultado.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testResultado.getFehcaFin()).isEqualTo(DEFAULT_FEHCA_FIN);
        assertThat(testResultado.getValorMinimo()).isEqualTo(DEFAULT_VALOR_MINIMO);
        assertThat(testResultado.getValorMaximo()).isEqualTo(DEFAULT_VALOR_MAXIMO);
        assertThat(testResultado.getValorFinal()).isEqualTo(DEFAULT_VALOR_FINAL);
        assertThat(testResultado.getValorFinalNum()).isEqualTo(DEFAULT_VALOR_FINAL_NUM);
        assertThat(testResultado.getCodigoLaboratorio()).isEqualTo(DEFAULT_CODIGO_LABORATORIO);
        assertThat(testResultado.getCodigoEquipo()).isEqualTo(DEFAULT_CODIGO_EQUIPO);
    }

    @Test
    @Transactional
    public void createResultadoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resultadoRepository.findAll().size();

        // Create the Resultado with an existing ID
        resultado.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultadoMockMvc.perform(post("/api/resultados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resultado)))
            .andExpect(status().isBadRequest());

        // Validate the Resultado in the database
        List<Resultado> resultadoList = resultadoRepository.findAll();
        assertThat(resultadoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllResultados() throws Exception {
        // Initialize the database
        resultadoRepository.saveAndFlush(resultado);

        // Get all the resultadoList
        restResultadoMockMvc.perform(get("/api/resultados?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultado.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(sameInstant(DEFAULT_FECHA_INICIO))))
            .andExpect(jsonPath("$.[*].fehcaFin").value(hasItem(sameInstant(DEFAULT_FEHCA_FIN))))
            .andExpect(jsonPath("$.[*].valorMinimo").value(hasItem(DEFAULT_VALOR_MINIMO.toString())))
            .andExpect(jsonPath("$.[*].valorMaximo").value(hasItem(DEFAULT_VALOR_MAXIMO.toString())))
            .andExpect(jsonPath("$.[*].valorFinal").value(hasItem(DEFAULT_VALOR_FINAL.toString())))
            .andExpect(jsonPath("$.[*].valorFinalNum").value(hasItem(DEFAULT_VALOR_FINAL_NUM.doubleValue())))
            .andExpect(jsonPath("$.[*].codigoLaboratorio").value(hasItem(DEFAULT_CODIGO_LABORATORIO.toString())))
            .andExpect(jsonPath("$.[*].codigoEquipo").value(hasItem(DEFAULT_CODIGO_EQUIPO.toString())));
    }
    
    @Test
    @Transactional
    public void getResultado() throws Exception {
        // Initialize the database
        resultadoRepository.saveAndFlush(resultado);

        // Get the resultado
        restResultadoMockMvc.perform(get("/api/resultados/{id}", resultado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resultado.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(sameInstant(DEFAULT_FECHA_INICIO)))
            .andExpect(jsonPath("$.fehcaFin").value(sameInstant(DEFAULT_FEHCA_FIN)))
            .andExpect(jsonPath("$.valorMinimo").value(DEFAULT_VALOR_MINIMO.toString()))
            .andExpect(jsonPath("$.valorMaximo").value(DEFAULT_VALOR_MAXIMO.toString()))
            .andExpect(jsonPath("$.valorFinal").value(DEFAULT_VALOR_FINAL.toString()))
            .andExpect(jsonPath("$.valorFinalNum").value(DEFAULT_VALOR_FINAL_NUM.doubleValue()))
            .andExpect(jsonPath("$.codigoLaboratorio").value(DEFAULT_CODIGO_LABORATORIO.toString()))
            .andExpect(jsonPath("$.codigoEquipo").value(DEFAULT_CODIGO_EQUIPO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResultado() throws Exception {
        // Get the resultado
        restResultadoMockMvc.perform(get("/api/resultados/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResultado() throws Exception {
        // Initialize the database
        resultadoRepository.saveAndFlush(resultado);

        int databaseSizeBeforeUpdate = resultadoRepository.findAll().size();

        // Update the resultado
        Resultado updatedResultado = resultadoRepository.findById(resultado.getId()).get();
        // Disconnect from session so that the updates on updatedResultado are not directly saved in db
        em.detach(updatedResultado);
        updatedResultado
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fehcaFin(UPDATED_FEHCA_FIN)
            .valorMinimo(UPDATED_VALOR_MINIMO)
            .valorMaximo(UPDATED_VALOR_MAXIMO)
            .valorFinal(UPDATED_VALOR_FINAL)
            .valorFinalNum(UPDATED_VALOR_FINAL_NUM)
            .codigoLaboratorio(UPDATED_CODIGO_LABORATORIO)
            .codigoEquipo(UPDATED_CODIGO_EQUIPO);

        restResultadoMockMvc.perform(put("/api/resultados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResultado)))
            .andExpect(status().isOk());

        // Validate the Resultado in the database
        List<Resultado> resultadoList = resultadoRepository.findAll();
        assertThat(resultadoList).hasSize(databaseSizeBeforeUpdate);
        Resultado testResultado = resultadoList.get(resultadoList.size() - 1);
        assertThat(testResultado.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testResultado.getFehcaFin()).isEqualTo(UPDATED_FEHCA_FIN);
        assertThat(testResultado.getValorMinimo()).isEqualTo(UPDATED_VALOR_MINIMO);
        assertThat(testResultado.getValorMaximo()).isEqualTo(UPDATED_VALOR_MAXIMO);
        assertThat(testResultado.getValorFinal()).isEqualTo(UPDATED_VALOR_FINAL);
        assertThat(testResultado.getValorFinalNum()).isEqualTo(UPDATED_VALOR_FINAL_NUM);
        assertThat(testResultado.getCodigoLaboratorio()).isEqualTo(UPDATED_CODIGO_LABORATORIO);
        assertThat(testResultado.getCodigoEquipo()).isEqualTo(UPDATED_CODIGO_EQUIPO);
    }

    @Test
    @Transactional
    public void updateNonExistingResultado() throws Exception {
        int databaseSizeBeforeUpdate = resultadoRepository.findAll().size();

        // Create the Resultado

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultadoMockMvc.perform(put("/api/resultados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resultado)))
            .andExpect(status().isBadRequest());

        // Validate the Resultado in the database
        List<Resultado> resultadoList = resultadoRepository.findAll();
        assertThat(resultadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteResultado() throws Exception {
        // Initialize the database
        resultadoRepository.saveAndFlush(resultado);

        int databaseSizeBeforeDelete = resultadoRepository.findAll().size();

        // Delete the resultado
        restResultadoMockMvc.perform(delete("/api/resultados/{id}", resultado.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resultado> resultadoList = resultadoRepository.findAll();
        assertThat(resultadoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resultado.class);
        Resultado resultado1 = new Resultado();
        resultado1.setId(1L);
        Resultado resultado2 = new Resultado();
        resultado2.setId(resultado1.getId());
        assertThat(resultado1).isEqualTo(resultado2);
        resultado2.setId(2L);
        assertThat(resultado1).isNotEqualTo(resultado2);
        resultado1.setId(null);
        assertThat(resultado1).isNotEqualTo(resultado2);
    }
}
