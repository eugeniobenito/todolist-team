package madstodolist;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.boot.test.mock.mockito.MockBean;

import javax.servlet.http.HttpSession;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class NavbarTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TareaService tareaService;

    @MockBean
    private ManagerUserSession managerUserSession;

    private long addUsuarioTareasBD() {
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);

        return usuario.getId();
    }

    private long addAdminToBd() {
        Usuario usuario = new Usuario("admin@ua");
        usuario.setPassword("123");
        usuario.setIsAdmin(true);
        usuario = usuarioService.registrar(usuario);

        return usuario.getId();
    }


    @Test
    public void loginNotContainsNavbar() throws Exception{
        String url = "/login";
        this.mockMvc.perform(get(url))
                .andExpect((content().string(not(containsString("<div id=\"navbar-menu\"")))));
    }

    @Test
    public void registroNotContainsNavbar() throws Exception{
        String url = "/registro";
        this.mockMvc.perform(get(url))
                .andExpect((content().string(not(containsString("<div id=\"navbar-menu\"")))));
    }

    @Test
    public void aboutContainsDefaultNavbar() throws Exception{

        String url = "/about";
        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(containsString("<div id=\"navbar-menu\""),
                                                    containsString("<div id=\"default-navbar\"")))));
    }

    @Test
    public void aboutContainsLoggedNavbar() throws Exception{
        Long usuarioId = addUsuarioTareasBD();
        String url = "/about";

        when(managerUserSession.usuarioLogeado()).thenReturn(usuarioId);

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(containsString("<div id=\"navbar-menu\""),
                        containsString("<div id=\"logged-navbar\"")))));

    }

    @Test
    public void allTareasContainsLoggedNavbar() throws Exception{
        Long usuarioId = addUsuarioTareasBD();
        String url = "/usuarios/" + usuarioId + "/tareas";

        when(managerUserSession.usuarioLogeado()).thenReturn(usuarioId);

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(containsString("<div id=\"navbar-menu\""),
                        containsString("<div id=\"logged-navbar\"")))));

    }

    @Test
    public void editarTareasContainsLoggedNavbar() throws Exception{
        Long usuarioId = addUsuarioTareasBD();

        Tarea tarea = tareaService.nuevaTareaUsuario(usuarioId, "Lavar coche");
        System.out.println(tarea.getId());


        String url = "/tareas/" + tarea.getId() + "/editar";

        when(managerUserSession.usuarioLogeado()).thenReturn(usuarioId);

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(containsString("<div id=\"navbar-menu\""),
                        containsString("<div id=\"logged-navbar\"")))));

    }

    @Test
    public void nuevaTareasContainsLoggedNavbar() throws Exception{
        Long usuarioId = addUsuarioTareasBD();
        String url = "/usuarios/" + usuarioId + "/tareas/nueva";

        when(managerUserSession.usuarioLogeado()).thenReturn(usuarioId);

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(containsString("<div id=\"navbar-menu\""),
                        containsString("<div id=\"logged-navbar\"")))));

    }

    @Test
    public void registradosContainsNavbar() throws Exception{
        Long usuarioId = addAdminToBd();
        String url = "/registrados";

        when(managerUserSession.usuarioLogeado()).thenReturn(usuarioId);

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(containsString("<div id=\"navbar-menu\""),
                        containsString("<div id=\"logged-navbar\"")))));

    }
}
