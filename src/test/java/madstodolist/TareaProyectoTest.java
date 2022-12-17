package madstodolist;

import madstodolist.model.*;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class TareaProyectoTest {

    @Autowired
    EquipoRepository equipoRepository;

    @Autowired
    ProyectoRepository proyectoRepository;

    @Autowired
    TareaProyectoRepository tareaProyectoRepository;

    @Test
    public void crearTareaProyectoConRelaciones() {
        Equipo e = new Equipo("aaa");
        Proyecto p = new Proyecto("prueba", e);
        TareaProyecto tp = new TareaProyecto("estudiar", p);

        Assertions.assertThat(tp.getProyecto()).isEqualTo(p);
        Assertions.assertThat(p.getTareasProyecto().size()).isEqualTo(1);
        Assertions.assertThat(tp.getStatus()).isEqualTo(Status.TODO);
        tp.setStatus(Status.DONE);
        Assertions.assertThat(tp.getStatus()).isEqualTo(Status.DONE);
    }

    @Test
    public void tareaProyectoCanSave() {
        Equipo e = new Equipo("aaa");
        equipoRepository.save(e);
        Proyecto p = new Proyecto("prueba", e);
        proyectoRepository.save(p);
        TareaProyecto tp = new TareaProyecto("estudiar", p);
        tp = tareaProyectoRepository.save(tp);
        Assertions.assertThat(tp.getId()).isNotNull(); 
    }



}
