package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.ResultadoMetereologia;
import com.sigma.repository.ResultadoMetereologiaRepository;
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
 * Integration tests for the {@Link ResultadoMetereologiaResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class ResultadoMetereologiaResourceIT {

    private static final ZonedDateTime DEFAULT_FECHA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    private static final Float DEFAULT_VALOR_DECIMAL = 1F;
    private static final Float UPDATED_VALOR_DECIMAL = 2F;

    @Autowired
    private ResultadoMetereologiaRepository resultadoMetereologiaRepository;

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

    private MockMvc restResultadoMetereologiaMockMvc;

    private ResultadoMetereologia resultadoMetereologia;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResultadoMetereologiaResource resultadoMetereologiaResource = new ResultadoMetereologiaResource(resultadoMetereologiaRepository);
        this.restResultadoMetereologiaMockMvc = MockMvcBuilders.standaloneSetup(resultadoMetereologiaResource)
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
    public static ResultadoMetereologia createEntity(EntityManager em) {
        ResultadoMetereologia resultadoMetereologia = new ResultadoMetereologia()
            .fecha(DEFAULT_FECHA)
            .valor(DEFAULT_VALOR)
            .valorDecimal(DEFAULT_VALOR_DECIMAL);
        return resultadoMetereologia;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultadoMetereologia createUpdatedEntity(EntityManager em) {
        ResultadoMetereologia resultadoMetereologia = new ResultadoMetereologia()
            .fecha(UPDATED_FECHA)
            .valor(UPDATED_VALOR)
            .valorDecimal(UPDATED_VALOR_DECIMAL);
        return resultadoMetereologia;
    }

    @BeforeEach
    public void initTest() {
        resultadoMetereologia = createEntity(em);
    }

    @Test
    @Transactional
    public void createResultadoMetereologia() throws Exception {
        int databaseSizeBeforeCreate = resultadoMetereologiaRepository.findAll().size();

        // Create the ResultadoMetereologia
        restResultadoMetereologiaMockMvc.perform(post("/api/resultado-metereologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resultadoMetereologia)))
            .andExpect(status().isCreated());

        // Validate the ResultadoMetereologia in the database
        List<ResultadoMetereologia> resultadoMetereologiaList = resultadoMetereologiaRepository.findAll();
        assertThat(resultadoMetereologiaList).hasSize(databaseSizeBeforeCreate + 1);
        ResultadoMetereologia testResultadoMetereologia = resultadoMetereologiaList.get(resultadoMetereologiaList.size() - 1);
        assertThat(testResultadoMetereologia.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testResultadoMetereologia.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testResultadoMetereologia.getValorDecimal()).isEqualTo(DEFAULT_VALOR_DECIMAL);
    }

    @Test
    @Transactional
    public void createResultadoMetereologiaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resultadoMetereologiaRepository.findAll().size();

        // Create the ResultadoMetereologia with an existing ID
        resultadoMetereologia.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultadoMetereologiaMockMvc.perform(post("/api/resultado-metereologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resultadoMetereologia)))
            .andExpect(status().isBadRequest());

        // Validate the ResultadoMetereologia in the database
        List<ResultadoMetereologia> resultadoMetereologiaList = resultadoMetereologiaRepository.findAll();
        assertThat(resultadoMetereologiaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllResultadoMetereologias() throws Exception {
        // Initialize the database
        resultadoMetereologiaRepository.saveAndFlush(resultadoMetereologia);

        // Get all the resultadoMetereologiaList
        restResultadoMetereologiaMockMvc.perform(get("/api/resultado-metereologias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultadoMetereologia.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(sameInstant(DEFAULT_FECHA))))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.toString())))
            .andExpect(jsonPath("$.[*].valorDecimal").value(hasItem(DEFAULT_VALOR_DECIMAL.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getResultadoMetereologia() throws Exception {
        // Initialize the database
        resultadoMetereologiaRepository.saveAndFlush(resultadoMetereologia);

        // Get the resultadoMetereologia
        restResultadoMetereologiaMockMvc.perform(get("/api/resultado-metereologias/{id}", resultadoMetereologia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resultadoMetereologia.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(sameInstant(DEFAULT_FECHA)))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.toString()))
            .andExpect(jsonPath("$.valorDecimal").value(DEFAULT_VALOR_DECIMAL.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingResultadoMetereologia() throws Exception {
        // Get the resultadoMetereologia
        restResultadoMetereologiaMockMvc.perform(get("/api/resultado-metereologias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResultadoMetereologia() throws Exception {
        // Initialize the database
        resultadoMetereologiaRepository.saveAndFlush(resultadoMetereologia);

        int databaseSizeBeforeUpdate = resultadoMetereologiaRepository.findAll().size();

        // Update the resultadoMetereologia
        ResultadoMetereologia updatedResultadoMetereologia = resultadoMetereologiaRepository.findById(resultadoMetereologia.getId()).get();
        // Disconnect from session so that the updates on updatedResultadoMetereologia are not directly saved in db
        em.detach(updatedResultadoMetereologia);
        updatedResultadoMetereologia
            .fecha(UPDATED_FECHA)
            .valor(UPDATED_VALOR)
            .valorDecimal(UPDATED_VALOR_DECIMAL);

        restResultadoMetereologiaMockMvc.perform(put("/api/resultado-metereologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResultadoMetereologia)))
            .andExpect(status().isOk());

        // Validate the ResultadoMetereologia in the database
        List<ResultadoMetereologia> resultadoMetereologiaList = resultadoMetereologiaRepository.findAll();
        assertThat(resultadoMetereologiaList).hasSize(databaseSizeBeforeUpdate);
        ResultadoMetereologia testResultadoMetereologia = resultadoMetereologiaList.get(resultadoMetereologiaList.size() - 1);
        assertThat(testResultadoMetereologia.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testResultadoMetereologia.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testResultadoMetereologia.getValorDecimal()).isEqualTo(UPDATED_VALOR_DECIMAL);
    }

    @Test
    @Transactional
    public void updateNonExistingResultadoMetereologia() throws Exception {
        int databaseSizeBeforeUpdate = resultadoMetereologiaRepository.findAll().size();

        // Create the ResultadoMetereologia

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultadoMetereologiaMockMvc.perform(put("/api/resultado-metereologias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resultadoMetereologia)))
            .andExpect(status().isBadRequest());

        // Validate the ResultadoMetereologia in the database
        List<ResultadoMetereologia> resultadoMetereologiaList = resultadoMetereologiaRepository.findAll();
        assertThat(resultadoMetereologiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteResultadoMetereologia() throws Exception {
        // Initialize the database
        resultadoMetereologiaRepository.saveAndFlush(resultadoMetereologia);

        int databaseSizeBeforeDelete = resultadoMetereologiaRepository.findAll().size();

        // Delete the resultadoMetereologia
        restResultadoMetereologiaMockMvc.perform(delete("/api/resultado-metereologias/{id}", resultadoMetereologia.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResultadoMetereologia> resultadoMetereologiaList = resultadoMetereologiaRepository.findAll();
        assertThat(resultadoMetereologiaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResultadoMetereologia.class);
        ResultadoMetereologia resultadoMetereologia1 = new ResultadoMetereologia();
        resultadoMetereologia1.setId(1L);
        ResultadoMetereologia resultadoMetereologia2 = new ResultadoMetereologia();
        resultadoMetereologia2.setId(resultadoMetereologia1.getId());
        assertThat(resultadoMetereologia1).isEqualTo(resultadoMetereologia2);
        resultadoMetereologia2.setId(2L);
        assertThat(resultadoMetereologia1).isNotEqualTo(resultadoMetereologia2);
        resultadoMetereologia1.setId(null);
        assertThat(resultadoMetereologia1).isNotEqualTo(resultadoMetereologia2);
    }
}
