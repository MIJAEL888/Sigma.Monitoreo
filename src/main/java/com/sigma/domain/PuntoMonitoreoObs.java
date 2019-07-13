package com.sigma.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PuntoMonitoreoObs.
 */
@Entity
@Table(name = "punto_monitoreo_obs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PuntoMonitoreoObs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Lob
    @Column(name = "comentario")
    private String comentario;

    @Lob
    @Column(name = "observacion")
    private String observacion;

    @ManyToOne
    @JsonIgnoreProperties("puntoMonitoreoObs")
    private PuntoMonitoreo puntoMonitoreo;

    @OneToOne
    @JoinColumn(unique = true)
    private Resultado resultado;

    @OneToMany(mappedBy = "puntoMonitoreoObs")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FotografiaMonitoreo> fotografiaMonitoreos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("puntoMonitoreos")
    private Proyecto proyecto;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public PuntoMonitoreoObs codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public PuntoMonitoreoObs descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComentario() {
        return comentario;
    }

    public PuntoMonitoreoObs comentario(String comentario) {
        this.comentario = comentario;
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getObservacion() {
        return observacion;
    }

    public PuntoMonitoreoObs observacion(String observacion) {
        this.observacion = observacion;
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public PuntoMonitoreo getPuntoMonitoreo() {
        return puntoMonitoreo;
    }

    public PuntoMonitoreoObs puntoMonitoreo(PuntoMonitoreo puntoMonitoreo) {
        this.puntoMonitoreo = puntoMonitoreo;
        return this;
    }

    public void setPuntoMonitoreo(PuntoMonitoreo puntoMonitoreo) {
        this.puntoMonitoreo = puntoMonitoreo;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public PuntoMonitoreoObs resultado(Resultado resultado) {
        this.resultado = resultado;
        return this;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    public Set<FotografiaMonitoreo> getFotografiaMonitoreos() {
        return fotografiaMonitoreos;
    }

    public PuntoMonitoreoObs fotografiaMonitoreos(Set<FotografiaMonitoreo> fotografiaMonitoreos) {
        this.fotografiaMonitoreos = fotografiaMonitoreos;
        return this;
    }

    public PuntoMonitoreoObs addFotografiaMonitoreo(FotografiaMonitoreo fotografiaMonitoreo) {
        this.fotografiaMonitoreos.add(fotografiaMonitoreo);
        fotografiaMonitoreo.setPuntoMonitoreoObs(this);
        return this;
    }

    public PuntoMonitoreoObs removeFotografiaMonitoreo(FotografiaMonitoreo fotografiaMonitoreo) {
        this.fotografiaMonitoreos.remove(fotografiaMonitoreo);
        fotografiaMonitoreo.setPuntoMonitoreoObs(null);
        return this;
    }

    public void setFotografiaMonitoreos(Set<FotografiaMonitoreo> fotografiaMonitoreos) {
        this.fotografiaMonitoreos = fotografiaMonitoreos;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public PuntoMonitoreoObs proyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
        return this;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PuntoMonitoreoObs)) {
            return false;
        }
        return id != null && id.equals(((PuntoMonitoreoObs) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PuntoMonitoreoObs{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", comentario='" + getComentario() + "'" +
            ", observacion='" + getObservacion() + "'" +
            "}";
    }
}
