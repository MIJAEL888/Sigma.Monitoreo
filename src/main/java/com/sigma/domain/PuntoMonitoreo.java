package com.sigma.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PuntoMonitoreo.
 */
@Entity
@Table(name = "punto_monitoreo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PuntoMonitoreo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "codigo", nullable = false)
    private String codigo;

    @NotNull
    @Column(name = "codigo_sede", nullable = false)
    private String codigoSede;

    @NotNull
    @Column(name = "codigo_cliente", nullable = false)
    private String codigoCliente;

    @NotNull
    @Column(name = "coordenada_norte", nullable = false)
    private String coordenadaNorte;

    @NotNull
    @Column(name = "coordenada_este", nullable = false)
    private String coordenadaEste;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Lob
    @Column(name = "comentario")
    private String comentario;

    @Column(name = "latitud")
    private Float latitud;

    @Column(name = "longitud")
    private Float longitud;

    @Lob
    @Column(name = "observacion")
    private String observacion;

    @OneToMany(mappedBy = "puntoMonitoreo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PuntoMonitoreoObs> puntoMonitoreoObs = new HashSet<>();

    @OneToMany(mappedBy = "puntoMonitoreo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Paramentro> paramentros = new HashSet<>();

    @OneToMany(mappedBy = "puntoMonitoreo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FotografiaPunto> fotografiaPuntos = new HashSet<>();

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

    public PuntoMonitoreo codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoSede() {
        return codigoSede;
    }

    public PuntoMonitoreo codigoSede(String codigoSede) {
        this.codigoSede = codigoSede;
        return this;
    }

    public void setCodigoSede(String codigoSede) {
        this.codigoSede = codigoSede;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public PuntoMonitoreo codigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
        return this;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getCoordenadaNorte() {
        return coordenadaNorte;
    }

    public PuntoMonitoreo coordenadaNorte(String coordenadaNorte) {
        this.coordenadaNorte = coordenadaNorte;
        return this;
    }

    public void setCoordenadaNorte(String coordenadaNorte) {
        this.coordenadaNorte = coordenadaNorte;
    }

    public String getCoordenadaEste() {
        return coordenadaEste;
    }

    public PuntoMonitoreo coordenadaEste(String coordenadaEste) {
        this.coordenadaEste = coordenadaEste;
        return this;
    }

    public void setCoordenadaEste(String coordenadaEste) {
        this.coordenadaEste = coordenadaEste;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public PuntoMonitoreo descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComentario() {
        return comentario;
    }

    public PuntoMonitoreo comentario(String comentario) {
        this.comentario = comentario;
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Float getLatitud() {
        return latitud;
    }

    public PuntoMonitoreo latitud(Float latitud) {
        this.latitud = latitud;
        return this;
    }

    public void setLatitud(Float latitud) {
        this.latitud = latitud;
    }

    public Float getLongitud() {
        return longitud;
    }

    public PuntoMonitoreo longitud(Float longitud) {
        this.longitud = longitud;
        return this;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

    public String getObservacion() {
        return observacion;
    }

    public PuntoMonitoreo observacion(String observacion) {
        this.observacion = observacion;
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Set<PuntoMonitoreoObs> getPuntoMonitoreoObs() {
        return puntoMonitoreoObs;
    }

    public PuntoMonitoreo puntoMonitoreoObs(Set<PuntoMonitoreoObs> puntoMonitoreoObs) {
        this.puntoMonitoreoObs = puntoMonitoreoObs;
        return this;
    }

    public PuntoMonitoreo addPuntoMonitoreoObs(PuntoMonitoreoObs puntoMonitoreoObs) {
        this.puntoMonitoreoObs.add(puntoMonitoreoObs);
        puntoMonitoreoObs.setPuntoMonitoreo(this);
        return this;
    }

    public PuntoMonitoreo removePuntoMonitoreoObs(PuntoMonitoreoObs puntoMonitoreoObs) {
        this.puntoMonitoreoObs.remove(puntoMonitoreoObs);
        puntoMonitoreoObs.setPuntoMonitoreo(null);
        return this;
    }

    public void setPuntoMonitoreoObs(Set<PuntoMonitoreoObs> puntoMonitoreoObs) {
        this.puntoMonitoreoObs = puntoMonitoreoObs;
    }

    public Set<Paramentro> getParamentros() {
        return paramentros;
    }

    public PuntoMonitoreo paramentros(Set<Paramentro> paramentros) {
        this.paramentros = paramentros;
        return this;
    }

    public PuntoMonitoreo addParamentro(Paramentro paramentro) {
        this.paramentros.add(paramentro);
        paramentro.setPuntoMonitoreo(this);
        return this;
    }

    public PuntoMonitoreo removeParamentro(Paramentro paramentro) {
        this.paramentros.remove(paramentro);
        paramentro.setPuntoMonitoreo(null);
        return this;
    }

    public void setParamentros(Set<Paramentro> paramentros) {
        this.paramentros = paramentros;
    }

    public Set<FotografiaPunto> getFotografiaPuntos() {
        return fotografiaPuntos;
    }

    public PuntoMonitoreo fotografiaPuntos(Set<FotografiaPunto> fotografiaPuntos) {
        this.fotografiaPuntos = fotografiaPuntos;
        return this;
    }

    public PuntoMonitoreo addFotografiaPunto(FotografiaPunto fotografiaPunto) {
        this.fotografiaPuntos.add(fotografiaPunto);
        fotografiaPunto.setPuntoMonitoreo(this);
        return this;
    }

    public PuntoMonitoreo removeFotografiaPunto(FotografiaPunto fotografiaPunto) {
        this.fotografiaPuntos.remove(fotografiaPunto);
        fotografiaPunto.setPuntoMonitoreo(null);
        return this;
    }

    public void setFotografiaPuntos(Set<FotografiaPunto> fotografiaPuntos) {
        this.fotografiaPuntos = fotografiaPuntos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PuntoMonitoreo)) {
            return false;
        }
        return id != null && id.equals(((PuntoMonitoreo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PuntoMonitoreo{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", codigoSede='" + getCodigoSede() + "'" +
            ", codigoCliente='" + getCodigoCliente() + "'" +
            ", coordenadaNorte='" + getCoordenadaNorte() + "'" +
            ", coordenadaEste='" + getCoordenadaEste() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", comentario='" + getComentario() + "'" +
            ", latitud=" + getLatitud() +
            ", longitud=" + getLongitud() +
            ", observacion='" + getObservacion() + "'" +
            "}";
    }
}
