package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.EquipoMonitoreo;
import com.sigma.repository.EquipoMonitoreoRepository;
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
 * Integration tests for the {@Link EquipoMonitoreoResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class EquipoMonitoreoResourceIT {

    private static final String DEFAULT_CODIGO_EQUIPO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_EQUIPO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RESERVADO_DESDE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RESERVADO_DESDE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RESERVADO_HASTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RESERVADO_HASTA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DOCUMENTO_CALIBRACION = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTO_CALIBRACION = "BBBBBBBBBB";

    @Autowired
    private EquipoMonitoreoRepository equipoMonitoreoRepository;

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

    private MockMvc restEquipoMonitoreoMockMvc;

    private EquipoMonitoreo equipoMonitoreo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EquipoMonitoreoResource equipoMonitoreoResource = new EquipoMonitoreoResource(equipoMonitoreoRepository);
        this.restEquipoMonitoreoMockMvc = MockMvcBuilders.standaloneSetup(equipoMonitoreoResource)
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
    public static EquipoMonitoreo createEntity(EntityManager em) {
        EquipoMonitoreo equipoMonitoreo = new EquipoMonitoreo()
            .codigoEquipo(DEFAULT_CODIGO_EQUIPO)
            .reservadoDesde(DEFAULT_RESERVADO_DESDE)
            .reservadoHasta(DEFAULT_RESERVADO_HASTA)
            .documentoCalibracion(DEFAULT_DOCUMENTO_CALIBRACION);
        return equipoMonitoreo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipoMonitoreo createUpdatedEntity(EntityManager em) {
        EquipoMonitoreo equipoMonitoreo = new EquipoMonitoreo()
            .codigoEquipo(UPDATED_CODIGO_EQUIPO)
            .reservadoDesde(UPDATED_RESERVADO_DESDE)
            .reservadoHasta(UPDATED_RESERVADO_HASTA)
            .documentoCalibracion(UPDATED_DOCUMENTO_CALIBRACION);
        return equipoMonitoreo;
    }

    @BeforeEach
    public void initTest() {
        equipoMonitoreo = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipoMonitoreo() throws Exception {
        int databaseSizeBeforeCreate = equipoMonitoreoRepository.findAll().size();

        // Create the EquipoMonitoreo
        restEquipoMonitoreoMockMvc.perform(post("/api/equipo-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipoMonitoreo)))
            .andExpect(status().isCreated());

        // Validate the EquipoMonitoreo in the database
        List<EquipoMonitoreo> equipoMonitoreoList = equipoMonitoreoRepository.findAll();
        assertThat(equipoMonitoreoList).hasSize(databaseSizeBeforeCreate + 1);
        EquipoMonitoreo testEquipoMonitoreo = equipoMonitoreoList.get(equipoMonitoreoList.size() - 1);
        assertThat(testEquipoMonitoreo.getCodigoEquipo()).isEqualTo(DEFAULT_CODIGO_EQUIPO);
        assertThat(testEquipoMonitoreo.getReservadoDesde()).isEqualTo(DEFAULT_RESERVADO_DESDE);
        assertThat(testEquipoMonitoreo.getReservadoHasta()).isEqualTo(DEFAULT_RESERVADO_HASTA);
        assertThat(testEquipoMonitoreo.getDocumentoCalibracion()).isEqualTo(DEFAULT_DOCUMENTO_CALIBRACION);
    }

    @Test
    @Transactional
    public void createEquipoMonitoreoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipoMonitoreoRepository.findAll().size();

        // Create the EquipoMonitoreo with an existing ID
        equipoMonitoreo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipoMonitoreoMockMvc.perform(post("/api/equipo-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipoMonitoreo)))
            .andExpect(status().isBadRequest());

        // Validate the EquipoMonitoreo in the database
        List<EquipoMonitoreo> equipoMonitoreoList = equipoMonitoreoRepository.findAll();
        assertThat(equipoMonitoreoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEquipoMonitoreos() throws Exception {
        // Initialize the database
        equipoMonitoreoRepository.saveAndFlush(equipoMonitoreo);

        // Get all the equipoMonitoreoList
        restEquipoMonitoreoMockMvc.perform(get("/api/equipo-monitoreos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipoMonitoreo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigoEquipo").value(hasItem(DEFAULT_CODIGO_EQUIPO.toString())))
            .andExpect(jsonPath("$.[*].reservadoDesde").value(hasItem(DEFAULT_RESERVADO_DESDE.toString())))
            .andExpect(jsonPath("$.[*].reservadoHasta").value(hasItem(DEFAULT_RESERVADO_HASTA.toString())))
            .andExpect(jsonPath("$.[*].documentoCalibracion").value(hasItem(DEFAULT_DOCUMENTO_CALIBRACION.toString())));
    }
    
    @Test
    @Transactional
    public void getEquipoMonitoreo() throws Exception {
        // Initialize the database
        equipoMonitoreoRepository.saveAndFlush(equipoMonitoreo);

        // Get the equipoMonitoreo
        restEquipoMonitoreoMockMvc.perform(get("/api/equipo-monitoreos/{id}", equipoMonitoreo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(equipoMonitoreo.getId().intValue()))
            .andExpect(jsonPath("$.codigoEquipo").value(DEFAULT_CODIGO_EQUIPO.toString()))
            .andExpect(jsonPath("$.reservadoDesde").value(DEFAULT_RESERVADO_DESDE.toString()))
            .andExpect(jsonPath("$.reservadoHasta").value(DEFAULT_RESERVADO_HASTA.toString()))
            .andExpect(jsonPath("$.documentoCalibracion").value(DEFAULT_DOCUMENTO_CALIBRACION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEquipoMonitoreo() throws Exception {
        // Get the equipoMonitoreo
        restEquipoMonitoreoMockMvc.perform(get("/api/equipo-monitoreos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipoMonitoreo() throws Exception {
        // Initialize the database
        equipoMonitoreoRepository.saveAndFlush(equipoMonitoreo);

        int databaseSizeBeforeUpdate = equipoMonitoreoRepository.findAll().size();

        // Update the equipoMonitoreo
        EquipoMonitoreo updatedEquipoMonitoreo = equipoMonitoreoRepository.findById(equipoMonitoreo.getId()).get();
        // Disconnect from session so that the updates on updatedEquipoMonitoreo are not directly saved in db
        em.detach(updatedEquipoMonitoreo);
        updatedEquipoMonitoreo
            .codigoEquipo(UPDATED_CODIGO_EQUIPO)
            .reservadoDesde(UPDATED_RESERVADO_DESDE)
            .reservadoHasta(UPDATED_RESERVADO_HASTA)
            .documentoCalibracion(UPDATED_DOCUMENTO_CALIBRACION);

        restEquipoMonitoreoMockMvc.perform(put("/api/equipo-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEquipoMonitoreo)))
            .andExpect(status().isOk());

        // Validate the EquipoMonitoreo in the database
        List<EquipoMonitoreo> equipoMonitoreoList = equipoMonitoreoRepository.findAll();
        assertThat(equipoMonitoreoList).hasSize(databaseSizeBeforeUpdate);
        EquipoMonitoreo testEquipoMonitoreo = equipoMonitoreoList.get(equipoMonitoreoList.size() - 1);
        assertThat(testEquipoMonitoreo.getCodigoEquipo()).isEqualTo(UPDATED_CODIGO_EQUIPO);
        assertThat(testEquipoMonitoreo.getReservadoDesde()).isEqualTo(UPDATED_RESERVADO_DESDE);
        assertThat(testEquipoMonitoreo.getReservadoHasta()).isEqualTo(UPDATED_RESERVADO_HASTA);
        assertThat(testEquipoMonitoreo.getDocumentoCalibracion()).isEqualTo(UPDATED_DOCUMENTO_CALIBRACION);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipoMonitoreo() throws Exception {
        int databaseSizeBeforeUpdate = equipoMonitoreoRepository.findAll().size();

        // Create the EquipoMonitoreo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipoMonitoreoMockMvc.perform(put("/api/equipo-monitoreos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipoMonitoreo)))
            .andExpect(status().isBadRequest());

        // Validate the EquipoMonitoreo in the database
        List<EquipoMonitoreo> equipoMonitoreoList = equipoMonitoreoRepository.findAll();
        assertThat(equipoMonitoreoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEquipoMonitoreo() throws Exception {
        // Initialize the database
        equipoMonitoreoRepository.saveAndFlush(equipoMonitoreo);

        int databaseSizeBeforeDelete = equipoMonitoreoRepository.findAll().size();

        // Delete the equipoMonitoreo
        restEquipoMonitoreoMockMvc.perform(delete("/api/equipo-monitoreos/{id}", equipoMonitoreo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EquipoMonitoreo> equipoMonitoreoList = equipoMonitoreoRepository.findAll();
        assertThat(equipoMonitoreoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipoMonitoreo.class);
        EquipoMonitoreo equipoMonitoreo1 = new EquipoMonitoreo();
        equipoMonitoreo1.setId(1L);
        EquipoMonitoreo equipoMonitoreo2 = new EquipoMonitoreo();
        equipoMonitoreo2.setId(equipoMonitoreo1.getId());
        assertThat(equipoMonitoreo1).isEqualTo(equipoMonitoreo2);
        equipoMonitoreo2.setId(2L);
        assertThat(equipoMonitoreo1).isNotEqualTo(equipoMonitoreo2);
        equipoMonitoreo1.setId(null);
        assertThat(equipoMonitoreo1).isNotEqualTo(equipoMonitoreo2);
    }
}
