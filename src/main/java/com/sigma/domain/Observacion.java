package com.sigma.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Observacion.
 */
@Entity
@Table(name = "observacion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Observacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Lob
    @Column(name = "comentario")
    private String comentario;

    @Column(name = "codigo_monitorista")
    private String codigoMonitorista;

    @ManyToOne
    @JsonIgnoreProperties("observacions")
    private Proyecto proyecto;

    @ManyToOne
    @JsonIgnoreProperties("observacions")
    private Componente componente;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Observacion descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComentario() {
        return comentario;
    }

    public Observacion comentario(String comentario) {
        this.comentario = comentario;
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getCodigoMonitorista() {
        return codigoMonitorista;
    }

    public Observacion codigoMonitorista(String codigoMonitorista) {
        this.codigoMonitorista = codigoMonitorista;
        return this;
    }

    public void setCodigoMonitorista(String codigoMonitorista) {
        this.codigoMonitorista = codigoMonitorista;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public Observacion proyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
        return this;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Componente getComponente() {
        return componente;
    }

    public Observacion componente(Componente componente) {
        this.componente = componente;
        return this;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Observacion)) {
            return false;
        }
        return id != null && id.equals(((Observacion) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Observacion{" +
            "id=" + getId() +
            ", descripcion='" + getDescripcion() + "'" +
            ", comentario='" + getComentario() + "'" +
            ", codigoMonitorista='" + getCodigoMonitorista() + "'" +
            "}";
    }
}
