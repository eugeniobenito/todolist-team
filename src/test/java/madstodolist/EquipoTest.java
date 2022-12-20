package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class EquipoTest {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void crearEquipo() {
        Equipo equipo = new Equipo("Proyecto P1");
        assertThat(equipo.getNombre()).isEqualTo("Proyecto P1");
    }

    @Test
    public void crearEquipoDescripcionNull() {
        Equipo equipo = new Equipo("Proyecto P1");
        assertThat(equipo.getDescripcion()).isNull();
    }

    @Test
    public void crearEquipoPublicoPorDefecto() {
        Equipo equipo = new Equipo("Proyecto P1");
        assertThat(equipo.isPrivate()).isFalse();
    }

    @Test
    public void anyadeDescripcionEquipo() {
        // GIVEN
        // Un equipo nuevo
        Equipo equipo = new Equipo("Proyecto P1");

        // WHEN
        // Añadimos una descripcion de equipo 
        equipo.setDescripcion("Equipo encargado de la asignatura MADS");        

        // THEN 
        // Obtenemos la descripción del equipo
        assertThat(equipo.getDescripcion()).isEqualTo("Equipo encargado de la asignatura MADS");
    }

    @Test
    public void cambiaVisibilidadEquipo() {
        // GIVEN
        // Un equipo nuevo
        Equipo equipo = new Equipo("Proyecto P1");

        // WHEN
        // Cambiamos la visibilidad
        equipo.changeVisibility();

        // THEN 
        // Ahora el equipo es privado
        assertThat(equipo.isPrivate()).isTrue();
    }


    @Test
    @Transactional
    public void grabarYBuscarEquipo() {
        // GIVEN
        // Un equipo nuevo
        Equipo equipo = new Equipo("Proyecto P1");

        // WHEN
        // Salvamos el equipo en la base de datos
        equipoRepository.save(equipo);

        // THEN
        // Su identificador se ha actualizado y lo podemos
        // usar para recuperarlo de la base de datos
        Long equipoId = equipo.getId();
        assertThat(equipoId).isNotNull();
        Equipo equipoDB = equipoRepository.findById(equipoId).orElse(null);
        assertThat(equipoDB).isNotNull();
        assertThat(equipoDB.getNombre()).isEqualTo("Proyecto P1");
    }

    @Test
    @Transactional
    public void grabarYBuscarEquipoConDescripcion() {
        // GIVEN
        // Un equipo nuevo con descripcion
        Equipo equipo = new Equipo("Proyecto P1");
        equipo.setDescripcion("Equipo encargado de la asignatura MADS");

        // WHEN
        // Salvamos el equipo en la base de datos
        equipoRepository.save(equipo);

        // THEN
        // Su identificador se ha actualizado y lo podemos
        // usar para recuperarlo de la base de datos
        Long equipoId = equipo.getId();
        assertThat(equipoId).isNotNull();
        Equipo equipoDB = equipoRepository.findById(equipoId).orElse(null);
        assertThat(equipoDB).isNotNull();
        assertThat(equipoDB.getNombre()).isEqualTo("Proyecto P1");
        assertThat(equipoDB.getDescripcion()).isEqualTo("Equipo encargado de la asignatura MADS");
    }


    @Test
    public void comprobarIgualdadEquipos() {
        // GIVEN
        // Creamos tres equipos sin id, sólo con el nombre
        Equipo equipo1 = new Equipo("Proyecto P1");
        Equipo equipo2 = new Equipo("Proyecto P2");
        Equipo equipo3 = new Equipo("Proyecto P2");

        // THEN
        // Comprobamos igualdad basada en el atributo nombre y que el
        // hashCode es el mismo para dos equipos con igual nombre
        assertThat(equipo1).isNotEqualTo(equipo2);
        assertThat(equipo2).isEqualTo(equipo3);
        assertThat(equipo2.hashCode()).isEqualTo(equipo3.hashCode());

        // WHEN
        // Añadimos identificadores y comprobamos igualdad por identificadores
        equipo1.setId(1L);
        equipo2.setId(1L);
        equipo3.setId(2L);

        // THEN
        // Comprobamos igualdad basada en el atributo nombre
        assertThat(equipo1).isEqualTo(equipo2);
        assertThat(equipo2).isNotEqualTo(equipo3);
    }


    @Test
    @Transactional
    public void comprobarRelacionBaseDatos() {
        // GIVEN
        // Un equipo y un usuario en la BD
        Equipo equipo = new Equipo("Proyecto 1");
        equipoRepository.save(equipo);

        Usuario usuario = new Usuario("user@ua");
        usuarioRepository.save(usuario);

        // WHEN
        // Añadimos el usuario al equipo

        equipo.addUsuario(usuario);

        // THEN
        // La relación entre usuario y equipo pqueda actualizada en BD

        Equipo equipoBD = equipoRepository.findById(equipo.getId()).orElse(null);
        Usuario usuarioBD = usuarioRepository.findById(usuario.getId()).orElse(null);

        assertThat(equipo.getUsuarios()).hasSize(1);
        assertThat(equipo.getUsuarios()).contains(usuario);
        assertThat(usuario.getEquipos()).hasSize(1);
        assertThat(usuario.getEquipos()).hasSize(1);
        assertThat(usuario.getEquipos()).contains(equipo);
    }

    @Test
    @Transactional
    public void comprobarFindAll() {
        // GIVEN
        // Dos equipos en la base de datos
        equipoRepository.save(new Equipo("Proyecto 2"));
        equipoRepository.save(new Equipo("Proyecto 3"));

        // WHEN
        List<Equipo> equipos = equipoRepository.findAll();

        // THEN
        assertThat(equipos).hasSize(2);
    }

    @Test
    public void removeUsuario() {
        Equipo equipo = new Equipo("prueba");
        Usuario usuario = new Usuario("user@ua");
        equipo.addUsuario(usuario);
        assertThat(equipo.getUsuarios().size()).isEqualTo(1);
        equipo.removeUsuario(usuario);
        assertThat(equipo.getUsuarios().size()).isEqualTo(0);
    }

    @Test
    public void setNombre() {
        Equipo equipo = new Equipo("hola");
        equipo.setNombre("adios");
        assertThat(equipo.getNombre()).isEqualTo("adios");
    }

    @Test
    public void getAdmin() {
        Equipo equipo = new Equipo("prueba");
        Usuario usuario = new Usuario("user@ua");
        equipo.setAdmin(usuario);
        assertThat(usuario).isEqualTo(equipo.getAdmin());
    }
}