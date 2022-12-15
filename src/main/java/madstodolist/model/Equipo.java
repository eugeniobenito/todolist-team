package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    public Equipo(String nombre){
        this.nombre = nombre;
        this.descripcion = null;
    }

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Usuario admin;


    @OneToMany(mappedBy = "equipo", fetch = FetchType.EAGER)
    Set<ComentarioEquipo> comentariosEquipo = new HashSet<>();

    public void setAdmin(Usuario admin) { this.admin = admin; }

    public Set<ComentarioEquipo> getComentariosEquipo() { return this.comentariosEquipo; }

    public Usuario getAdmin(){ return this.admin; }

    public Equipo(){}

    public String getNombre(){
        return this.nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){ this.id = id; }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "equipo_usuario", joinColumns = { @JoinColumn(name = "fk_equipo") },
            inverseJoinColumns = {@JoinColumn(name = "fk_usuario")})
    Set<Usuario> usuarios = new HashSet<>();

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

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void addUsuario(Usuario usuario) {
        this.getUsuarios().add(usuario);
        usuario.getEquipos().add(this);
    }

    public void removeUsuario(Usuario usuario){
        this.getUsuarios().remove(usuario);
        usuario.getEquipos().remove(usuario);
    }

    public void setNombre(String nombre) { this.nombre = nombre; }
}
