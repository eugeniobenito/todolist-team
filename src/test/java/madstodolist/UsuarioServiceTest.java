package madstodolist;

import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import madstodolist.service.UsuarioServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    // Método para inicializar los datos de prueba en la BD
    // Devuelve el identificador del usuario de la BD
    Long addUsuarioBD() {
        Usuario usuario = new Usuario("user@ua");
        usuario.setNombre("Usuario Ejemplo");
        usuario.setPassword("123");
        usuario.setBlocked(false);
        usuario = usuarioService.registrar(usuario);
        return usuario.getId();
    }

    Long addUsuarioBlockedBD() {
        Usuario usuario = new Usuario("dos@ua");
        usuario.setNombre("Usuario Ejemplo");
        usuario.setPassword("123");
        usuario.setBlocked(true);
        usuario = usuarioService.registrar(usuario);
        return usuario.getId();
    }

    Long addBlockedUsuarioBD() {
        Usuario usuario = new Usuario("blocked@ua");
        usuario.setNombre("Usuario Ejemplo");
        usuario.setPassword("123");
        usuario.setBlocked(true);
        usuario = usuarioService.registrar(usuario);
        return usuario.getId();
    }

    @Test
    public void servicioLoginUsuario() {
        // GIVEN
        // Un usuario en la BD

        addUsuarioBD();
        addBlockedUsuarioBD();
        // WHEN
        // intentamos logear un usuario y contraseña correctos
        UsuarioService.LoginStatus loginStatus1 = usuarioService.login("user@ua", "123");

        // intentamos logear un usuario correcto, con una contraseña incorrecta
        UsuarioService.LoginStatus loginStatus2 = usuarioService.login("user@ua", "000");

        // intentamos logear un usuario que no existe,
        UsuarioService.LoginStatus loginStatus3 = usuarioService.login("pepito.perez@gmail.com", "12345678");

        UsuarioService.LoginStatus loginStatus4= usuarioService.login("blocked@ua", "123");
        // THEN

        // el valor devuelto por el primer login es LOGIN_OK,
        assertThat(loginStatus1).isEqualTo(UsuarioService.LoginStatus.LOGIN_OK);

        // el valor devuelto por el segundo login es ERROR_PASSWORD,
        assertThat(loginStatus2).isEqualTo(UsuarioService.LoginStatus.ERROR_PASSWORD);

        // y el valor devuelto por el tercer login es USER_NOT_FOUND.
        assertThat(loginStatus3).isEqualTo(UsuarioService.LoginStatus.USER_NOT_FOUND);

        assertThat(loginStatus4).isEqualTo(UsuarioService.LoginStatus.USER_BLOCKED);
    }

    @Test
    public void servicioRegistroUsuario() {
        // GIVEN
        // Creado un usuario nuevo, con una contraseña

        Usuario usuario = new Usuario("usuario.prueba2@gmail.com");
        usuario.setPassword("12345678");

        // WHEN
        // registramos el usuario,

        usuarioService.registrar(usuario);

        // THEN
        // el usuario se añade correctamente al sistema.

        Usuario usuarioBaseDatos = usuarioService.findByEmail("usuario.prueba2@gmail.com");
        assertThat(usuarioBaseDatos).isNotNull();
        assertThat(usuarioBaseDatos.getPassword()).isEqualTo(usuario.getPassword());
    }

    @Test
    public void servicioRegistroUsuarioExcepcionConNullPassword() {
        // GIVEN
        // Un usuario creado sin contraseña,

        Usuario usuario =  new Usuario("usuario.prueba@gmail.com");

        // WHEN, THEN
        // intentamos registrarlo, se produce una excepción de tipo UsuarioServiceException
        Assertions.assertThrows(UsuarioServiceException.class, () -> {
            usuarioService.registrar(usuario);
        });
    }


    @Test
    public void servicioRegistroUsuarioExcepcionConEmailRepetido() {
        // GIVEN
        // Un usuario en la BD

        addUsuarioBD();

        // WHEN
        // Creamos un usuario con un e-mail ya existente en la base de datos,
        Usuario usuario =  new Usuario("user@ua");
        usuario.setPassword("12345678");

        // THEN
        // si lo registramos, se produce una excepción de tipo UsuarioServiceException
        Assertions.assertThrows(UsuarioServiceException.class, () -> {
            usuarioService.registrar(usuario);
        });
    }

    @Test
    public void servicioRegistroUsuarioDevuelveUsuarioConId() {
        // GIVEN
        // Dado un usuario con contraseña nuevo y sin identificador,

        Usuario usuario = new Usuario("usuario.prueba@gmail.com");
        usuario.setPassword("12345678");

        // WHEN
        // lo registramos en el sistema,

        usuarioService.registrar(usuario);

        // THEN
        // se actualiza el identificador del usuario

        assertThat(usuario.getId()).isNotNull();

        // con el identificador que se ha guardado en la BD.

        Usuario usuarioBD = usuarioService.findById(usuario.getId());
        assertThat(usuarioBD).isEqualTo(usuario);
    }

    @Test
    public void servicioConsultaUsuarioDevuelveUsuario() {
        // GIVEN
        // Un usuario en la BD

        Long usuarioId = addUsuarioBD();

        // WHEN
        // recuperamos un usuario usando su e-mail,

        Usuario usuario = usuarioService.findByEmail("user@ua");

        // THEN
        // el usuario obtenido es el correcto.

        assertThat(usuario.getId()).isEqualTo(usuarioId);
        assertThat(usuario.getEmail()).isEqualTo("user@ua");
        assertThat(usuario.getNombre()).isEqualTo("Usuario Ejemplo");
    }

    private int size(Iterable<?> it){
        int i = 0;
        for (Object obj : it) i++;
        return i;
    }
    @Test
    public void serviciofindAllDevuelve0() {
        assertThat(size(usuarioService.findAll())).isEqualTo(0);
    }

    @Test
    public void serviciofindAllDevuelveLosRegistrados(){
        Long usuarioId = addUsuarioBD();

        Iterable<Usuario> usuarios = usuarioService.findAll();
        // hay un usuario
        assertThat(size(usuarios)).isEqualTo(1);

        for (Usuario u : usuarios){
            assertThat(u.getId()).isEqualTo(usuarioId);
            assertThat(u.getEmail()).isEqualTo("user@ua");
            assertThat(u.getNombre()).isEqualTo("Usuario Ejemplo");
        }

    }

    @Test
    public void existsAnyAdmin(){
        assertThat(usuarioService.existsAnyAdmin()).isEqualTo(false);
        Usuario usuario = new Usuario("user@ua");
        usuario.setNombre("Usuario Ejemplo");
        usuario.setPassword("123");
        usuario.setIsAdmin(true);
        usuario = usuarioService.registrar(usuario);

        assertThat(usuarioService.existsAnyAdmin()).isEqualTo(true);
    }

    @Test
    public void userGetsBlocked(){
        Long idUser = addUsuarioBD();
        Usuario usuario = usuarioService.findById(idUser);
        assertThat(usuario.getBlocked()).isEqualTo(false);
        usuarioService.blockUser(idUser);

        usuario = usuarioService.findById(idUser);
        assertThat(usuario.getBlocked()).isEqualTo(true);
    }

    @Test
    public void userGetsUnblocked(){
        Long idUser = addUsuarioBlockedBD();
        Usuario usuario = usuarioService.findById(idUser);
        assertThat(usuario.getBlocked()).isEqualTo(true);
        usuarioService.unblockUser(idUser);

        usuario = usuarioService.findById(idUser);
        assertThat(usuario.getBlocked()).isEqualTo(false);
    }

    @Test
    public void servicioEditarUsuario() {
        Usuario usuario = new Usuario("usuario.prueba2@gmail.com");
        usuario.setNombre("Alvaro");
        usuario.setFechaNacimiento(new Date(1999,02,27));
        usuario.setPassword("12345678");
        usuarioService.registrar(usuario);
        Usuario usuarioBaseDatos = usuarioService.findByEmail("usuario.prueba2@gmail.com");
        assertThat(usuarioBaseDatos).isNotNull();
        assertThat(usuarioBaseDatos.getPassword()).isEqualTo(usuario.getPassword());
        usuario.setPassword("123");
        usuarioService.editar(usuario);
        usuarioBaseDatos = usuarioService.findByEmail("usuario.prueba2@gmail.com");
        assertThat(usuarioBaseDatos.getPassword()).isEqualTo(usuario.getPassword());
    }

    @Test
    public void servicioEditarUsuario2() {
        Usuario usuario = new Usuario("usuario.prueba2@gmail.com");
        usuario.setNombre("Alvaro");
        usuario.setFechaNacimiento(new Date(1999,02,27));
        usuario.setPassword("12345678");
        usuarioService.registrar(usuario);
        Usuario usuarioBaseDatos = usuarioService.findByEmail("usuario.prueba2@gmail.com");
        assertThat(usuarioBaseDatos).isNotNull();
        assertThat(usuarioBaseDatos.getPassword()).isEqualTo(usuario.getPassword());
        usuario.setPassword("123");
        usuario.setNombre("Alberto");
        usuarioService.editar(usuario);
        usuarioBaseDatos = usuarioService.findByEmail("usuario.prueba2@gmail.com");
        assertThat(usuarioBaseDatos.getPassword()).isEqualTo(usuario.getPassword());
        assertThat(usuarioBaseDatos.getNombre()).isEqualTo(usuario.getNombre());
    }
}