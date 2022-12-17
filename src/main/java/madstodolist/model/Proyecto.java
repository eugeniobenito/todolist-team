package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "proyectos")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    public Proyecto(String nombre, Equipo e) {
        this.nombre = nombre;
        this.equipo = e;
        e.getProyectos().add(this);
    }

    public Long getId() { return this.id; }
    public Proyecto(){}

    public Equipo getEquipo() { return this.equipo; }
    public String getNombre() { return this.nombre; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proyecto proyecto = (Proyecto) o;
        if(proyecto.getId() == null || this.getId() == null){
            return false;
        }
        return proyecto.getId() == this.getId();
    }



    @Override
    public int hashCode() { return Objects.hash(id); }
}
