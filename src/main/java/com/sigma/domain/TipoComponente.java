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
 * A TipoComponente.
 */
@Entity
@Table(name = "tipo_componente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoComponente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne
    @JsonIgnoreProperties("tipoComponentes")
    private Componente componente;

    @OneToMany(mappedBy = "tipoComponente")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Paramentro> paramentros = new HashSet<>();

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

    public TipoComponente nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoComponente descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Componente getComponente() {
        return componente;
    }

    public TipoComponente componente(Componente componente) {
        this.componente = componente;
        return this;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    public Set<Paramentro> getParamentros() {
        return paramentros;
    }

    public TipoComponente paramentros(Set<Paramentro> paramentros) {
        this.paramentros = paramentros;
        return this;
    }

    public TipoComponente addParamentro(Paramentro paramentro) {
        this.paramentros.add(paramentro);
        paramentro.setTipoComponente(this);
        return this;
    }

    public TipoComponente removeParamentro(Paramentro paramentro) {
        this.paramentros.remove(paramentro);
        paramentro.setTipoComponente(null);
        return this;
    }

    public void setParamentros(Set<Paramentro> paramentros) {
        this.paramentros = paramentros;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoComponente)) {
            return false;
        }
        return id != null && id.equals(((TipoComponente) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TipoComponente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
