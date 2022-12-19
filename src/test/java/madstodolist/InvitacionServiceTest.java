package madstodolist;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import madstodolist.controller.InvitacionData;
import madstodolist.model.Equipo;
import madstodolist.model.Invitacion;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.InvitacionService;
import madstodolist.service.UsuarioService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class InvitacionServiceTest {
    
    @Autowired
    InvitacionService invitacionService;

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

    private InvitacionData invitacionDTO(Long idEquipo, Long idUsuario) {
        InvitacionData invitacionData = new InvitacionData();
        invitacionData.setIdEquipo(idEquipo);
        invitacionData.setIdUsuario(idUsuario);
        return invitacionData;
    }

    @Test
    public void invitarUsuarioAEquipoOK() {
        // GIVEN
        // Un usuario y un equipo existentes
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);
        Equipo e2 = equipoService.crearEquipo("equipo2", "desc", u);

        // WHEN
        // El usuario y el equipo existen, por lo que se crea la invitación
        invitacionService.invitar(invitacionDTO(e.getId(), u.getId()));
        invitacionService.invitar(invitacionDTO(e2.getId(), u.getId()));
        
        // THEN
        // Al obtenerla, los datos son correctos
        List<Invitacion> invitacionBD = invitacionService.obtenerInvitacionesDelUsuario(u.getId());
        assertThat(invitacionBD.get(0).getUsuarioId()).isEqualTo(u.getId());
        assertThat(invitacionBD.get(0).getEquipoId()).isEqualTo(e.getId());
        assertThat(invitacionBD.get(1).getUsuarioId()).isEqualTo(u.getId());
        assertThat(invitacionBD.get(1).getEquipoId()).isEqualTo(e2.getId());
    }
}
