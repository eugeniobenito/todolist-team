package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class AcercaDeWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;

    @Autowired
    EquipoService equipoService;
    @Test
    public void getAboutDevuelveNombreAplicacion() throws Exception {
        this.mockMvc.perform(get("/about"))
                .andExpect(content().string(containsString("ToDoList")));
    }

    @Test
    public void testShowEntityAmount() throws Exception {

        this.mockMvc.perform(get("/about"))
                .andExpect(content().string(allOf(containsString("<li>Usuarios registrados: <span>0</span></li>"),
                        containsString("<li>Tareas registradas: <span>0</span></li>"),
                        containsString("<li>Equipos registrados: <span>0</span></li>"))));

        Usuario invitado = new Usuario("als106@alu.ua.es");
        invitado.setPassword("123");
        invitado = usuarioService.registrar(invitado);

        Tarea task1 = tareaService.nuevaTareaUsuario(invitado.getId(),"Task 1");

        Equipo equipoA  = equipoService.crearEquipo("Equipo A");
        Equipo equipoB  = equipoService.crearEquipo("Equipo B");

        this.mockMvc.perform(get("/about"))
                .andExpect(content().string(allOf(containsString("<li>Usuarios registrados: <span>1</span></li>"),
                        containsString("<li>Tareas registradas: <span>1</span></li>"),
                        containsString("<li>Equipos registrados: <span>2</span></li>"))));
    }
}