package madstodolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Invitacion;
import madstodolist.model.InvitacionRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import madstodolist.service.UsuarioService;
import madstodolist.service.EquipoService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

import java.util.List;

import org.junit.jupiter.api.Test;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class InvitacionTest {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EquipoService equipoService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EquipoRepository equipoRepository;

    @Autowired
    InvitacionRepository invitacionRepository;

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

    //
    // Tests InvitacionRepository
    //
    @Test
    @Transactional
    public void guardarInvitacionEnBaseDatos() {
        // GIVEN
        // Un equipo creado y un usuario al que invitar
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);

        // WHEN
        // creamos una invitacion y la guardamos
        Invitacion invitacion = new Invitacion(e.getId(), u.getId());
        invitacionRepository.save(invitacion);

        // THEN
        // al obtenerla accedemos a sus atributos correctamente guardados
        List<Invitacion> invitacionBD = invitacionRepository.findByIdUsuario(u.getId());
        assertThat(invitacionBD.get(0).getUsuarioId()).isEqualTo(u.getId());
        assertThat(invitacionBD.get(0).getEquipoId()).isEqualTo(e.getId());
    }

        //
    // Tests InvitacionRepository
    //
    @Test
    @Transactional
    public void usuarioTieneDosInvitacionesDistintas() {
        // GIVEN
        // Dos equipos creados y un usuario al que invitar
        Usuario u = crearUsuario("a@a", false);
        Equipo e1 = crearEquipo(u);
        Equipo e = equipoService.crearEquipo("equipo2", "desc", u);


        // WHEN
        // creamos invitaciones y las guardamos
        Invitacion invitacion = new Invitacion(e.getId(), u.getId());
        invitacionRepository.save(invitacion);
        Invitacion invitacion2 = new Invitacion(e1.getId(), u.getId());
        invitacionRepository.save(invitacion2);

        // THEN
        // al obtenerlas accedemos a sus atributos correctamente guardados
        List<Invitacion> invitacionBD = invitacionRepository.findByIdUsuario(u.getId());
        assertThat(invitacionBD.get(0).getUsuarioId()).isEqualTo(u.getId());
        assertThat(invitacionBD.get(0).getEquipoId()).isEqualTo(e.getId());
        assertThat(invitacionBD.get(1).getUsuarioId()).isEqualTo(u.getId());
        assertThat(invitacionBD.get(1).getEquipoId()).isEqualTo(e1.getId());
    }
}
