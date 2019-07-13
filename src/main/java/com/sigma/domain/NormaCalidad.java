package com.sigma.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.sigma.domain.enumeration.TipoNorma;

/**
 * A NormaCalidad.
 */
@Entity
@Table(name = "norma_calidad")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NormaCalidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoNorma tipo;

    @Column(name = "fuente")
    private String fuente;

    @Column(name = "ruta_doc_norma")
    private String rutaDocNorma;

    @Column(name = "nombre_doc_norma")
    private String nombreDocNorma;

    @Lob
    @Column(name = "documento_norma")
    private byte[] documentoNorma;

    @Column(name = "documento_norma_content_type")
    private String documentoNormaContentType;

    @OneToMany(mappedBy = "normaCalidad")
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

    public NormaCalidad nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public NormaCalidad codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public NormaCalidad fechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
        return this;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public TipoNorma getTipo() {
        return tipo;
    }

    public NormaCalidad tipo(TipoNorma tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoNorma tipo) {
        this.tipo = tipo;
    }

    public String getFuente() {
        return fuente;
    }

    public NormaCalidad fuente(String fuente) {
        this.fuente = fuente;
        return this;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public String getRutaDocNorma() {
        return rutaDocNorma;
    }

    public NormaCalidad rutaDocNorma(String rutaDocNorma) {
        this.rutaDocNorma = rutaDocNorma;
        return this;
    }

    public void setRutaDocNorma(String rutaDocNorma) {
        this.rutaDocNorma = rutaDocNorma;
    }

    public String getNombreDocNorma() {
        return nombreDocNorma;
    }

    public NormaCalidad nombreDocNorma(String nombreDocNorma) {
        this.nombreDocNorma = nombreDocNorma;
        return this;
    }

    public void setNombreDocNorma(String nombreDocNorma) {
        this.nombreDocNorma = nombreDocNorma;
    }

    public byte[] getDocumentoNorma() {
        return documentoNorma;
    }

    public NormaCalidad documentoNorma(byte[] documentoNorma) {
        this.documentoNorma = documentoNorma;
        return this;
    }

    public void setDocumentoNorma(byte[] documentoNorma) {
        this.documentoNorma = documentoNorma;
    }

    public String getDocumentoNormaContentType() {
        return documentoNormaContentType;
    }

    public NormaCalidad documentoNormaContentType(String documentoNormaContentType) {
        this.documentoNormaContentType = documentoNormaContentType;
        return this;
    }

    public void setDocumentoNormaContentType(String documentoNormaContentType) {
        this.documentoNormaContentType = documentoNormaContentType;
    }

    public Set<Paramentro> getParamentros() {
        return paramentros;
    }

    public NormaCalidad paramentros(Set<Paramentro> paramentros) {
        this.paramentros = paramentros;
        return this;
    }

    public NormaCalidad addParamentro(Paramentro paramentro) {
        this.paramentros.add(paramentro);
        paramentro.setNormaCalidad(this);
        return this;
    }

    public NormaCalidad removeParamentro(Paramentro paramentro) {
        this.paramentros.remove(paramentro);
        paramentro.setNormaCalidad(null);
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
        if (!(o instanceof NormaCalidad)) {
            return false;
        }
        return id != null && id.equals(((NormaCalidad) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "NormaCalidad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", fechaPublicacion='" + getFechaPublicacion() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", fuente='" + getFuente() + "'" +
            ", rutaDocNorma='" + getRutaDocNorma() + "'" +
            ", nombreDocNorma='" + getNombreDocNorma() + "'" +
            ", documentoNorma='" + getDocumentoNorma() + "'" +
            ", documentoNormaContentType='" + getDocumentoNormaContentType() + "'" +
            "}";
    }
}
