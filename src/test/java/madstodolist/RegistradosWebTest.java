package madstodolist;

import madstodolist.model.Usuario;
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
public class RegistradosWebTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void muestraTemplateListadoRegistradosVacio() throws Exception{
        this.mockMvc.perform(get("/registrados"))
                .andExpect(content().string
                        (containsString("Listado de usuarios registrados")));
    }

    @Test
    public void getRegistradosReturnUsers() throws Exception {
        Usuario u1 = new Usuario("prueba@gmail.com");
        u1.setPassword("123");
        Usuario u2 = new Usuario("prueba2@gmail.com");
        u2.setPassword("1234");


        usuarioService.registrar(u1);
        usuarioService.registrar(u2);
        this.mockMvc.perform(get("/registrados"))
                .andExpect(content().string(allOf(containsString("prueba2@gmail.com"),
                                                    containsString("prueba@gmail.com"),
                        containsString("<td>" + u1.getId() + "</td>"),
                        containsString("<td>" + u2.getId() + "</td>")
                        )));
    }
}