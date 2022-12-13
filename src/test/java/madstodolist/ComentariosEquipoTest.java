package madstodolist;

import madstodolist.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class ComentariosEquipoTest {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private ComentarioEquipoRepository comentarioEquipoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

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
    public void crearComentarioEquipo() {
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);
        ComentarioEquipo c = new ComentarioEquipo("prueba", u, e);
        assertThat(c.getComentario()).isEqualTo("prueba");
        assertThat(c.getEquipo()).isEqualTo(e);
        assertThat(c.getUsuario()).isEqualTo(u);
        assertThat(c.getFecha()).isNotNull();
    }

    @Test
    public void comentarioEquipoSeActualizaEnEquipos() {
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);
        Set<ComentarioEquipo> comentarios = e.getComentariosEquipo();
        ComentarioEquipo c = new ComentarioEquipo("prueba", u, e);
        assertThat(e.getComentariosEquipo()).contains(c);
        assertThat(comentarios).contains(c);
    }

    @Test
    public void comentariosEquipoRepository() {
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);
        Set<ComentarioEquipo> comentarios = e.getComentariosEquipo();
        ComentarioEquipo c = new ComentarioEquipo("prueba", u, e);
        assertThat(c.getId()).isNull();
        c = comentarioEquipoRepository.save(c);
        assertThat(c.getId()).isNotNull();

    }

}