package com.sigma.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.sigma.domain.enumeration.EstadoProyecto;

/**
 * A Proyecto.
 */
@Entity
@Table(name = "proyecto")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Proyecto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "codigo_solicitud")
    private String codigoSolicitud;

    @Column(name = "codigo_responsable")
    private String codigoResponsable;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoProyecto estado;

    @Column(name = "fecha_incio")
    private LocalDate fechaIncio;

    @Column(name = "fecha_fina")
    private LocalDate fechaFina;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Lob
    @Column(name = "comentario")
    private String comentario;

    @OneToMany(mappedBy = "proyecto")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PuntoMonitoreoObs> puntoMonitoreos = new HashSet<>();

    @OneToMany(mappedBy = "proyecto")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EquipoMonitoreo> equipos = new HashSet<>();

    @OneToMany(mappedBy = "proyecto")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Observacion> observacions = new HashSet<>();

    @OneToMany(mappedBy = "proyecto")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LaboratorioMonitoreo> laboratorios = new HashSet<>();

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

    public Proyecto codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoSolicitud() {
        return codigoSolicitud;
    }

    public Proyecto codigoSolicitud(String codigoSolicitud) {
        this.codigoSolicitud = codigoSolicitud;
        return this;
    }

    public void setCodigoSolicitud(String codigoSolicitud) {
        this.codigoSolicitud = codigoSolicitud;
    }

    public String getCodigoResponsable() {
        return codigoResponsable;
    }

    public Proyecto codigoResponsable(String codigoResponsable) {
        this.codigoResponsable = codigoResponsable;
        return this;
    }

    public void setCodigoResponsable(String codigoResponsable) {
        this.codigoResponsable = codigoResponsable;
    }

    public EstadoProyecto getEstado() {
        return estado;
    }

    public Proyecto estado(EstadoProyecto estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(EstadoProyecto estado) {
        this.estado = estado;
    }

    public LocalDate getFechaIncio() {
        return fechaIncio;
    }

    public Proyecto fechaIncio(LocalDate fechaIncio) {
        this.fechaIncio = fechaIncio;
        return this;
    }

    public void setFechaIncio(LocalDate fechaIncio) {
        this.fechaIncio = fechaIncio;
    }

    public LocalDate getFechaFina() {
        return fechaFina;
    }

    public Proyecto fechaFina(LocalDate fechaFina) {
        this.fechaFina = fechaFina;
        return this;
    }

    public void setFechaFina(LocalDate fechaFina) {
        this.fechaFina = fechaFina;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Proyecto descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComentario() {
        return comentario;
    }

    public Proyecto comentario(String comentario) {
        this.comentario = comentario;
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Set<PuntoMonitoreoObs> getPuntoMonitoreos() {
        return puntoMonitoreos;
    }

    public Proyecto puntoMonitoreos(Set<PuntoMonitoreoObs> puntoMonitoreoObs) {
        this.puntoMonitoreos = puntoMonitoreoObs;
        return this;
    }

    public Proyecto addPuntoMonitoreo(PuntoMonitoreoObs puntoMonitoreoObs) {
        this.puntoMonitoreos.add(puntoMonitoreoObs);
        puntoMonitoreoObs.setProyecto(this);
        return this;
    }

    public Proyecto removePuntoMonitoreo(PuntoMonitoreoObs puntoMonitoreoObs) {
        this.puntoMonitoreos.remove(puntoMonitoreoObs);
        puntoMonitoreoObs.setProyecto(null);
        return this;
    }

    public void setPuntoMonitoreos(Set<PuntoMonitoreoObs> puntoMonitoreoObs) {
        this.puntoMonitoreos = puntoMonitoreoObs;
    }

    public Set<EquipoMonitoreo> getEquipos() {
        return equipos;
    }

    public Proyecto equipos(Set<EquipoMonitoreo> equipoMonitoreos) {
        this.equipos = equipoMonitoreos;
        return this;
    }

    public Proyecto addEquipo(EquipoMonitoreo equipoMonitoreo) {
        this.equipos.add(equipoMonitoreo);
        equipoMonitoreo.setProyecto(this);
        return this;
    }

    public Proyecto removeEquipo(EquipoMonitoreo equipoMonitoreo) {
        this.equipos.remove(equipoMonitoreo);
        equipoMonitoreo.setProyecto(null);
        return this;
    }

    public void setEquipos(Set<EquipoMonitoreo> equipoMonitoreos) {
        this.equipos = equipoMonitoreos;
    }

    public Set<Observacion> getObservacions() {
        return observacions;
    }

    public Proyecto observacions(Set<Observacion> observacions) {
        this.observacions = observacions;
        return this;
    }

    public Proyecto addObservacion(Observacion observacion) {
        this.observacions.add(observacion);
        observacion.setProyecto(this);
        return this;
    }

    public Proyecto removeObservacion(Observacion observacion) {
        this.observacions.remove(observacion);
        observacion.setProyecto(null);
        return this;
    }

    public void setObservacions(Set<Observacion> observacions) {
        this.observacions = observacions;
    }

    public Set<LaboratorioMonitoreo> getLaboratorios() {
        return laboratorios;
    }

    public Proyecto laboratorios(Set<LaboratorioMonitoreo> laboratorioMonitoreos) {
        this.laboratorios = laboratorioMonitoreos;
        return this;
    }

    public Proyecto addLaboratorio(LaboratorioMonitoreo laboratorioMonitoreo) {
        this.laboratorios.add(laboratorioMonitoreo);
        laboratorioMonitoreo.setProyecto(this);
        return this;
    }

    public Proyecto removeLaboratorio(LaboratorioMonitoreo laboratorioMonitoreo) {
        this.laboratorios.remove(laboratorioMonitoreo);
        laboratorioMonitoreo.setProyecto(null);
        return this;
    }

    public void setLaboratorios(Set<LaboratorioMonitoreo> laboratorioMonitoreos) {
        this.laboratorios = laboratorioMonitoreos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proyecto)) {
            return false;
        }
        return id != null && id.equals(((Proyecto) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Proyecto{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", codigoSolicitud='" + getCodigoSolicitud() + "'" +
            ", codigoResponsable='" + getCodigoResponsable() + "'" +
            ", estado='" + getEstado() + "'" +
            ", fechaIncio='" + getFechaIncio() + "'" +
            ", fechaFina='" + getFechaFina() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", comentario='" + getComentario() + "'" +
            "}";
    }
}
