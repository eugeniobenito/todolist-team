package madstodolist;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Equipo;
import madstodolist.model.Proyecto;
import madstodolist.model.Usuario;
import madstodolist.service.ComentarioEquipoService;
import madstodolist.service.EquipoService;
import madstodolist.service.ProyectoService;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class TareaProyectoWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private ComentarioEquipoService comentarioEquipoService;

    @Autowired
    private ProyectoService proyectoService;

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
    public void paginaProyectoFormAnyadirTarea() throws Exception{
        Usuario u = crearUsuario("a@a", false);
        Usuario u2 = crearUsuario("a2@a", false);
        Equipo e = crearEquipo(u);
        Proyecto p = proyectoService.crearProyecto("proyecto24", e.getId());

        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        this.mockMvc.perform(post("/proyectos/" + p.getId().toString() + "/tareas").param("nombre", "estudiar"));
        p = proyectoService.getById(p.getId());
        Assertions.assertThat(p.getTareasProyecto().size()).isEqualTo(1);


        MockHttpServletResponse response = this.mockMvc.perform(post("/proyectos/200/tareas"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(404);

        when(managerUserSession.usuarioLogeado()).thenReturn(u2.getId());
        response = this.mockMvc.perform(post("/proyectos/" + p.getId().toString() + "/tareas"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }





}
