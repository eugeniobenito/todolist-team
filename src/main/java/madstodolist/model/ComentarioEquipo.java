package madstodolist.model;

import madstodolist.model.Equipo;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "comentarios_equipo")
public class ComentarioEquipo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String comentario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    private Date fecha;

    public ComentarioEquipo() {}

    public ComentarioEquipo(String comentario, Usuario usuario, Equipo equipo) {
        this.comentario = comentario;
        this.usuario = usuario;
        this.equipo = equipo;
        this.fecha = Date.from(Instant.now());

        // should add to equipo to
        equipo.getComentariosEquipo().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentario() { return this.comentario; }

    public Equipo getEquipo() { return this.equipo; }

    public Usuario getUsuario() {
        return usuario;
    }

    public Date getFecha() { return this.fecha; }



    @Override
    public boolean equals(Object o) {
       if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComentarioEquipo comentarioEquipo = (ComentarioEquipo) o;
        if (id != null && comentarioEquipo.id != null)
            // Si tenemos los ID, comparamos por ID
            return Objects.equals(id, comentarioEquipo.id);
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(comentario, equipo, usuario);
    }
}
