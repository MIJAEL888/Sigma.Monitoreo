package com.sigma.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ResultadoEmisiones.
 */
@Entity
@Table(name = "resultado_emisiones")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ResultadoEmisiones implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_equipo")
    private String tipoEquipo;

    @Column(name = "combustible")
    private String combustible;

    @Column(name = "consumo")
    private Float consumo;

    @Column(name = "hora_por_mes")
    private Float horaPorMes;

    @Column(name = "altura")
    private Float altura;

    @Column(name = "diametro")
    private Float diametro;

    @Column(name = "seccion")
    private String seccion;

    @ManyToOne
    @JsonIgnoreProperties("resultadoEmisiones")
    private Resultado resultado;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public ResultadoEmisiones tipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
        return this;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public String getCombustible() {
        return combustible;
    }

    public ResultadoEmisiones combustible(String combustible) {
        this.combustible = combustible;
        return this;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public Float getConsumo() {
        return consumo;
    }

    public ResultadoEmisiones consumo(Float consumo) {
        this.consumo = consumo;
        return this;
    }

    public void setConsumo(Float consumo) {
        this.consumo = consumo;
    }

    public Float getHoraPorMes() {
        return horaPorMes;
    }

    public ResultadoEmisiones horaPorMes(Float horaPorMes) {
        this.horaPorMes = horaPorMes;
        return this;
    }

    public void setHoraPorMes(Float horaPorMes) {
        this.horaPorMes = horaPorMes;
    }

    public Float getAltura() {
        return altura;
    }

    public ResultadoEmisiones altura(Float altura) {
        this.altura = altura;
        return this;
    }

    public void setAltura(Float altura) {
        this.altura = altura;
    }

    public Float getDiametro() {
        return diametro;
    }

    public ResultadoEmisiones diametro(Float diametro) {
        this.diametro = diametro;
        return this;
    }

    public void setDiametro(Float diametro) {
        this.diametro = diametro;
    }

    public String getSeccion() {
        return seccion;
    }

    public ResultadoEmisiones seccion(String seccion) {
        this.seccion = seccion;
        return this;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public ResultadoEmisiones resultado(Resultado resultado) {
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
        if (!(o instanceof ResultadoEmisiones)) {
            return false;
        }
        return id != null && id.equals(((ResultadoEmisiones) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ResultadoEmisiones{" +
            "id=" + getId() +
            ", tipoEquipo='" + getTipoEquipo() + "'" +
            ", combustible='" + getCombustible() + "'" +
            ", consumo=" + getConsumo() +
            ", horaPorMes=" + getHoraPorMes() +
            ", altura=" + getAltura() +
            ", diametro=" + getDiametro() +
            ", seccion='" + getSeccion() + "'" +
            "}";
    }
}
