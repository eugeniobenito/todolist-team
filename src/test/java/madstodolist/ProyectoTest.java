package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.Proyecto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class ProyectoTest {


    @Test
    public void entidadProyecto(){
        Equipo e = new Equipo("aaa");
        Proyecto p = new Proyecto("prueba", e);
        Assertions.assertThat(p.getNombre()).isEqualTo("prueba");
        Assertions.assertThat(p.getEquipo()).isEqualTo(e);
        Assertions.assertThat(e.getProyectos().size()).isEqualTo(1);
    }

}
