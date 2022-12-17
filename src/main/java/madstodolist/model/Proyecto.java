package madstodolist.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "proyectos")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    public String getNombre() { return this.nombre; }

    public Proyecto(String nombre) { this.nombre = nombre; }

    public Proyecto(){}


    @Override
    public int hashCode() { return Objects.hash(id); }
}
