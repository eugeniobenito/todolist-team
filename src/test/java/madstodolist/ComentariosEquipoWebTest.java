package madstodolist;


import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.ComentarioEquipo;
import madstodolist.model.ComentarioEquipoRepository;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.ComentarioEquipoService;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class ComentariosEquipoWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private ComentarioEquipoService comentarioEquipoService;

    @MockBean
    private ManagerUserSession managerUserSession;


    private Usuario crearUsuario(String email, Boolean isAdmin){
        Usuario u = new Usuario(email);
        u.setPassword("123");
        u.setIsAdmin(isAdmin);
        u = usuarioService.registrar(u);
        return u;
    }

    private Equipo crearEquipo(Usuario u){
        Equipo e = equipoService.crearEquipo("equipo", "desc", u);
        return e;
    }

    @Test
    @Transactional
    public void eliminarComentarioController() throws Exception{
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);
        ComentarioEquipo c = comentarioEquipoService.crearComentario("prueba", u.getId(), e.getId());

        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        Assertions.assertThat(e.getComentariosEquipo().size()).isEqualTo(1);
        MockHttpServletResponse response = this.mockMvc.perform(delete("/equipos/" + e.getId().toString() + "/comentarios/" + c.getId().toString())).andReturn().getResponse();
        Assertions.assertThat(e.getComentariosEquipo().size()).isEqualTo(0);

        Assertions.assertThat(comentarioEquipoService.recuperarComentario(c.getId())).isNull();
    }

    @Test
    public void eliminarComentarioControllerExceptions() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(delete("/equipos/200/comentarios/10")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);

        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);
        ComentarioEquipo c = comentarioEquipoService.crearComentario("prueba", u.getId(), e.getId());

        response = this.mockMvc.perform(delete("/equipos/" + e.getId().toString() + "/comentarios/" + c.getId().toString())).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(401);


        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        response = this.mockMvc.perform(delete("/equipos/" + e.getId().toString() + "/comentarios/200")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }


    @Test
    @Transactional
    public void crearComentarioController() throws Exception{
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);

        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        Assertions.assertThat(e.getComentariosEquipo().size()).isEqualTo(0);
        MockHttpServletResponse response = this.mockMvc.perform(post("/equipos/" + e.getId().toString() + "/comentarios").param("comentario", "prueba")).andReturn().getResponse();
        Assertions.assertThat(e.getComentariosEquipo().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void crearComentarioControllerExceptions() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(post("/equipos/200/comentarios")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);

        Usuario u = crearUsuario("a@a", false);
        Usuario u2 = crearUsuario("a@aa", false);
        Equipo e = crearEquipo(u);
        ComentarioEquipo c = comentarioEquipoService.crearComentario("prueba", u.getId(), e.getId());

        // nadie logeado
        response = this.mockMvc.perform(post("/equipos/" + e.getId().toString() + "/comentarios")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(401);


        when(managerUserSession.usuarioLogeado()).thenReturn(u2.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        // logeado uno que no está unido al grupo
        response = this.mockMvc.perform(post("/equipos/" + e.getId().toString() + "/comentarios")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);

        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());

        response = this.mockMvc.perform(delete("/equipos/" + e.getId().toString() + "/comentarios/200")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void crearComentarioForumlarioShow() throws Exception {

        Usuario u = crearUsuario("a@a", false);

        Usuario u2 = crearUsuario("a2@a", false);
        Equipo e = crearEquipo(u);


        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);


        this.mockMvc.perform(get("/equipos/" + e.getId().toString()))
                .andExpect(content().string(containsString("Enviar comentario")));

        when(managerUserSession.usuarioLogeado()).thenReturn(u2.getId());

        this.mockMvc.perform(get("/equipos/" + e.getId().toString()))
                .andExpect(content().string(not(containsString("Enviar comentario"))));
    }

    @Test
    public void eliminarComentarioShow() throws Exception {

        Usuario u = crearUsuario("a@a", false);

        Usuario u2 = crearUsuario("a2@a", false);
        Equipo e = crearEquipo(u);
        equipoService.addUsuarioEquipo(u2.getId(), e.getId());
        comentarioEquipoService.crearComentario("aa", u.getId(), e.getId());


        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);


        this.mockMvc.perform(get("/equipos/" + e.getId().toString()))
                .andExpect(content().string(containsString("Eliminar comentario")));

        when(managerUserSession.usuarioLogeado()).thenReturn(u2.getId());

        this.mockMvc.perform(get("/equipos/" + e.getId().toString()))
                .andExpect(content().string(not(containsString("Eliminar comentario"))));
    }

    @Test
    public void showAllComentarios() throws Exception {

        Usuario u = crearUsuario("a@a", false);

        Usuario u2 = crearUsuario("a2@a", false);
        Equipo e = crearEquipo(u);
        equipoService.addUsuarioEquipo(u2.getId(), e.getId());


        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);


        this.mockMvc.perform(get("/equipos/" + e.getId().toString()))
                .andExpect(content().string(containsString("No existen comentarios en el equipo actualmente")));

        comentarioEquipoService.crearComentario("El mejor comentario de la historia", u.getId(), e.getId());

        this.mockMvc.perform(get("/equipos/" + e.getId().toString()))
                .andExpect(content().string((containsString("El mejor comentario de la historia"))));
    }

}
