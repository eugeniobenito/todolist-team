package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.EquipoServiceException;
import madstodolist.service.UsuarioService;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class EquipoServiceTest {

    @Autowired
    EquipoService equipoService;

    @Autowired
    UsuarioService usuarioService;

    @Test
    public void crearRecuperarEquipo() {
        Equipo equipo = equipoService.crearEquipo("Proyecto 1");
        Equipo equipoBd = equipoService.recuperarEquipo(equipo.getId());
        assertThat(equipoBd).isNotNull();
        assertThat(equipoBd.getNombre()).isEqualTo("Proyecto 1");
    }

    @Test
    public void crearEquipoConDescripcion() {
        Equipo equipo = equipoService.crearEquipo("Proyecto 1", "Equipo de la asignatura MADS");
        Equipo equipoBd = equipoService.recuperarEquipo(equipo.getId());
        assertThat(equipoBd).isNotNull();
        assertThat(equipoBd.getNombre()).isEqualTo("Proyecto 1");
        assertThat(equipoBd.getDescripcion()).isEqualTo("Equipo de la asignatura MADS");
    }

    @Test
    public void listadoEquiposOrdenAlfabetico() {
        // GIVEN
        // Dos equipos en la base de datos
        equipoService.crearEquipo("Proyecto BBB");
        equipoService.crearEquipo("Proyecto AAA");

        // WHEN
        // Recuperamos los equipos
        List<Equipo> equipos = equipoService.findAllOrderedByName();

        // THEN
        // Los equipos están ordenados por nombre
        assertThat(equipos).hasSize(2);
        assertThat(equipos.get(0).getNombre()).isEqualTo("Proyecto AAA");
        assertThat(equipos.get(1).getNombre()).isEqualTo("Proyecto BBB");
    }

    @Test
    public void accesoUsuariosGeneraExcepcion() {
        // Given
        // Un equipo en la base de datos
        Equipo equipo = equipoService.crearEquipo("Proyecto 1");

        // WHEN
        // Se recupera el equipo
        Equipo equipoBd = equipoService.recuperarEquipo(equipo.getId());

        // THEN
        // Se produce una excepción al intentar acceder a sus usuarios
        assertThatThrownBy(() -> {
            equipoBd.getUsuarios().size();
        }).isInstanceOf(LazyInitializationException.class);
    }

    @Test
    public void actualizarRecuperarUsuarioEquipo() {
        // GIVEN
        // Un equipo creado en la base de datos y un usuario registrado
        Equipo equipo = equipoService.crearEquipo("Proyecto 1");
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);

        // WHEN
        // Añadimos el usuario al equipo y lo recuperamos
        equipoService.addUsuarioEquipo(usuario.getId(), equipo.getId());
        List<Usuario> usuarios = equipoService.usuariosEquipo(equipo.getId());

        // THEN
        // El usuario se ha recuperado correctamente
        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0).getEmail()).isEqualTo("user@ua");
    }

    @Test
    public void comprobarRelacionUsuarioEquipos() {
        // GIVEN
        // Un equipo creado en la base de datos y un usuario registrado
        Equipo equipo = equipoService.crearEquipo("Proyecto 1");
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);

        // WHEN
        // Añadimos el usuario al equipo y lo recuperamos
        equipoService.addUsuarioEquipo(usuario.getId(), equipo.getId());
        Usuario usuarioBD = usuarioService.findById(usuario.getId());

        // THEN
        // Se recuperan también los equipos del usuario,
        // porque la relación entre usuarios y equipos es EAGER
        assertThat(usuarioBD.getEquipos()).hasSize(1);
    }

    @Test
    public void crearEquipoServiceNoPuedeSerVacio(){
        EquipoServiceException e = assertThrows(EquipoServiceException.class, () -> equipoService.crearEquipo(""));
        assertThat(e.getMessage()).isEqualTo("El nombre del equipo no puede estar vacio");
    }

    @Test
    public void addUsuarioToEquipoWhoNotExistsException() {
        EquipoServiceException e = assertThrows(EquipoServiceException.class, () -> equipoService.addUsuarioEquipo(new Long(1), new Long( 20)));
        assertThat(e.getMessage()).isEqualTo("No existe el equipo con id 20");

    }

    @Test
    public void addUsuarioToEquipoWhoUsuarioNotExistsException() {
        Equipo equipo = equipoService.crearEquipo("don");
        EquipoServiceException e = assertThrows(EquipoServiceException.class, () -> equipoService.addUsuarioEquipo(new Long(20), equipo.getId()));
        assertThat(e.getMessage()).isEqualTo("No existe el usuario con id 20");

    }

    @Test
    public void comprobarRelacionUsuarioEquipos2() {

        Equipo equipo = equipoService.crearEquipo("Proyecto 1");
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);

        equipoService.addUsuarioEquipo(usuario.getId(), equipo.getId());
        Usuario usuarioBD = usuarioService.findById(usuario.getId());

        assertThat(usuarioBD.getEquipos()).hasSize(1);

        equipoService.removeUsuarioEquipo(usuario.getId(), equipo.getId());

        usuarioBD = usuarioService.findById(usuario.getId());
        assertThat(usuarioBD.getEquipos()).hasSize(0);
    }

    @Test
    public void removeUsuarioFromEquipoThrowsExceptionWhenObjectNotFound(){
        EquipoServiceException e = assertThrows(EquipoServiceException.class, () -> equipoService.removeUsuarioEquipo(new Long(1), new Long( 20)));
        assertThat(e.getMessage()).isEqualTo("No existe el equipo con id 20");
        Equipo equipo = equipoService.crearEquipo("don");
        e = assertThrows(EquipoServiceException.class, () -> equipoService.removeUsuarioEquipo(new Long(20), equipo.getId()));
        assertThat(e.getMessage()).isEqualTo("No existe el usuario con id 20");
    }

    @Test
    public void editarEquipo() {
        Equipo equipo = equipoService.crearEquipo("Proyecto 1");
        equipo = equipoService.modificarEquipo(equipo.getId(), "Proyecto 2");

        assertThat(equipo.getNombre()).isEqualTo("Proyecto 2");
    }

    @Test
    public void editarDescripcionEquipo() {
        // GIVEN
        // Un equipo creado en la base de datos sin descripcion
        Equipo equipo = equipoService.crearEquipo("Proyecto 1");

        // WHEN 
        // Añadimos una descripción
        equipo = equipoService.modificarEquipo(equipo.getId(), "Proyecto 1", "Equipo de la asignatura MADS");

        // THEN
        // Se ha actualizado la descripción del equipo
        assertThat(equipo.getDescripcion()).isEqualTo("Equipo de la asignatura MADS");
    }


    @Test
    public void editarEquipoThrowsExceptions() {
        EquipoServiceException e = assertThrows(EquipoServiceException.class, () -> equipoService.modificarEquipo(new Long(100), ""));
        assertThat(e.getMessage()).isEqualTo("No existe el equipo con id 100");

        Equipo equipo = equipoService.crearEquipo("Proyecto 1");
        e = assertThrows(EquipoServiceException.class, () -> equipoService.modificarEquipo(equipo.getId(), ""));
        assertThat(e.getMessage()).isEqualTo("El nombre no puede estar vacio");

    }

    @Test
    public void eliminarEquipo() {
        Equipo equipo = equipoService.crearEquipo("Proyecto 1");
        Long equipoId = equipo.getId();
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);

        equipoService.addUsuarioEquipo(usuario.getId(), equipo.getId());
        equipoService.eliminarEquipo(equipoId);
        equipo = equipoService.recuperarEquipo(equipoId);
        assertThat(equipo).isNull();
        usuario = usuarioService.findById(usuario.getId());
        assertThat(usuario.getEquipos()).hasSize(0);
    }

    @Test
    public void eliminarEquipoThrowsException() {
        EquipoServiceException e = assertThrows(EquipoServiceException.class, () -> equipoService.eliminarEquipo(new Long(100)));
        assertThat(e.getMessage()).isEqualTo("No existe el equipo con id 100");
    }
}