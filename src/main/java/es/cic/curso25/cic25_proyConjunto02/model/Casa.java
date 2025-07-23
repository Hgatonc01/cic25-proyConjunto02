package es.cic.curso25.cic25_proyConjunto02.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Casa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String direccion;
    private long valorCatastral;
    private long cargas;
    @JsonIgnore
    @OneToOne(mappedBy = "casa")
    private Persona persona;

    public Casa() {

    }

    public Casa(Long id, String direccion, long valorCatastral, long cargas) {
        this.id = id;
        this.direccion = direccion;
        this.valorCatastral = valorCatastral;
        this.cargas = cargas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public long getValorCatastral() {
        return valorCatastral;
    }

    public void setValorCatastral(long valorCatastral) {
        this.valorCatastral = valorCatastral;
    }

    public long getCargas() {
        return cargas;
    }

    public void setCargas(long cargas) {
        this.cargas = cargas;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Casa other = (Casa) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Casa [id=" + id + ", direccion=" + direccion + "]";
    }

    

    

}
