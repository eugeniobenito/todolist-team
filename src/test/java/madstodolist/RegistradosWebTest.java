package madstodolist;

import madstodolist.controller.exception.UsuarioNotFoundException;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistradosWebTest {
    @Autowired
    private TareaService tareaService;

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

    @Test
    public void listarDetallesNotFound() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(get("/registrados/10")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void listarDetalles() throws Exception {
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario.setNombre("usuario de la ua");
        usuario.setFechaNacimiento(new Date());
        usuario = usuarioService.registrar(usuario);
        Tarea tarea1 = tareaService.nuevaTareaUsuario(usuario.getId(), "Lavar coche");

        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);


        this.mockMvc.perform(get("/registrados/" + usuario.getId()))
                .andExpect(content().string(allOf(containsString(usuario.getNombre()),
                        containsString(usuario.getEmail()),
                        containsString(usuario.getId().toString()),
                        containsString(df.format(usuario.getFechaNacimiento())),
                        containsString(tarea1.getTitulo())
                )));
    }
}