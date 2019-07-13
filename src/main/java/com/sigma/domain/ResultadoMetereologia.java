package com.sigma.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A ResultadoMetereologia.
 */
@Entity
@Table(name = "resultado_metereologia")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ResultadoMetereologia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha")
    private ZonedDateTime fecha;

    @Column(name = "valor")
    private String valor;

    @Column(name = "valor_decimal")
    private Float valorDecimal;

    @ManyToOne
    @JsonIgnoreProperties("resultadoMetereologias")
    private Resultado resultado;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFecha() {
        return fecha;
    }

    public ResultadoMetereologia fecha(ZonedDateTime fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(ZonedDateTime fecha) {
        this.fecha = fecha;
    }

    public String getValor() {
        return valor;
    }

    public ResultadoMetereologia valor(String valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Float getValorDecimal() {
        return valorDecimal;
    }

    public ResultadoMetereologia valorDecimal(Float valorDecimal) {
        this.valorDecimal = valorDecimal;
        return this;
    }

    public void setValorDecimal(Float valorDecimal) {
        this.valorDecimal = valorDecimal;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public ResultadoMetereologia resultado(Resultado resultado) {
        this.resultado = resultado;
        return this;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResultadoMetereologia)) {
            return false;
        }
        return id != null && id.equals(((ResultadoMetereologia) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ResultadoMetereologia{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", valor='" + getValor() + "'" +
            ", valorDecimal=" + getValorDecimal() +
            "}";
    }
}
