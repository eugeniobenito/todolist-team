package madstodolist.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="invitaciones")
public class Invitacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long idEquipo;
    @NotNull
    private Long idUsuario;

    public Invitacion() {}

    public Invitacion(Long idEquipo, Long idUsuario) {
        this.idEquipo = idEquipo;
        this.idUsuario = idUsuario;
    }

    public Long getUsuarioId() {
        return this.idUsuario;
    }

    public Long getEquipoId() {
        return this.idEquipo;
    }
}
