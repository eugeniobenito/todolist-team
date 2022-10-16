package madstodolist;

import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistroWebTest {
    @Autowired
    private TareaService tareaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void checkAdminCheckboxWhen0Admins() throws Exception {
        this.mockMvc.perform(get("/registro"))
                .andExpect(content().string(containsString("El usuario será administrador")));
    }


    @Test
    public void checkAdminCheckboxWhenAnyAdmins() throws Exception {

        Usuario u1 = new Usuario("prueba@gmail.com");
        u1.setPassword("123");
        u1.setIsAdmin(true);



        usuarioService.registrar(u1);
        this.mockMvc.perform(get("/registro"))
                .andExpect(content().string(not(containsString("El usuario será administrador"))));
    }


}