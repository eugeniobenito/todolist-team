package madstodolist;

import madstodolist.model.Proyecto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class ProyectoTest {

    @Test
    public void proyecto(){
        Proyecto p = new Proyecto("prueba");
        Assertions.assertThat(p.getNombre()).isEqualTo("prueba");
    }

}
