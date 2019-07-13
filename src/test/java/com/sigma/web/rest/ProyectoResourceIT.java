package com.sigma.web.rest;

import com.sigma.MonitoreoApp;
import com.sigma.domain.Proyecto;
import com.sigma.repository.ProyectoRepository;
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

import com.sigma.domain.enumeration.EstadoProyecto;
/**
 * Integration tests for the {@Link ProyectoResource} REST controller.
 */
@SpringBootTest(classes = MonitoreoApp.class)
public class ProyectoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_SOLICITUD = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_SOLICITUD = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_RESPONSABLE = "BBBBBBBBBB";

    private static final EstadoProyecto DEFAULT_ESTADO = EstadoProyecto.REGISTRADO;
    private static final EstadoProyecto UPDATED_ESTADO = EstadoProyecto.PLANIFICADO;

    private static final LocalDate DEFAULT_FECHA_INCIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INCIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_FINA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FINA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    @Autowired
    private ProyectoRepository proyectoRepository;

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

    private MockMvc restProyectoMockMvc;

    private Proyecto proyecto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProyectoResource proyectoResource = new ProyectoResource(proyectoRepository);
        this.restProyectoMockMvc = MockMvcBuilders.standaloneSetup(proyectoResource)
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
    public static Proyecto createEntity(EntityManager em) {
        Proyecto proyecto = new Proyecto()
            .codigo(DEFAULT_CODIGO)
            .codigoSolicitud(DEFAULT_CODIGO_SOLICITUD)
            .codigoResponsable(DEFAULT_CODIGO_RESPONSABLE)
            .estado(DEFAULT_ESTADO)
            .fechaIncio(DEFAULT_FECHA_INCIO)
            .fechaFina(DEFAULT_FECHA_FINA)
            .descripcion(DEFAULT_DESCRIPCION)
            .comentario(DEFAULT_COMENTARIO);
        return proyecto;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proyecto createUpdatedEntity(EntityManager em) {
        Proyecto proyecto = new Proyecto()
            .codigo(UPDATED_CODIGO)
            .codigoSolicitud(UPDATED_CODIGO_SOLICITUD)
            .codigoResponsable(UPDATED_CODIGO_RESPONSABLE)
            .estado(UPDATED_ESTADO)
            .fechaIncio(UPDATED_FECHA_INCIO)
            .fechaFina(UPDATED_FECHA_FINA)
            .descripcion(UPDATED_DESCRIPCION)
            .comentario(UPDATED_COMENTARIO);
        return proyecto;
    }

    @BeforeEach
    public void initTest() {
        proyecto = createEntity(em);
    }

    @Test
    @Transactional
    public void createProyecto() throws Exception {
        int databaseSizeBeforeCreate = proyectoRepository.findAll().size();

        // Create the Proyecto
        restProyectoMockMvc.perform(post("/api/proyectos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proyecto)))
            .andExpect(status().isCreated());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeCreate + 1);
        Proyecto testProyecto = proyectoList.get(proyectoList.size() - 1);
        assertThat(testProyecto.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testProyecto.getCodigoSolicitud()).isEqualTo(DEFAULT_CODIGO_SOLICITUD);
        assertThat(testProyecto.getCodigoResponsable()).isEqualTo(DEFAULT_CODIGO_RESPONSABLE);
        assertThat(testProyecto.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testProyecto.getFechaIncio()).isEqualTo(DEFAULT_FECHA_INCIO);
        assertThat(testProyecto.getFechaFina()).isEqualTo(DEFAULT_FECHA_FINA);
        assertThat(testProyecto.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testProyecto.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
    }

    @Test
    @Transactional
    public void createProyectoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proyectoRepository.findAll().size();

        // Create the Proyecto with an existing ID
        proyecto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProyectoMockMvc.perform(post("/api/proyectos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proyecto)))
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProyectos() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList
        restProyectoMockMvc.perform(get("/api/proyectos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proyecto.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
            .andExpect(jsonPath("$.[*].codigoSolicitud").value(hasItem(DEFAULT_CODIGO_SOLICITUD.toString())))
            .andExpect(jsonPath("$.[*].codigoResponsable").value(hasItem(DEFAULT_CODIGO_RESPONSABLE.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].fechaIncio").value(hasItem(DEFAULT_FECHA_INCIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFina").value(hasItem(DEFAULT_FECHA_FINA.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO.toString())));
    }
    
    @Test
    @Transactional
    public void getProyecto() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get the proyecto
        restProyectoMockMvc.perform(get("/api/proyectos/{id}", proyecto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(proyecto.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.codigoSolicitud").value(DEFAULT_CODIGO_SOLICITUD.toString()))
            .andExpect(jsonPath("$.codigoResponsable").value(DEFAULT_CODIGO_RESPONSABLE.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.fechaIncio").value(DEFAULT_FECHA_INCIO.toString()))
            .andExpect(jsonPath("$.fechaFina").value(DEFAULT_FECHA_FINA.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProyecto() throws Exception {
        // Get the proyecto
        restProyectoMockMvc.perform(get("/api/proyectos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProyecto() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        int databaseSizeBeforeUpdate = proyectoRepository.findAll().size();

        // Update the proyecto
        Proyecto updatedProyecto = proyectoRepository.findById(proyecto.getId()).get();
        // Disconnect from session so that the updates on updatedProyecto are not directly saved in db
        em.detach(updatedProyecto);
        updatedProyecto
            .codigo(UPDATED_CODIGO)
            .codigoSolicitud(UPDATED_CODIGO_SOLICITUD)
            .codigoResponsable(UPDATED_CODIGO_RESPONSABLE)
            .estado(UPDATED_ESTADO)
            .fechaIncio(UPDATED_FECHA_INCIO)
            .fechaFina(UPDATED_FECHA_FINA)
            .descripcion(UPDATED_DESCRIPCION)
            .comentario(UPDATED_COMENTARIO);

        restProyectoMockMvc.perform(put("/api/proyectos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProyecto)))
            .andExpect(status().isOk());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
        Proyecto testProyecto = proyectoList.get(proyectoList.size() - 1);
        assertThat(testProyecto.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testProyecto.getCodigoSolicitud()).isEqualTo(UPDATED_CODIGO_SOLICITUD);
        assertThat(testProyecto.getCodigoResponsable()).isEqualTo(UPDATED_CODIGO_RESPONSABLE);
        assertThat(testProyecto.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testProyecto.getFechaIncio()).isEqualTo(UPDATED_FECHA_INCIO);
        assertThat(testProyecto.getFechaFina()).isEqualTo(UPDATED_FECHA_FINA);
        assertThat(testProyecto.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testProyecto.getComentario()).isEqualTo(UPDATED_COMENTARIO);
    }

    @Test
    @Transactional
    public void updateNonExistingProyecto() throws Exception {
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().size();

        // Create the Proyecto

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProyectoMockMvc.perform(put("/api/proyectos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proyecto)))
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProyecto() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        int databaseSizeBeforeDelete = proyectoRepository.findAll().size();

        // Delete the proyecto
        restProyectoMockMvc.perform(delete("/api/proyectos/{id}", proyecto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Proyecto> proyectoList = proyectoRepository.findAll();
        assertThat(proyectoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proyecto.class);
        Proyecto proyecto1 = new Proyecto();
        proyecto1.setId(1L);
        Proyecto proyecto2 = new Proyecto();
        proyecto2.setId(proyecto1.getId());
        assertThat(proyecto1).isEqualTo(proyecto2);
        proyecto2.setId(2L);
        assertThat(proyecto1).isNotEqualTo(proyecto2);
        proyecto1.setId(null);
        assertThat(proyecto1).isNotEqualTo(proyecto2);
    }
}
