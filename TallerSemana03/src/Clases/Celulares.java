/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Luis
 */
@Entity
@Table(name = "celulares")
@NamedQueries({
    @NamedQuery(name = "Celulares.findAll", query = "SELECT c FROM Celulares c"),
    @NamedQuery(name = "Celulares.findByIdCelulares", query = "SELECT c FROM Celulares c WHERE c.idCelulares = :idCelulares"),
    @NamedQuery(name = "Celulares.findByMarca", query = "SELECT c FROM Celulares c WHERE c.marca = :marca"),
    @NamedQuery(name = "Celulares.findByModelo", query = "SELECT c FROM Celulares c WHERE c.modelo = :modelo")})
public class Celulares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCelulares")
    private Integer idCelulares;
    @Basic(optional = false)
    @Column(name = "marca")
    private String marca;
    @Basic(optional = false)
    @Column(name = "modelo")
    private String modelo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCelulares")
    private Collection<Chips> chipsCollection;
    @JoinColumn(name = "idUsuarios", referencedColumnName = "idUsuarios")
    @ManyToOne(optional = false)
    private Usuarios idUsuarios;

    public Celulares() {
    }

    public Celulares(Integer idCelulares) {
        this.idCelulares = idCelulares;
    }

    public Celulares(Integer idCelulares, String marca, String modelo) {
        this.idCelulares = idCelulares;
        this.marca = marca;
        this.modelo = modelo;
    }

    public Integer getIdCelulares() {
        return idCelulares;
    }

    public void setIdCelulares(Integer idCelulares) {
        this.idCelulares = idCelulares;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Collection<Chips> getChipsCollection() {
        return chipsCollection;
    }

    public void setChipsCollection(Collection<Chips> chipsCollection) {
        this.chipsCollection = chipsCollection;
    }

    public Usuarios getIdUsuarios() {
        return idUsuarios;
    }

    public void setIdUsuarios(Usuarios idUsuarios) {
        this.idUsuarios = idUsuarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCelulares != null ? idCelulares.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Celulares)) {
            return false;
        }
        Celulares other = (Celulares) object;
        if ((this.idCelulares == null && other.idCelulares != null) || (this.idCelulares != null && !this.idCelulares.equals(other.idCelulares))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Celulares[ idCelulares=" + idCelulares + " ]";
    }
    
}
