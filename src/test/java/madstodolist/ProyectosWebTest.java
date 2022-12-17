package madstodolist;


import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.ComentarioEquipo;
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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class ProyectosWebTest {
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
    public void botonYPaginaAnyadirProyecto() throws Exception{
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);

        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        this.mockMvc.perform(get("/equipos/" + e.getId().toString()))
                .andExpect(content().string(containsString("Añadir un nuevo proyecto")));


        this.mockMvc.perform(get("/equipos/" + e.getId().toString() + "/proyectos/nuevo"))
                .andExpect(content().string(containsString("Nuevo proyecto para el equipo " + e.getNombre())));

       MockHttpServletResponse response = this.mockMvc.perform(post("/equipos/" + e.getId().toString() + "/proyectos").param("nombre", "prueba894"))
                .andReturn().getResponse();

        this.mockMvc.perform(get("/equipos/" + e.getId().toString()))
                .andExpect(content().string(containsString("prueba894")));

    }

    @Test
    public void eliminarProyectoWeb() throws Exception{
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);

        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        Proyecto p = proyectoService.crearProyecto("prueba", e.getId());


        MockHttpServletResponse response = this.mockMvc.perform(delete("/equipos/" + e.getId().toString() + "/proyectos/" + p.getId()))
                .andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(200);

    }





}
