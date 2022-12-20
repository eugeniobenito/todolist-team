package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tareas_proyecto")
public class TareaProyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;

    private Status status;

    public Status getStatus() { return status; }

    public void setStatus(Status status){ this.status = status; }

    public TareaProyecto(String nombre, Proyecto p) {
        this.nombre = nombre;
        this.proyecto = p;
        p.getTareasProyecto().add(this);
        this.status = Status.TODO;
    }

    public Long getId() { return this.id; }
    public TareaProyecto(){}

    public Proyecto getProyecto() { return this.proyecto; }
    public String getNombre() { return this.nombre; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TareaProyecto tareaProyecto = (TareaProyecto) o;
        if(tareaProyecto.getId() == null || this.getId() == null){
            return false;
        }
        return tareaProyecto.getId() == this.getId();
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tareapro_usuario", joinColumns = { @JoinColumn(name = "fk_tareapro") },
            inverseJoinColumns = {@JoinColumn(name = "fk_usuario")})
    Set<Usuario> usuarios = new HashSet<>();

    public void addUsuario(Usuario u) {
        this.usuarios.add(u);
        u.tareasProyecto.add(this);
    }

    public Set<Usuario> getUsuarios() { return this.usuarios; }


    @Override
    public int hashCode() { return Objects.hash(id); }
}
