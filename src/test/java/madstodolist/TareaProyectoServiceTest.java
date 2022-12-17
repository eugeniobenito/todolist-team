package madstodolist;

import madstodolist.model.*;
import madstodolist.service.TareaProyectoService;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

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




}
