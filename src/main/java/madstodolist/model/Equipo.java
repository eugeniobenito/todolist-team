package madstodolist.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    public Equipo(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return this.nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){ this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        if(equipo.getId() == null || this.getId() == null){
            return equipo.getNombre() == this.getNombre();
        }
        return equipo.getId() == this.getId();
    }

    @Override
    public int hashCode() {
        // Generamos un hash basado en los campos obligatorios
        return Objects.hash(id);
    }
}
