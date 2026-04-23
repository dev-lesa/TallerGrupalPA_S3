package Clases;

import java.io.Serializable;
import java.math.BigDecimal;
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

@Entity
@Table(name = "chips")
@NamedQueries({
    @NamedQuery(name = "Chips.findAll", query = "SELECT c FROM Chips c"),
    @NamedQuery(name = "Chips.findByIdChips", query = "SELECT c FROM Chips c WHERE c.idChips = :idChips"),
    @NamedQuery(name = "Chips.findByOperadora", query = "SELECT c FROM Chips c WHERE c.operadora = :operadora"),
    @NamedQuery(name = "Chips.findByChipSlot", query = "SELECT c FROM Chips c WHERE c.chipSlot = :chipSlot"),
    @NamedQuery(name = "Chips.findBySaldo", query = "SELECT c FROM Chips c WHERE c.saldo = :saldo"),
    @NamedQuery(name = "Chips.findByNumero", query = "SELECT c FROM Chips c WHERE c.numero = :numero")})
public class Chips implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idChips")
    private Integer idChips;

    @Basic(optional = false)
    @Column(name = "operadora")
    private String operadora;

    @Basic(optional = false)
    @Column(name = "chipSlot")
    private int chipSlot;   

    @Basic(optional = false)
    @Column(name = "saldo")
    private BigDecimal saldo;

    @Basic(optional = false)
    @Column(name = "numero")
    private String numero;

    @JoinColumn(name = "idCelulares", referencedColumnName = "idCelulares")
    @ManyToOne(optional = false)
    private Celulares idCelulares;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idChips")
    private Collection<Recargas> recargasCollection;

    public Chips() {
    }

    public Chips(Integer idChips) {
        this.idChips = idChips;
    }

    public Chips(Integer idChips, String operadora, int chipSlot, BigDecimal saldo, String numero) {
        this.idChips = idChips;
        this.operadora = operadora;
        this.chipSlot = chipSlot;
        this.saldo = saldo;
        this.numero = numero;
    }

    public Integer getIdChips() {
        return idChips;
    }

    public void setIdChips(Integer idChips) {
        this.idChips = idChips;
    }

    public String getOperadora() {
        return operadora;
    }

    public void setOperadora(String operadora) {
        this.operadora = operadora;
    }

    public int getChipSlot() {
        return chipSlot;
    }

    public void setChipSlot(int chipSlot) {
        this.chipSlot = chipSlot;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Celulares getIdCelulares() {
        return idCelulares;
    }

    public void setIdCelulares(Celulares idCelulares) {
        this.idCelulares = idCelulares;
    }

    public Collection<Recargas> getRecargasCollection() {
        return recargasCollection;
    }

    public void setRecargasCollection(Collection<Recargas> recargasCollection) {
        this.recargasCollection = recargasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idChips != null ? idChips.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Chips)) {
            return false;
        }
        Chips other = (Chips) object;
        if ((this.idChips == null && other.idChips != null)
                || (this.idChips != null && !this.idChips.equals(other.idChips))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Chips[ idChips=" + idChips + " ]";
    }
}
