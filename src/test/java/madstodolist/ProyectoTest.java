package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.Proyecto;
import madstodolist.model.ProyectoRepository;
import madstodolist.model.EquipoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class ProyectoTest {
    @Autowired
    ProyectoRepository proyectoRepository;

    @Autowired
    EquipoRepository equipoRepository;


    @Test
    public void entidadProyecto(){
        Equipo e = new Equipo("aaa");
        Proyecto p = new Proyecto("prueba", e);
        Assertions.assertThat(p.getNombre()).isEqualTo("prueba");
        Assertions.assertThat(p.getEquipo()).isEqualTo(e);
        Assertions.assertThat(e.getProyectos().size()).isEqualTo(1);
    }

    @Test
    public void proyectoCanSave(){
        Equipo e = new Equipo("aaa");
        equipoRepository.save(e); 
        Proyecto p = new Proyecto("prueba", e);
        proyectoRepository.save(p);
        Assertions.assertThat(p.getId()).isNotNull();
    }

}
