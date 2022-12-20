package madstodolist;

import madstodolist.model.*;
import madstodolist.service.TareaProyectoService;
import madstodolist.service.TareaProyectoServiceException;
import madstodolist.service.UsuarioService;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class TareaProyectoServiceTest {

    @Autowired
    EquipoRepository equipoRepository;

    @Autowired
    ProyectoRepository proyectoRepository;

    @Autowired
    TareaProyectoService tareaProyectoService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    public void crearTareaProyecto() {
        Equipo e = new Equipo("aaa");
        e = equipoRepository.save(e);
        Proyecto p = new Proyecto("prueba", e);
        p = proyectoRepository.save(p);

        TareaProyecto tarea = tareaProyectoService.crearTareaProyectoService("estudiar", p.getId());
        Assertions.assertThat(tarea.getId()).isNotNull();
    }

    @Test
    public void findByIdTest(){
        Equipo e = new Equipo("aaa");
        e = equipoRepository.save(e);
        Proyecto p = new Proyecto("prueba", e);
        p = proyectoRepository.save(p);

        TareaProyecto tarea = tareaProyectoService.crearTareaProyectoService("estudiar", p.getId());
        Assertions.assertThat(tarea.getId()).isNotNull();
        Assertions.assertThat(tareaProyectoService.findById(tarea.getId())).isEqualTo(tarea);
    }

    @Test
    public void eliminarTest(){
        Equipo e = new Equipo("aaa");
        e = equipoRepository.save(e);
        Proyecto p = new Proyecto("prueba", e);
        p = proyectoRepository.save(p);

        TareaProyecto tarea = tareaProyectoService.crearTareaProyectoService("estudiar", p.getId());
        Assertions.assertThat(tareaProyectoService.findById(tarea.getId())).isEqualTo(tarea);
        tareaProyectoService.eliminarTareaProyecto(tarea.getId());
        Assertions.assertThat(tareaProyectoService.findById(tarea.getId())).isNull();
        Assertions.assertThat(p.getTareasProyecto().size()).isEqualTo(0);
    }

    @Test
    public void modificarStatus(){
        Equipo e = new Equipo("aaa");
        e = equipoRepository.save(e);
        Proyecto p = new Proyecto("prueba", e);
        p = proyectoRepository.save(p);

        TareaProyecto tarea = tareaProyectoService.crearTareaProyectoService("estudiar", p.getId());
        Assertions.assertThat(tarea.getStatus()).isEqualTo(Status.TODO);
        tarea = tareaProyectoService.cambiarEstado(tarea.getId(), Status.DONE);
        Assertions.assertThat(tarea.getStatus()).isEqualTo(Status.DONE);
    }

    @Test
    @Transactional
    public void anyadirUsuarioService(){
        Equipo e = new Equipo("aaa");
        e = equipoRepository.save(e);
        Proyecto p = new Proyecto("prueba", e);
        p = proyectoRepository.save(p);
        Usuario u = new Usuario("qa@a");
        u.setPassword("aaa");
        u = usuarioRepository.save(u);

        TareaProyecto tarea = tareaProyectoService.crearTareaProyectoService("estudiar", p.getId());
        Assertions.assertThat(tarea.getUsuarios()).hasSize(0);
        tareaProyectoService.addUsuario(tarea.getId(), u.getId());
        Assertions.assertThat(tarea.getUsuarios()).contains(u);
    }

    @Test
    @Transactional
    public void eliminarUsuario() {
        Equipo e = new Equipo("aaa");
        e = equipoRepository.save(e);
        Proyecto p = new Proyecto("prueba", e);
        p = proyectoRepository.save(p);
        Usuario u = new Usuario("qa@a");
        u.setPassword("aaa");
        u = usuarioRepository.save(u);

        TareaProyecto tarea = tareaProyectoService.crearTareaProyectoService("estudiar", p.getId());
        Assertions.assertThat(tarea.getUsuarios()).hasSize(0);
        tareaProyectoService.addUsuario(tarea.getId(), u.getId());
        Assertions.assertThat(tarea.getUsuarios()).contains(u);
        tareaProyectoService.removeUsuario(tarea.getId(), u.getId());
        Assertions.assertThat(tarea.getUsuarios()).doesNotContain(u);
    }

    @Test
    public void checkExceptionsAddyRemove() {
        Equipo e = new Equipo("aaa");
        e = equipoRepository.save(e);
        Proyecto p = new Proyecto("prueba", e);
        p = proyectoRepository.save(p);
        Usuario u = new Usuario("qa@a");
        u.setPassword("aaa");
        u = usuarioRepository.save(u);

        TareaProyecto tarea = tareaProyectoService.crearTareaProyectoService("estudiar", p.getId());
        assertThrows(TareaProyectoServiceException.class, () -> tareaProyectoService.addUsuario(new Long(20), new Long(200)));
        assertThrows(TareaProyectoServiceException.class, () -> tareaProyectoService.removeUsuario(new Long(20), new Long(200)));
        Long uid = u.getId();

        assertThrows(TareaProyectoServiceException.class, () -> tareaProyectoService.addUsuario(new Long(20), uid));
        assertThrows(TareaProyectoServiceException.class, () -> tareaProyectoService.removeUsuario(new Long(20), uid));


    }


}
