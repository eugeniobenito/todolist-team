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




}
