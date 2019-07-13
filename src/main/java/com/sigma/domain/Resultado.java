package com.sigma.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Resultado.
 */
@Entity
@Table(name = "resultado")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Resultado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_inicio")
    private ZonedDateTime fechaInicio;

    @Column(name = "fehca_fin")
    private ZonedDateTime fehcaFin;

    @Column(name = "valor_minimo")
    private String valorMinimo;

    @Column(name = "valor_maximo")
    private String valorMaximo;

    @Column(name = "valor_final")
    private String valorFinal;

    @Column(name = "valor_final_num")
    private Float valorFinalNum;

    @Column(name = "codigo_laboratorio")
    private String codigoLaboratorio;

    @Column(name = "codigo_equipo")
    private String codigoEquipo;

    @OneToMany(mappedBy = "resultado")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ResultadoEmisiones> resultadoEmisiones = new HashSet<>();

    @OneToMany(mappedBy = "resultado")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ResultadoMetereologia> resultadoMetereologias = new HashSet<>();

    @OneToOne(mappedBy = "resultado")
    @JsonIgnore
    private PuntoMonitoreoObs puntoMonitoreoObs;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFechaInicio() {
        return fechaInicio;
    }

    public Resultado fechaInicio(ZonedDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public void setFechaInicio(ZonedDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public ZonedDateTime getFehcaFin() {
        return fehcaFin;
    }

    public Resultado fehcaFin(ZonedDateTime fehcaFin) {
        this.fehcaFin = fehcaFin;
        return this;
    }

    public void setFehcaFin(ZonedDateTime fehcaFin) {
        this.fehcaFin = fehcaFin;
    }

    public String getValorMinimo() {
        return valorMinimo;
    }

    public Resultado valorMinimo(String valorMinimo) {
        this.valorMinimo = valorMinimo;
        return this;
    }

    public void setValorMinimo(String valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public String getValorMaximo() {
        return valorMaximo;
    }

    public Resultado valorMaximo(String valorMaximo) {
        this.valorMaximo = valorMaximo;
        return this;
    }

    public void setValorMaximo(String valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    public String getValorFinal() {
        return valorFinal;
    }

    public Resultado valorFinal(String valorFinal) {
        this.valorFinal = valorFinal;
        return this;
    }

    public void setValorFinal(String valorFinal) {
        this.valorFinal = valorFinal;
    }

    public Float getValorFinalNum() {
        return valorFinalNum;
    }

    public Resultado valorFinalNum(Float valorFinalNum) {
        this.valorFinalNum = valorFinalNum;
        return this;
    }

    public void setValorFinalNum(Float valorFinalNum) {
        this.valorFinalNum = valorFinalNum;
    }

    public String getCodigoLaboratorio() {
        return codigoLaboratorio;
    }

    public Resultado codigoLaboratorio(String codigoLaboratorio) {
        this.codigoLaboratorio = codigoLaboratorio;
        return this;
    }

    public void setCodigoLaboratorio(String codigoLaboratorio) {
        this.codigoLaboratorio = codigoLaboratorio;
    }

    public String getCodigoEquipo() {
        return codigoEquipo;
    }

    public Resultado codigoEquipo(String codigoEquipo) {
        this.codigoEquipo = codigoEquipo;
        return this;
    }

    public void setCodigoEquipo(String codigoEquipo) {
        this.codigoEquipo = codigoEquipo;
    }

    public Set<ResultadoEmisiones> getResultadoEmisiones() {
        return resultadoEmisiones;
    }

    public Resultado resultadoEmisiones(Set<ResultadoEmisiones> resultadoEmisiones) {
        this.resultadoEmisiones = resultadoEmisiones;
        return this;
    }

    public Resultado addResultadoEmisiones(ResultadoEmisiones resultadoEmisiones) {
        this.resultadoEmisiones.add(resultadoEmisiones);
        resultadoEmisiones.setResultado(this);
        return this;
    }

    public Resultado removeResultadoEmisiones(ResultadoEmisiones resultadoEmisiones) {
        this.resultadoEmisiones.remove(resultadoEmisiones);
        resultadoEmisiones.setResultado(null);
        return this;
    }

    public void setResultadoEmisiones(Set<ResultadoEmisiones> resultadoEmisiones) {
        this.resultadoEmisiones = resultadoEmisiones;
    }

    public Set<ResultadoMetereologia> getResultadoMetereologias() {
        return resultadoMetereologias;
    }

    public Resultado resultadoMetereologias(Set<ResultadoMetereologia> resultadoMetereologias) {
        this.resultadoMetereologias = resultadoMetereologias;
        return this;
    }

    public Resultado addResultadoMetereologia(ResultadoMetereologia resultadoMetereologia) {
        this.resultadoMetereologias.add(resultadoMetereologia);
        resultadoMetereologia.setResultado(this);
        return this;
    }

    public Resultado removeResultadoMetereologia(ResultadoMetereologia resultadoMetereologia) {
        this.resultadoMetereologias.remove(resultadoMetereologia);
        resultadoMetereologia.setResultado(null);
        return this;
    }

    public void setResultadoMetereologias(Set<ResultadoMetereologia> resultadoMetereologias) {
        this.resultadoMetereologias = resultadoMetereologias;
    }

    public PuntoMonitoreoObs getPuntoMonitoreoObs() {
        return puntoMonitoreoObs;
    }

    public Resultado puntoMonitoreoObs(PuntoMonitoreoObs puntoMonitoreoObs) {
        this.puntoMonitoreoObs = puntoMonitoreoObs;
        return this;
    }

    public void setPuntoMonitoreoObs(PuntoMonitoreoObs puntoMonitoreoObs) {
        this.puntoMonitoreoObs = puntoMonitoreoObs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resultado)) {
            return false;
        }
        return id != null && id.equals(((Resultado) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Resultado{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fehcaFin='" + getFehcaFin() + "'" +
            ", valorMinimo='" + getValorMinimo() + "'" +
            ", valorMaximo='" + getValorMaximo() + "'" +
            ", valorFinal='" + getValorFinal() + "'" +
            ", valorFinalNum=" + getValorFinalNum() +
            ", codigoLaboratorio='" + getCodigoLaboratorio() + "'" +
            ", codigoEquipo='" + getCodigoEquipo() + "'" +
            "}";
    }
}
