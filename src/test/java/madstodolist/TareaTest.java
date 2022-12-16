package madstodolist;

import madstodolist.model.Status;
import madstodolist.model.Tarea;
import madstodolist.model.TareaRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class TareaTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TareaRepository tareaRepository;

    //
    // Tests modelo Tarea en memoria, sin la conexión con la BD
    //

    @Test
    public void crearTarea() {
        // GIVEN
        // Un usuario nuevo creado en memoria, sin conexión con la BD,

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN
        // se crea una nueva tarea con ese usuario,

        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

        // THEN
        // el título y el usuario de la tarea son los correctos.

        assertThat(tarea.getTitulo()).isEqualTo("Práctica 1 de MADS");
        assertThat(tarea.getUsuario()).isEqualTo(usuario);
    }

    @Test
    public void crearTareaConFecha() {
        // GIVEN
        // Un usuario nuevo creado en memoria, sin conexión con la BD
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN
        // se crea una nueva tarea con ese usuario
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

        // THEN
        // la fecha es null
        assertThat(tarea.getFechaLimite()).isNull();
    }

    @Test
    public void crearTareaConFechaNotNull() throws Exception {
        // GIVEN
        // Un usuario nuevo creado en memoria, sin conexión con la BD
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN
        // se crea una nueva tarea con ese usuario
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

        // se le asigna una fecha límite
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tarea.setFechaLimite(sdf.parse("1997-02-20"));

        // THEN
        // la fecha es la que se ha asignado y no null
        // assertThat(tarea.getFechaLimite()).isNotNull();
        assertThat(tarea.getFechaLimite()).isEqualTo(sdf.parse("1997-02-20"));
    }

    @Test
    public void crearTareaConStatusAToDo() {
        // GIVEN
        // Un usuario nuevo creado en memoria, sin conexión con la BD
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN
        // se crea una nueva tarea con ese usuario
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

        // THEN
        // el estado es 'ToDo' por defecto cuando se crea una tarea nueva
        assertThat(tarea.getStatus()).isEqualTo(Status.TODO);
    }

    @Test
    public void cambiarStatusTareaAInProgress() {
        // GIVEN
        // Un usuario nuevo creado y una tarea asignada
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

        // WHEN
        // se cambia el estado a 'InProgress'
        tarea.changeStatus(Status.IN_PROGRESS);

        // THEN
        // el estado es 'In Progress'
        assertThat(tarea.getStatus()).isEqualTo(Status.IN_PROGRESS);
    }

    @Test
    public void cambiarStatusTareaADoneMarcaFechaLimiteANull() throws ParseException {
        // GIVEN
        // Un usuario nuevo creado y una tarea asignada
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tarea.setFechaLimite(sdf.parse("2030-02-20"));

        // WHEN
        // se cambia el estado a 'Done'
        tarea.changeStatus(Status.DONE);

        // THEN
        // el estado es 'Done' y automáticamente se settea la fecha límite a null
        assertThat(tarea.getStatus()).isEqualTo(Status.DONE);
        assertThat(tarea.getFechaLimite()).isNull();
    }

    @Test
    public void laListaDeTareasDeUnUsuarioSeActualizaEnMemoriaConUnaNuevaTarea() {
        // GIVEN
        // Un usuario nuevo creado en memoria, sin conexión con la BD,

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN
        // se crea una tarea de ese usuario,

        Set<Tarea> tareas = usuario.getTareas();
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

        // THEN
        // la tarea creada se ha añadido a la lista de tareas del usuario.

        assertThat(usuario.getTareas()).contains(tarea);
        assertThat(tareas).contains(tarea);
    }

    @Test
    public void comprobarIgualdadTareasSinId() {
        // GIVEN
        // Creadas tres tareas sin identificador, y dos de ellas con
        // la misma descripción

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea1 = new Tarea(usuario, "Práctica 1 de MADS");
        Tarea tarea2 = new Tarea(usuario, "Práctica 1 de MADS");
        Tarea tarea3 = new Tarea(usuario, "Pagar el alquiler");

        // THEN
        // son iguales (Equal) las tareas que tienen la misma descripción.

        assertThat(tarea1).isEqualTo(tarea2);
        assertThat(tarea1).isNotEqualTo(tarea3);
    }

    @Test
    public void comprobarIgualdadTareasConId() {
        // GIVEN
        // Creadas tres tareas con distintas descripciones y dos de ellas
        // con el mismo identificador,

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea1 = new Tarea(usuario, "Práctica 1 de MADS");
        Tarea tarea2 = new Tarea(usuario, "Lavar la ropa");
        Tarea tarea3 = new Tarea(usuario, "Pagar el alquiler");
        tarea1.setId(1L);
        tarea2.setId(2L);
        tarea3.setId(1L);

        // THEN
        // son iguales (Equal) las tareas que tienen el mismo identificador.

        assertThat(tarea1).isEqualTo(tarea3);
        assertThat(tarea1).isNotEqualTo(tarea2);
    }

    //
    // Tests TareaRepository.
    // El código que trabaja con repositorios debe
    // estar en un entorno transactional, para que todas las peticiones
    // estén en la misma conexión a la base de datos, las entidades estén
    // conectadas y sea posible acceder a colecciones LAZY.
    //

    @Test
    @Transactional
    public void guardarTareaEnBaseDatos() {
        // GIVEN
        // Un usuario en la base de datos.

        Usuario usuario = new Usuario("user@ua");
        usuarioRepository.save(usuario);

        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

        // WHEN
        // salvamos la tarea en la BD,

        tareaRepository.save(tarea);

        // THEN
        // se actualiza el id de la tarea,

        assertThat(tarea.getId()).isNotNull();

        // y con ese identificador se recupera de la base de datos la tarea
        // con los valores correctos de las propiedades y la relación con
        // el usuario actualizado también correctamente (la relación entre tarea
        // y usuario es EAGER).

        Tarea tareaBD = tareaRepository.findById(tarea.getId()).orElse(null);
        assertThat(tareaBD.getTitulo()).isEqualTo(tarea.getTitulo());
        assertThat(tareaBD.getUsuario()).isEqualTo(usuario);
        assertThat(tareaBD.getStatus()).isEqualTo(tarea.getStatus());
    }

    @Test
    @Transactional
    public void salvarTareaEnBaseDatosConUsuarioNoBDLanzaExcepcion() {
        // GIVEN
        // Un usuario nuevo que no está en la BD
        // y una tarea asociada a ese usuario,

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

        // WHEN // THEN
        // se lanza una excepción al intentar salvar la tarea en la BD

        Assertions.assertThrows(Exception.class, () -> {
            tareaRepository.save(tarea);
        });
    }

    @Test
    @Transactional
    public void unUsuarioTieneUnaListaDeTareas() {
        // GIVEN
        // Un usuario con 2 tareas en la base de datos
        Usuario usuario = new Usuario("user@ua");
        usuarioRepository.save(usuario);
        Long usuarioId = usuario.getId();

        Tarea tarea1 = new Tarea(usuario, "Práctica 1 de MADS");
        Tarea tarea2 = new Tarea(usuario, "Renovar el DNI");
        tareaRepository.save(tarea1);
        tareaRepository.save(tarea2);

        // WHEN
        // recuperamos el ususario de la base de datos,

        Usuario usuarioRecuperado = usuarioRepository.findById(usuarioId).orElse(null);

        // THEN
        // su lista de tareas también se recupera, porque se ha
        // definido la relación de usuario y tareas como EAGER.

        assertThat(usuarioRecuperado.getTareas()).hasSize(2);
    }

    @Test
    @Transactional
    public void añadirUnaTareaAUnUsuarioEnBD() {
        // GIVEN
        // Un usuario en la base de datos
        Usuario usuario = new Usuario("user@ua");
        usuarioRepository.save(usuario);
        Long usuarioId = usuario.getId();

        // WHEN
        // Creamos una nueva tarea con el usuario recuperado de la BD
        // y la salvamos,

        Usuario usuarioBD = usuarioRepository.findById(usuarioId).orElse(null);
        Tarea tarea = new Tarea(usuarioBD, "Práctica 1 de MADS");
        tareaRepository.save(tarea);
        Long tareaId = tarea.getId();

        // THEN
        // la tarea queda guardada en la BD asociada al usuario

        Tarea tareaBD = tareaRepository.findById(tareaId).orElse(null);
        assertThat(tareaBD).isEqualTo(tarea);
        assertThat(tarea.getUsuario()).isEqualTo(usuarioBD);

        // y si recuperamos el usuario se obtiene la nueva tarea
        usuarioBD = usuarioRepository.findById(usuarioId).orElse(null);
        assertThat(usuarioBD.getTareas()).contains(tareaBD);
    }

    @Test
    @Transactional
    public void cambioEnLaEntidadEnTransactionalModificaLaBD() {
        // GIVEN
        // Un usuario y una tarea en la base de datos
        Usuario usuario = new Usuario("user@ua");
        usuarioRepository.save(usuario);
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");
        tareaRepository.save(tarea);

        // Recuperamos la tarea
        Long tareaId = tarea.getId();
        tarea = tareaRepository.findById(tareaId).orElse(null);

        // WHEN
        // modificamos la descripción de la tarea

        tarea.setTitulo("Esto es una prueba");

        // THEN
        // la descripción queda actualizada en la BD.

        Tarea tareaBD = tareaRepository.findById(tareaId).orElse(null);
        assertThat(tareaBD.getTitulo()).isEqualTo(tarea.getTitulo());
    }

    @Test
    @Transactional
    public void guardarTareaEnBDConFechaNull() {
        // GIVEN
        // Un usuario y una tarea en la base de datos
        Usuario usuario = new Usuario("user@ua");
        usuarioRepository.save(usuario);
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");
        tareaRepository.save(tarea);

        // WHEN
        // Recuperamos la tarea
        Long tareaId = tarea.getId();
        tarea = tareaRepository.findById(tareaId).orElse(null);

        // THEN
        // la fecha es null
        Tarea tareaBD = tareaRepository.findById(tareaId).orElse(null);
        assertThat(tareaBD.getFechaLimite()).isNull();
    }

    @Test
    @Transactional
    public void guardarTareaEnBDConFechaNotNull() throws ParseException {
        // GIVEN
        // Un usuario y una tarea con fecha en la base de datos
        Usuario usuario = new Usuario("user@ua");
        usuarioRepository.save(usuario);
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");
        // se le asigna una fecha límite
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tarea.setFechaLimite(sdf.parse("1997-02-20"));
        tareaRepository.save(tarea);

        // WHEN
        // Recuperamos la tarea
        Long tareaId = tarea.getId();
        Tarea tareaBD = tareaRepository.findById(tareaId).orElse(null);

        // THEN
        // la fecha es la que hemos asignado
        assertThat(tareaBD.getFechaLimite()).isNotNull();
        assertThat(tareaBD.getFechaLimite()).isEqualTo(sdf.parse("1997-02-20"));
    }

    @Test
    public void guardarStatusTareaADoneMarcaFechaLimiteANull() throws ParseException {
        // GIVEN
        // Un usuario y una tarea en la base de datos
        Usuario usuario = new Usuario("user@ua");
        usuarioRepository.save(usuario);
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tarea.setFechaLimite(sdf.parse("2030-02-20"));
        tareaRepository.save(tarea);

        // WHEN
        // Recuperamos la tarea y cambiamos status a DONE
        Long tareaId = tarea.getId();
        Tarea tareaBD = tareaRepository.findById(tareaId).orElse(null);
        assertThat(tareaBD.getStatus()).isEqualTo(Status.TODO);
        tareaBD.changeStatus(Status.DONE);
        tareaRepository.save(tareaBD);

        // THEN
        // la fecha es null y el status ha cambiado
        Tarea tareaDONE = tareaRepository.findById(tarea.getId()).orElse(null);

        assertThat(tareaDONE.getFechaLimite()).isNull();
        assertThat(tareaDONE.getStatus()).isEqualTo(Status.DONE);
    }
}
