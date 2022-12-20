package madstodolist;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import madstodolist.controller.InvitacionData;
import madstodolist.model.Equipo;
import madstodolist.model.Invitacion;
import madstodolist.model.InvitacionRepository;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.InvitacionService;
import madstodolist.service.InvitacionServiceException;
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

    @Test
    public void invitarUsuarioNoExistenteAEquipoException() {
        // GIVEN
        // Un usuario no existente y un equipo existente en BD
        Usuario usuarioNoBD = new Usuario("pedro@ua.es");
        usuarioNoBD.setId(8L);
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);

        // WHEN, THEN
        // Intentamos invitar a un usuario que no existe se lanza excepcion
        Assertions.assertThrows(InvitacionServiceException.class, () -> {
            invitacionService.invitar(invitacionDTO(e.getId(), usuarioNoBD.getId()));
        });
    }

    @Test
    public void invitarUsuarioNoExistenteAEquipoNoExistenteException() {
        // GIVEN
        // Un usuario y un equipo no existentes en BD
        Usuario usuarioNoBD = new Usuario("pedro@ua.es");
        usuarioNoBD.setId(8L);
        Equipo equipoNoBD = new Equipo("Equipo");
        equipoNoBD.setId(9L);

        // WHEN, THEN
        // Intentamos invitar a un usuario a un equipo que no existen se lanza excepcion
        Assertions.assertThrows(InvitacionServiceException.class, () -> {
            invitacionService.invitar(invitacionDTO(equipoNoBD.getId(), usuarioNoBD.getId()));
        });
    }

    @Test
    public void enviarInvitacionRepetida() {
        // GIVEN
        // Un usuario y un equipo existentes
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);

        // WHEN
        invitacionService.invitar(invitacionDTO(e.getId(), u.getId()));

        // THEN
        // Intentamos invitar a un usuario a un equipo otra vez se lanza excepcion
        Assertions.assertThrows(InvitacionServiceException.class, () -> {
            invitacionService.invitar(invitacionDTO(e.getId(), u.getId()));
        });
    }

    @Test
    @Transactional
    public void aceptarInvitacionOK() {
        // GIVEN
        // Un usuario con una invitación pendiente
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);
        invitacionService.invitar(invitacionDTO(e.getId(), u.getId()));

        // WHEN
        // El usuario acepta la invitación
        List<Invitacion> invitacion = invitacionService.obtenerInvitacionesDelUsuario(u.getId());
        invitacionService.aceptar(invitacion.get(0));

        // THEN
        // El usuario es miembro del equipo
        Equipo equipoBD = equipoService.recuperarEquipo(e.getId());
        Usuario usuarioBD = usuarioService.findById(u.getId());
        assertThat(equipoBD.getUsuarios()).contains(usuarioBD);
        assertThat(usuarioBD.getEquipos()).contains(equipoBD);
    }

    @Test
    @Transactional
    public void denegarInvitacionOK() {
        // GIVEN
        // Un usuario con una invitación pendiente
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);
        invitacionService.invitar(invitacionDTO(e.getId(), u.getId()));

        // WHEN
        // El usuario acepta la invitación
        List<Invitacion> invitacion = invitacionService.obtenerInvitacionesDelUsuario(u.getId());
        invitacionService.denegar(invitacion.get(0));

        // THEN
        // El usuario es miembro del equipo
        Equipo equipoBD = equipoService.recuperarEquipo(e.getId());
        Usuario usuarioBD = usuarioService.findById(u.getId());
        assertThat(equipoBD.getUsuarios()).doesNotContain(usuarioBD);
        assertThat(usuarioBD.getEquipos()).doesNotContain(equipoBD);
        assertThat(invitacionService.obtenerInvitacionesDelUsuario(usuarioBD.getId())).hasSize(0);
    }

    @Test
    @Transactional
    public void testObtenerInvitacionesConNombreDelEquipo() {
        // GIVEN
        // Un usuario y un equipo
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);

        // WHEN
        // El usuario tiene una invitacion pendiente
        invitacionService.invitar(invitacionDTO(e.getId(), u.getId()));

        // THEN
        // La funcion devuelve el hashmap
        List<Invitacion> invitacion = invitacionService.obtenerInvitacionesDelUsuario(u.getId());
        HashMap<Invitacion, String> invitacionesConNombreDelEquipo = invitacionService.obtenerInvitacionesConNombreDelEquipo(u.getId());
        assertThat(invitacionesConNombreDelEquipo.size()).isEqualTo(1);
        assertThat(invitacionesConNombreDelEquipo.get(invitacion.get(0))).isEqualTo("equipo");
    }
}
