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

    public Proyecto(){}

    public Equipo getEquipo() { return this.equipo; }
    public String getNombre() { return this.nombre; }




    @Override
    public int hashCode() { return Objects.hash(id); }
}
