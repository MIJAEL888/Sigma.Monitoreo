package com.sigma.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A EquipoMonitoreo.
 */
@Entity
@Table(name = "equipo_monitoreo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EquipoMonitoreo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_equipo")
    private String codigoEquipo;

    @Column(name = "reservado_desde")
    private LocalDate reservadoDesde;

    @Column(name = "reservado_hasta")
    private LocalDate reservadoHasta;

    @Column(name = "documento_calibracion")
    private String documentoCalibracion;

    @ManyToOne
    @JsonIgnoreProperties("equipos")
    private Proyecto proyecto;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoEquipo() {
        return codigoEquipo;
    }

    public EquipoMonitoreo codigoEquipo(String codigoEquipo) {
        this.codigoEquipo = codigoEquipo;
        return this;
    }

    public void setCodigoEquipo(String codigoEquipo) {
        this.codigoEquipo = codigoEquipo;
    }

    public LocalDate getReservadoDesde() {
        return reservadoDesde;
    }

    public EquipoMonitoreo reservadoDesde(LocalDate reservadoDesde) {
        this.reservadoDesde = reservadoDesde;
        return this;
    }

    public void setReservadoDesde(LocalDate reservadoDesde) {
        this.reservadoDesde = reservadoDesde;
    }

    public LocalDate getReservadoHasta() {
        return reservadoHasta;
    }

    public EquipoMonitoreo reservadoHasta(LocalDate reservadoHasta) {
        this.reservadoHasta = reservadoHasta;
        return this;
    }

    public void setReservadoHasta(LocalDate reservadoHasta) {
        this.reservadoHasta = reservadoHasta;
    }

    public String getDocumentoCalibracion() {
        return documentoCalibracion;
    }

    public EquipoMonitoreo documentoCalibracion(String documentoCalibracion) {
        this.documentoCalibracion = documentoCalibracion;
        return this;
    }

    public void setDocumentoCalibracion(String documentoCalibracion) {
        this.documentoCalibracion = documentoCalibracion;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public EquipoMonitoreo proyecto(Proyecto proyecto) {
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
        if (!(o instanceof EquipoMonitoreo)) {
            return false;
        }
        return id != null && id.equals(((EquipoMonitoreo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EquipoMonitoreo{" +
            "id=" + getId() +
            ", codigoEquipo='" + getCodigoEquipo() + "'" +
            ", reservadoDesde='" + getReservadoDesde() + "'" +
            ", reservadoHasta='" + getReservadoHasta() + "'" +
            ", documentoCalibracion='" + getDocumentoCalibracion() + "'" +
            "}";
    }
}
