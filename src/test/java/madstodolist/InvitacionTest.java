package madstodolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import madstodolist.service.EquipoService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

import org.junit.jupiter.api.Test;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class InvitacionTest {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EquipoService equipoService;

    private Usuario crearUsuario(String email, Boolean isAdmin) {
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario.setIsAdmin(false);
        usuario.setBlocked(false);
        return usuarioService.registrar(usuario);
    }

    private Equipo crearEquipo(Usuario u) {
        Equipo e = equipoService.crearEquipo("equipo", "desc", u);
        return e;
    }

    //
    // Tests modelo Invitacion en memoria, sin la conexión con la BD
    //
    @Test
    public void crearInvitacion() {
        // GIVEN
        // Un equipo creado y un usuario al que invitar
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);

        // WHEN
        // se crear una invitación
        Invitacion invitacion = new Invitacion(e.getId(), u.getId());

        // THEN
        // el equipo y el usuario de la tarea son los correctos.
        assertThat(invitacion.getUsuarioId()).isEqualTo(u.getId());
        assertThat(invitacion.getEquipoId()).isEqualTo(e.getId());
    }

}
