package com.sigma.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Paramentro.
 */
@Entity
@Table(name = "paramentro")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Paramentro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "siglas", nullable = false)
    private String siglas;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "costo")
    private Float costo;

    @Lob
    @Column(name = "metodologia")
    private String metodologia;

    @Lob
    @Column(name = "metodo_ensayo")
    private String metodoEnsayo;

    @Column(name = "limite_cuantificacion")
    private Integer limiteCuantificacion;

    @ManyToOne
    @JsonIgnoreProperties("paramentros")
    private TipoComponente tipoComponente;

    @ManyToOne
    @JsonIgnoreProperties("paramentros")
    private NormaCalidad normaCalidad;

    @ManyToOne
    @JsonIgnoreProperties("paramentros")
    private PuntoMonitoreo puntoMonitoreo;

    @ManyToOne
    @JsonIgnoreProperties("paramentros")
    private Unidades unidades;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Paramentro nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSiglas() {
        return siglas;
    }

    public Paramentro siglas(String siglas) {
        this.siglas = siglas;
        return this;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Paramentro descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getCosto() {
        return costo;
    }

    public Paramentro costo(Float costo) {
        this.costo = costo;
        return this;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public String getMetodologia() {
        return metodologia;
    }

    public Paramentro metodologia(String metodologia) {
        this.metodologia = metodologia;
        return this;
    }

    public void setMetodologia(String metodologia) {
        this.metodologia = metodologia;
    }

    public String getMetodoEnsayo() {
        return metodoEnsayo;
    }

    public Paramentro metodoEnsayo(String metodoEnsayo) {
        this.metodoEnsayo = metodoEnsayo;
        return this;
    }

    public void setMetodoEnsayo(String metodoEnsayo) {
        this.metodoEnsayo = metodoEnsayo;
    }

    public Integer getLimiteCuantificacion() {
        return limiteCuantificacion;
    }

    public Paramentro limiteCuantificacion(Integer limiteCuantificacion) {
        this.limiteCuantificacion = limiteCuantificacion;
        return this;
    }

    public void setLimiteCuantificacion(Integer limiteCuantificacion) {
        this.limiteCuantificacion = limiteCuantificacion;
    }

    public TipoComponente getTipoComponente() {
        return tipoComponente;
    }

    public Paramentro tipoComponente(TipoComponente tipoComponente) {
        this.tipoComponente = tipoComponente;
        return this;
    }

    public void setTipoComponente(TipoComponente tipoComponente) {
        this.tipoComponente = tipoComponente;
    }

    public NormaCalidad getNormaCalidad() {
        return normaCalidad;
    }

    public Paramentro normaCalidad(NormaCalidad normaCalidad) {
        this.normaCalidad = normaCalidad;
        return this;
    }

    public void setNormaCalidad(NormaCalidad normaCalidad) {
        this.normaCalidad = normaCalidad;
    }

    public PuntoMonitoreo getPuntoMonitoreo() {
        return puntoMonitoreo;
    }

    public Paramentro puntoMonitoreo(PuntoMonitoreo puntoMonitoreo) {
        this.puntoMonitoreo = puntoMonitoreo;
        return this;
    }

    public void setPuntoMonitoreo(PuntoMonitoreo puntoMonitoreo) {
        this.puntoMonitoreo = puntoMonitoreo;
    }

    public Unidades getUnidades() {
        return unidades;
    }

    public Paramentro unidades(Unidades unidades) {
        this.unidades = unidades;
        return this;
    }

    public void setUnidades(Unidades unidades) {
        this.unidades = unidades;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paramentro)) {
            return false;
        }
        return id != null && id.equals(((Paramentro) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Paramentro{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", siglas='" + getSiglas() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", costo=" + getCosto() +
            ", metodologia='" + getMetodologia() + "'" +
            ", metodoEnsayo='" + getMetodoEnsayo() + "'" +
            ", limiteCuantificacion=" + getLimiteCuantificacion() +
            "}";
    }
}
