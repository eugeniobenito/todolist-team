package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Proyecto;
import madstodolist.model.ProyectoRepository;
import madstodolist.service.ProyectoService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class ProyectoServiceTest {
    @Autowired
    ProyectoRepository proyectoRepository;

    @Autowired
    EquipoRepository equipoRepository;

    @Autowired
    ProyectoService proyectoService;

    @Test
    public void entidadProyecto(){
        Equipo e = new Equipo("aaa");
        e = equipoRepository.save(e);
        Proyecto p = proyectoService.crearProyecto("prueba", e.getId());
        Assertions.assertThat(p.getEquipo()).isEqualTo(e);
        e = equipoRepository.findById(e.getId()).orElse(null);
        Assertions.assertThat(e.getProyectos().size()).isEqualTo(1);
    }

    @Test
    public void obtenerProyectoPorId(){
        Equipo e = new Equipo("aaa");
        e = equipoRepository.save(e);
        Proyecto p = proyectoService.crearProyecto("prueba", e.getId());
        Proyecto p2 = proyectoService.getById(p.getId());
        Assertions.assertThat(p.getId()).isEqualTo(p2.getId());
    }

    @Test
    public void eliminarProyecto(){
        Equipo e = new Equipo("aaa");
        e = equipoRepository.save(e);
        Proyecto p = proyectoService.crearProyecto("prueba", e.getId());
        proyectoService.eliminarProyecto(p.getId());
        Assertions.assertThat(proyectoService.getById(p.getId())).isNull();
    }
}
