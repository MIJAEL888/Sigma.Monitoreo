package com.sigma.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Componente.
 */
@Entity
@Table(name = "componente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Componente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Lob
    @Column(name = "protocolo")
    private String protocolo;

    @Column(name = "guia")
    private String guia;

    @Column(name = "iso")
    private String iso;

    @Lob
    @Column(name = "objetivos")
    private String objetivos;

    @OneToMany(mappedBy = "componente")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TipoComponente> tipoComponentes = new HashSet<>();

    @OneToMany(mappedBy = "componente")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Observacion> observacions = new HashSet<>();

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

    public Componente nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Componente descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public Componente protocolo(String protocolo) {
        this.protocolo = protocolo;
        return this;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getGuia() {
        return guia;
    }

    public Componente guia(String guia) {
        this.guia = guia;
        return this;
    }

    public void setGuia(String guia) {
        this.guia = guia;
    }

    public String getIso() {
        return iso;
    }

    public Componente iso(String iso) {
        this.iso = iso;
        return this;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public Componente objetivos(String objetivos) {
        this.objetivos = objetivos;
        return this;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public Set<TipoComponente> getTipoComponentes() {
        return tipoComponentes;
    }

    public Componente tipoComponentes(Set<TipoComponente> tipoComponentes) {
        this.tipoComponentes = tipoComponentes;
        return this;
    }

    public Componente addTipoComponente(TipoComponente tipoComponente) {
        this.tipoComponentes.add(tipoComponente);
        tipoComponente.setComponente(this);
        return this;
    }

    public Componente removeTipoComponente(TipoComponente tipoComponente) {
        this.tipoComponentes.remove(tipoComponente);
        tipoComponente.setComponente(null);
        return this;
    }

    public void setTipoComponentes(Set<TipoComponente> tipoComponentes) {
        this.tipoComponentes = tipoComponentes;
    }

    public Set<Observacion> getObservacions() {
        return observacions;
    }

    public Componente observacions(Set<Observacion> observacions) {
        this.observacions = observacions;
        return this;
    }

    public Componente addObservacion(Observacion observacion) {
        this.observacions.add(observacion);
        observacion.setComponente(this);
        return this;
    }

    public Componente removeObservacion(Observacion observacion) {
        this.observacions.remove(observacion);
        observacion.setComponente(null);
        return this;
    }

    public void setObservacions(Set<Observacion> observacions) {
        this.observacions = observacions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Componente)) {
            return false;
        }
        return id != null && id.equals(((Componente) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Componente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", protocolo='" + getProtocolo() + "'" +
            ", guia='" + getGuia() + "'" +
            ", iso='" + getIso() + "'" +
            ", objetivos='" + getObjetivos() + "'" +
            "}";
    }
}
