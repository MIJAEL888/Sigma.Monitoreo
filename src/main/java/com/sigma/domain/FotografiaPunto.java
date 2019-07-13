package com.sigma.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A FotografiaPunto.
 */
@Entity
@Table(name = "fotografia_punto")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FotografiaPunto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "ruta")
    private String ruta;

    @Column(name = "extension")
    private String extension;

    @Lob
    @Column(name = "fotografia")
    private byte[] fotografia;

    @Column(name = "fotografia_content_type")
    private String fotografiaContentType;

    @ManyToOne
    @JsonIgnoreProperties("fotografiaPuntos")
    private PuntoMonitoreo puntoMonitoreo;

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

    public FotografiaPunto nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public FotografiaPunto ruta(String ruta) {
        this.ruta = ruta;
        return this;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getExtension() {
        return extension;
    }

    public FotografiaPunto extension(String extension) {
        this.extension = extension;
        return this;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public byte[] getFotografia() {
        return fotografia;
    }

    public FotografiaPunto fotografia(byte[] fotografia) {
        this.fotografia = fotografia;
        return this;
    }

    public void setFotografia(byte[] fotografia) {
        this.fotografia = fotografia;
    }

    public String getFotografiaContentType() {
        return fotografiaContentType;
    }

    public FotografiaPunto fotografiaContentType(String fotografiaContentType) {
        this.fotografiaContentType = fotografiaContentType;
        return this;
    }

    public void setFotografiaContentType(String fotografiaContentType) {
        this.fotografiaContentType = fotografiaContentType;
    }

    public PuntoMonitoreo getPuntoMonitoreo() {
        return puntoMonitoreo;
    }

    public FotografiaPunto puntoMonitoreo(PuntoMonitoreo puntoMonitoreo) {
        this.puntoMonitoreo = puntoMonitoreo;
        return this;
    }

    public void setPuntoMonitoreo(PuntoMonitoreo puntoMonitoreo) {
        this.puntoMonitoreo = puntoMonitoreo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FotografiaPunto)) {
            return false;
        }
        return id != null && id.equals(((FotografiaPunto) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FotografiaPunto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", ruta='" + getRuta() + "'" +
            ", extension='" + getExtension() + "'" +
            ", fotografia='" + getFotografia() + "'" +
            ", fotografiaContentType='" + getFotografiaContentType() + "'" +
            "}";
    }
}
