package madstodolist;

import madstodolist.model.*;
import madstodolist.service.ComentarioEquipoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class ComentariosEquipoServiceTest {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private ComentarioEquipoRepository comentarioEquipoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioEquipoService comentarioEquipoService;

    private Usuario crearUsuario(String email, Boolean isAdmin){
        Usuario u = new Usuario(email);
        u.setPassword("123");
        u.setIsAdmin(isAdmin);
        usuarioRepository.save(u);
        return u;
    }

    private Equipo crearEquipo(Usuario u){
        Equipo e = new Equipo("equipo");
        e.setAdmin(u);
        equipoRepository.save(e);
        return e;
    }


    @Test
    public void comprobarEquipoService(){
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);
        ComentarioEquipo c = comentarioEquipoService.crearComentario("prueba", u.getId(), e.getId());
        ComentarioEquipo c2 = comentarioEquipoRepository.findById(c.getId()).orElse(null);
        assertThat(c2).isNotNull();
        assertThat(c.getId()).isEqualTo(c2.getId());
        assertThat(c.getUsuario()).isEqualTo(c2.getUsuario());
        assertThat(c.getEquipo()).isEqualTo(c2.getEquipo());
        assertThat(c.getFecha()).isEqualTo(c2.getFecha());
        assertThat(c).isEqualTo(c2); 
    }


}