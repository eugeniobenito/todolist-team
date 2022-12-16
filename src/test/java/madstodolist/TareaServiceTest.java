package madstodolist;

import madstodolist.controller.TareaData;
import madstodolist.model.Status;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.TareaServiceException;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

import java.text.ParseException;
import java.text.SimpleDateFormat;

// Hemos eliminado todos los @Transactional de los tests
// y usado un script para limpiar la BD de test después de
// cada test
// https://dev.to/henrykeys/don-t-use-transactional-in-tests-40eb

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class TareaServiceTest {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;

    class DosIds {
        Long usuarioId;
        Long tareaId;
        public DosIds(Long usuarioId, Long tareaId) {
            this.usuarioId = usuarioId;
            this.tareaId = tareaId;
        }
    }

    // Método para inicializar los datos de prueba en la BD
    // Devuelve el identificador del usuario y el de la primera tarea añadida
    DosIds addUsuarioTareasBD() {
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        Tarea tarea1 = tareaService.nuevaTareaUsuario(usuario.getId(), "Lavar coche");
        tareaService.nuevaTareaUsuario(usuario.getId(), "Renovar DNI");
        return new DosIds(usuario.getId(), tarea1.getId());
    }

    TareaData crearTareaDTOExample() throws ParseException {
        TareaData tareaDTO = new TareaData();
        tareaDTO.setTitulo("MADS");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tareaDTO.setFechaLimite(sdf.parse("2023-02-20"));
        return tareaDTO;
    }

    @Test
    public void testNuevaTareaUsuario() {
        // GIVEN
        // Un usuario en la BD

        Long usuarioId = addUsuarioTareasBD().usuarioId;

        // WHEN
        // creamos una nueva tarea asociada al usuario,
        Tarea tarea = tareaService.nuevaTareaUsuario(usuarioId, "Práctica 1 de MADS");

        // THEN
        // al recuperar el usuario usando el método findByEmail la tarea creada
        // está en la lista de tareas del usuario.

        Usuario usuario = usuarioService.findByEmail("user@ua");
        assertThat(usuario.getTareas()).hasSize(3);
        assertThat(usuario.getTareas()).contains(tarea);
    }

    @Test
    public void testNuevaTareaConFecha() throws ParseException {
        // GIVEN
        // Un usuario en la BD
        Long usuarioId = addUsuarioTareasBD().usuarioId;

        // WHEN
        // creamos una nueva tarea asociada al usuario
        TareaData tareaDTO = crearTareaDTOExample();
        Tarea tarea = tareaService.nuevaTareaUsuario(usuarioId, tareaDTO);

        // THEN
        // al recuperar el usuario usando el método findByEmail la tarea creada
        // está en la lista de tareas del usuario.
        Usuario usuario = usuarioService.findByEmail("user@ua");
        assertThat(usuario.getTareas()).hasSize(3);
        assertThat(usuario.getTareas()).contains(tarea);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        assertThat(tarea.getFechaLimite()).isEqualTo(sdf.parse("2023-02-20"));
    }

    @Test
    public void testNuevaTareaConFechaPasadaException() throws ParseException {
        // GIVEN
        // Un usuario en la BD
        Long usuarioId = addUsuarioTareasBD().usuarioId;

        // WHEN
        // creamos una nueva tarea asociada al usuario y la fecha de tarea es del pasado
        TareaData tareaDTO = crearTareaDTOExample();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tareaDTO.setFechaLimite(sdf.parse("1997-02-20"));

        // THEN
        // intentamos añadirla, se produce una excepción de tipo TareaServiceException
        Assertions.assertThrows(TareaServiceException.class, () -> {
            tareaService.nuevaTareaUsuario(usuarioId, tareaDTO);
        });
    }

    @Test
    public void testBuscarTarea() {
        // GIVEN
        // Una tarea en la BD

        Long tareaId = addUsuarioTareasBD().tareaId;

        // WHEN
        // recuperamos una tarea de la base de datos a partir de su ID,

        Tarea lavarCoche = tareaService.findById(tareaId);

        // THEN
        // los datos de la tarea recuperada son correctos.

        assertThat(lavarCoche).isNotNull();
        assertThat(lavarCoche.getTitulo()).isEqualTo("Lavar coche");
    }

    @Test
    public void testModificarTarea() throws ParseException {
        // GIVEN
        // Un usuario y una tarea en la BD

        DosIds dosIds = addUsuarioTareasBD();
        Long usuarioId = dosIds.usuarioId;
        Long tareaId = dosIds.tareaId;

        // WHEN
        // modificamos la tarea correspondiente a ese identificador,
        TareaData tareaDTO = crearTareaDTOExample();
        tareaService.modificaTarea(tareaId, tareaDTO);

        // THEN
        // al buscar por el identificador en la base de datos se devuelve la tarea
        // modificada

        Tarea tareaBD = tareaService.findById(tareaId);
        assertThat(tareaBD.getTitulo()).isEqualTo("MADS");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        assertThat(tareaBD.getFechaLimite()).isEqualTo(sdf.parse("2023-02-20"));

        
        // y el usuario tiene también esa tarea modificada.
        Usuario usuarioBD = usuarioService.findById(usuarioId);
        usuarioBD.getTareas().contains(tareaBD);
    }

    @Test
    public void testCambiarStatusTarea() {
        // GIVEN
        // Un usuario y una tarea en la BD
        DosIds dosIds = addUsuarioTareasBD();
        Long usuarioId = dosIds.usuarioId;
        Long tareaId = dosIds.tareaId;

        // WHEN
        // cambiamos el status de la tarea correspondiente a ese identificador
        tareaService.changeStatus(tareaId, Status.IN_PROGRESS);

        // THEN
        // al buscar por el identificador en la base de datos se devuelve la tarea
        // modificada
        Tarea tareaBD = tareaService.findById(tareaId);
        assertThat(tareaBD.getStatus()).isEqualTo(Status.IN_PROGRESS);

        // y el usuario tiene también esa tarea modificada.
        Usuario usuarioBD = usuarioService.findById(usuarioId);
        usuarioBD.getTareas().contains(tareaBD);
    }

    @Test
    public void testModificarTareaConStatusDoneException() throws ParseException {
        // GIVEN
        // Un usuario y una tarea en la BD con status a
        DosIds dosIds = addUsuarioTareasBD();
        Long tareaId = dosIds.tareaId;
        // cambiamos el status de la tarea correspondiente a DONE
        tareaService.changeStatus(tareaId, Status.DONE);

        // WHEN, THEN
        // intentamos modificar los atributos de una tarea con status DONE se lanza
        // excepción de tipo TareaServiceException
        Assertions.assertThrows(TareaServiceException.class, () -> {
            TareaData tareaDTO = crearTareaDTOExample();
            tareaService.modificaTarea(tareaId, tareaDTO);
        });
    }

    @Test
    public void testBorrarTarea() {
        // GIVEN
        // Un usuario y una tarea en la BD

        DosIds dosIds = addUsuarioTareasBD();
        Long usuarioId = dosIds.usuarioId;
        Long tareaId = dosIds.tareaId;

        // WHEN
        // borramos la tarea correspondiente al identificador,

        tareaService.borraTarea(tareaId);

        // THEN
        // la tarea ya no está en la base de datos ni en las tareas del usuario.

        assertThat(tareaService.findById(tareaId)).isNull();
        assertThat(usuarioService.findById(usuarioId).getTareas()).hasSize(1);
    }
}
