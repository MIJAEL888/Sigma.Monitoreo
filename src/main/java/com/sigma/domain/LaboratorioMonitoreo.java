package com.sigma.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A LaboratorioMonitoreo.
 */
@Entity
@Table(name = "laboratorio_monitoreo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LaboratorioMonitoreo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_laboratorio")
    private String codigoLaboratorio;

    @Column(name = "fecha_reseva")
    private LocalDate fechaReseva;

    @ManyToOne
    @JsonIgnoreProperties("laboratorioMonitoreos")
    private Proyecto proyecto;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoLaboratorio() {
        return codigoLaboratorio;
    }

    public LaboratorioMonitoreo codigoLaboratorio(String codigoLaboratorio) {
        this.codigoLaboratorio = codigoLaboratorio;
        return this;
    }

    public void setCodigoLaboratorio(String codigoLaboratorio) {
        this.codigoLaboratorio = codigoLaboratorio;
    }

    public LocalDate getFechaReseva() {
        return fechaReseva;
    }

    public LaboratorioMonitoreo fechaReseva(LocalDate fechaReseva) {
        this.fechaReseva = fechaReseva;
        return this;
    }

    public void setFechaReseva(LocalDate fechaReseva) {
        this.fechaReseva = fechaReseva;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public LaboratorioMonitoreo proyecto(Proyecto proyecto) {
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
        if (!(o instanceof LaboratorioMonitoreo)) {
            return false;
        }
        return id != null && id.equals(((LaboratorioMonitoreo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LaboratorioMonitoreo{" +
            "id=" + getId() +
            ", codigoLaboratorio='" + getCodigoLaboratorio() + "'" +
            ", fechaReseva='" + getFechaReseva() + "'" +
            "}";
    }
}
