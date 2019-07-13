package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.LaboratorioMonitoreo;
import com.sigma.repository.LaboratorioMonitoreoRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.sigma.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link LaboratorioMonitoreoResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class LaboratorioMonitoreoResourceIT {

    private static final String DEFAULT_CODIGO_LABORATORIO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_LABORATORIO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_RESEVA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_RESEVA = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private LaboratorioMonitoreoRepository laboratorioMonitoreoRepository;

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

    private MockMvc restLaboratorioMonitoreoMockMvc;

    private LaboratorioMonitoreo laboratorioMonitoreo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LaboratorioMonitoreoResource laboratorioMonitoreoResource = new LaboratorioMonitoreoResource(laboratorioMonitoreoRepository);
        this.restLaboratorioMonitoreoMockMvc = MockMvcBuilders.standaloneSetup(laboratorioMonitoreoResource)
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
    public static LaboratorioMonitoreo createEntity(EntityManager em) {
        LaboratorioMonitoreo laboratorioMonitoreo = new LaboratorioMonitoreo()
            .codigoLaboratorio(DEFAULT_CODIGO_LABORATORIO)
            .fechaReseva(DEFAULT_FECHA_RESEVA);
        return laboratorioMonitoreo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LaboratorioMonitoreo createUpdatedEntity(EntityManager em) {
        LaboratorioMonitoreo laboratorioMonitoreo = new LaboratorioMonitoreo()
            .codigoLaboratorio(UPDATED_CODIGO_LABORATORIO)
            .fechaReseva(UPDATED_FECHA_RESEVA);
        return laboratorioMonitoreo;
    }

    @BeforeEach
    public void initTest() {
        laboratorioMonitoreo = createEntity(em);
    }

    @Test
    @Transactional
    public void createLaboratorioMonitoreo() throws Exception {
        int databaseSizeBeforeCreate = laboratorioMonitoreoRepository.findAll().size();

        // Create the LaboratorioMonitoreo
        restLaboratorioMonitoreoMockMvc.perform(post("/api/laboratorio-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratorioMonitoreo)))
            .andExpect(status().isCreated());

        // Validate the LaboratorioMonitoreo in the database
        List<LaboratorioMonitoreo> laboratorioMonitoreoList = laboratorioMonitoreoRepository.findAll();
        assertThat(laboratorioMonitoreoList).hasSize(databaseSizeBeforeCreate + 1);
        LaboratorioMonitoreo testLaboratorioMonitoreo = laboratorioMonitoreoList.get(laboratorioMonitoreoList.size() - 1);
        assertThat(testLaboratorioMonitoreo.getCodigoLaboratorio()).isEqualTo(DEFAULT_CODIGO_LABORATORIO);
        assertThat(testLaboratorioMonitoreo.getFechaReseva()).isEqualTo(DEFAULT_FECHA_RESEVA);
    }

    @Test
    @Transactional
    public void createLaboratorioMonitoreoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = laboratorioMonitoreoRepository.findAll().size();

        // Create the LaboratorioMonitoreo with an existing ID
        laboratorioMonitoreo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLaboratorioMonitoreoMockMvc.perform(post("/api/laboratorio-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratorioMonitoreo)))
            .andExpect(status().isBadRequest());

        // Validate the LaboratorioMonitoreo in the database
        List<LaboratorioMonitoreo> laboratorioMonitoreoList = laboratorioMonitoreoRepository.findAll();
        assertThat(laboratorioMonitoreoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLaboratorioMonitoreos() throws Exception {
        // Initialize the database
        laboratorioMonitoreoRepository.saveAndFlush(laboratorioMonitoreo);

        // Get all the laboratorioMonitoreoList
        restLaboratorioMonitoreoMockMvc.perform(get("/api/laboratorio-monitoreos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(laboratorioMonitoreo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigoLaboratorio").value(hasItem(DEFAULT_CODIGO_LABORATORIO.toString())))
            .andExpect(jsonPath("$.[*].fechaReseva").value(hasItem(DEFAULT_FECHA_RESEVA.toString())));
    }
    
    @Test
    @Transactional
    public void getLaboratorioMonitoreo() throws Exception {
        // Initialize the database
        laboratorioMonitoreoRepository.saveAndFlush(laboratorioMonitoreo);

        // Get the laboratorioMonitoreo
        restLaboratorioMonitoreoMockMvc.perform(get("/api/laboratorio-monitoreos/{id}", laboratorioMonitoreo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(laboratorioMonitoreo.getId().intValue()))
            .andExpect(jsonPath("$.codigoLaboratorio").value(DEFAULT_CODIGO_LABORATORIO.toString()))
            .andExpect(jsonPath("$.fechaReseva").value(DEFAULT_FECHA_RESEVA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLaboratorioMonitoreo() throws Exception {
        // Get the laboratorioMonitoreo
        restLaboratorioMonitoreoMockMvc.perform(get("/api/laboratorio-monitoreos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLaboratorioMonitoreo() throws Exception {
        // Initialize the database
        laboratorioMonitoreoRepository.saveAndFlush(laboratorioMonitoreo);

        int databaseSizeBeforeUpdate = laboratorioMonitoreoRepository.findAll().size();

        // Update the laboratorioMonitoreo
        LaboratorioMonitoreo updatedLaboratorioMonitoreo = laboratorioMonitoreoRepository.findById(laboratorioMonitoreo.getId()).get();
        // Disconnect from session so that the updates on updatedLaboratorioMonitoreo are not directly saved in db
        em.detach(updatedLaboratorioMonitoreo);
        updatedLaboratorioMonitoreo
            .codigoLaboratorio(UPDATED_CODIGO_LABORATORIO)
            .fechaReseva(UPDATED_FECHA_RESEVA);

        restLaboratorioMonitoreoMockMvc.perform(put("/api/laboratorio-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLaboratorioMonitoreo)))
            .andExpect(status().isOk());

        // Validate the LaboratorioMonitoreo in the database
        List<LaboratorioMonitoreo> laboratorioMonitoreoList = laboratorioMonitoreoRepository.findAll();
        assertThat(laboratorioMonitoreoList).hasSize(databaseSizeBeforeUpdate);
        LaboratorioMonitoreo testLaboratorioMonitoreo = laboratorioMonitoreoList.get(laboratorioMonitoreoList.size() - 1);
        assertThat(testLaboratorioMonitoreo.getCodigoLaboratorio()).isEqualTo(UPDATED_CODIGO_LABORATORIO);
        assertThat(testLaboratorioMonitoreo.getFechaReseva()).isEqualTo(UPDATED_FECHA_RESEVA);
    }

    @Test
    @Transactional
    public void updateNonExistingLaboratorioMonitoreo() throws Exception {
        int databaseSizeBeforeUpdate = laboratorioMonitoreoRepository.findAll().size();

        // Create the LaboratorioMonitoreo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLaboratorioMonitoreoMockMvc.perform(put("/api/laboratorio-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratorioMonitoreo)))
            .andExpect(status().isBadRequest());

        // Validate the LaboratorioMonitoreo in the database
        List<LaboratorioMonitoreo> laboratorioMonitoreoList = laboratorioMonitoreoRepository.findAll();
        assertThat(laboratorioMonitoreoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLaboratorioMonitoreo() throws Exception {
        // Initialize the database
        laboratorioMonitoreoRepository.saveAndFlush(laboratorioMonitoreo);

        int databaseSizeBeforeDelete = laboratorioMonitoreoRepository.findAll().size();

        // Delete the laboratorioMonitoreo
        restLaboratorioMonitoreoMockMvc.perform(delete("/api/laboratorio-monitoreos/{id}", laboratorioMonitoreo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LaboratorioMonitoreo> laboratorioMonitoreoList = laboratorioMonitoreoRepository.findAll();
        assertThat(laboratorioMonitoreoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LaboratorioMonitoreo.class);
        LaboratorioMonitoreo laboratorioMonitoreo1 = new LaboratorioMonitoreo();
        laboratorioMonitoreo1.setId(1L);
        LaboratorioMonitoreo laboratorioMonitoreo2 = new LaboratorioMonitoreo();
        laboratorioMonitoreo2.setId(laboratorioMonitoreo1.getId());
        assertThat(laboratorioMonitoreo1).isEqualTo(laboratorioMonitoreo2);
        laboratorioMonitoreo2.setId(2L);
        assertThat(laboratorioMonitoreo1).isNotEqualTo(laboratorioMonitoreo2);
        laboratorioMonitoreo1.setId(null);
        assertThat(laboratorioMonitoreo1).isNotEqualTo(laboratorioMonitoreo2);
    }
}
