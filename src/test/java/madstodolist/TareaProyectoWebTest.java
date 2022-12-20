package madstodolist;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.*;
import madstodolist.service.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private TareaProyectoService tareaProyectoService;

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

    @Test
    @Transactional
    public void eliminarTareaController() throws Exception{
        Usuario u = crearUsuario("a@a", false);
        Usuario u2 = crearUsuario("a2@a", false);
        Equipo e = crearEquipo(u);
        Proyecto p = proyectoService.crearProyecto("proyecto24", e.getId());

        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        TareaProyecto tp = tareaProyectoService.crearTareaProyectoService("nuevaTarea", p.getId());
        Assertions.assertThat(p.getTareasProyecto().size()).isEqualTo(1);
        MockHttpServletResponse response = this.mockMvc.perform(delete("/proyectos/" + p.getId().toString() + "/tareas/" + tp.getId().toString())).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(200);

        p = proyectoService.getById(p.getId());
        Assertions.assertThat(tareaProyectoService.findById(tp.getId())).isNull();


         response = this.mockMvc.perform(delete("/proyectos/" + p.getId().toString() + "/tareas/2000"))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(404);

        when(managerUserSession.usuarioLogeado()).thenReturn(u2.getId());
        response = this.mockMvc.perform(delete("/proyectos/" + p.getId().toString() + "/tareas/" + tp.getId().toString()))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }


    @Test
    @Transactional
    public void updateTarea() throws Exception{
        Usuario u = crearUsuario("a@a", false);
        Usuario u2 = crearUsuario("a2@a", false);
        Equipo e = crearEquipo(u);
        Proyecto p = proyectoService.crearProyecto("proyecto24", e.getId());

        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        TareaProyecto tp = tareaProyectoService.crearTareaProyectoService("nuevaTarea", p.getId());
        String urlPatch = "/tareasproyecto/" + tp.getId().toString();
        this.mockMvc.perform(patch(urlPatch)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("DONE"))
                .andExpect(status().isOk());

        this.mockMvc.perform(patch(urlPatch)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("donut"))
                .andExpect(status().is4xxClientError());

        Assertions.assertThat(tareaProyectoService.findById(tp.getId()).getStatus()).isEqualTo(Status.DONE);

    }

    @Test
    @Transactional
    public void joinAndLeaveUsuarioTarea() throws Exception{

        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);
        Proyecto p = proyectoService.crearProyecto("proyecto24", e.getId());
        TareaProyecto tareaProyecto = tareaProyectoService.crearTareaProyectoService("prueba", p.getId());

        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);
        this.mockMvc.perform(post("/tareaproyecto/" + tareaProyecto.getId().toString() +
                                            "/usuarios/" + u.getId().toString()));
        Assertions.assertThat(tareaProyecto.getUsuarios()).contains(u);

        this.mockMvc.perform(delete("/tareaproyecto/" + tareaProyecto.getId().toString() +
                "/usuarios/" + u.getId().toString()));

        Assertions.assertThat(tareaProyecto.getUsuarios()).doesNotContain(u);
    }




}
