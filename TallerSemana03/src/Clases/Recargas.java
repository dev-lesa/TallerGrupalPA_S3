/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Luis
 */
@Entity
@Table(name = "recargas")
@NamedQueries({
    @NamedQuery(name = "Recargas.findAll", query = "SELECT r FROM Recargas r"),
    @NamedQuery(name = "Recargas.findByIdRecargas", query = "SELECT r FROM Recargas r WHERE r.idRecargas = :idRecargas"),
    @NamedQuery(name = "Recargas.findByValor", query = "SELECT r FROM Recargas r WHERE r.valor = :valor"),
    @NamedQuery(name = "Recargas.findByFechaRecarga", query = "SELECT r FROM Recargas r WHERE r.fechaRecarga = :fechaRecarga")})
public class Recargas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idRecargas")
    private Integer idRecargas;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "valor")
    private BigDecimal valor;
    @Basic(optional = false)
    @Column(name = "fechaRecarga")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRecarga;
    @JoinColumn(name = "idChips", referencedColumnName = "idChips")
    @ManyToOne(optional = false)
    private Chips idChips;

    public Recargas() {
    }

    public Recargas(Integer idRecargas) {
        this.idRecargas = idRecargas;
    }

    public Recargas(Integer idRecargas, BigDecimal valor, Date fechaRecarga) {
        this.idRecargas = idRecargas;
        this.valor = valor;
        this.fechaRecarga = fechaRecarga;
    }

    public Integer getIdRecargas() {
        return idRecargas;
    }

    public void setIdRecargas(Integer idRecargas) {
        this.idRecargas = idRecargas;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getFechaRecarga() {
        return fechaRecarga;
    }

    public void setFechaRecarga(Date fechaRecarga) {
        this.fechaRecarga = fechaRecarga;
    }

    public Chips getIdChips() {
        return idChips;
    }

    public void setIdChips(Chips idChips) {
        this.idChips = idChips;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRecargas != null ? idRecargas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recargas)) {
            return false;
        }
        Recargas other = (Recargas) object;
        if ((this.idRecargas == null && other.idRecargas != null) || (this.idRecargas != null && !this.idRecargas.equals(other.idRecargas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Recargas[ idRecargas=" + idRecargas + " ]";
    }
    
}
