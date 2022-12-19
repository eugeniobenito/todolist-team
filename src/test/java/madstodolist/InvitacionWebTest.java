package madstodolist;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.InvitacionData;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.InvitacionService;
import madstodolist.service.UsuarioService;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class InvitacionWebTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InvitacionService invitacionService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EquipoService equipoService;

    @MockBean
    private ManagerUserSession managerUserSession;

    private Usuario crearUsuario(String email, Boolean isAdmin) {
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario.setIsAdmin(true);
        usuario.setBlocked(false);
        return usuarioService.registrar(usuario);
    }

    private Equipo crearEquipo(Usuario u) {
        Equipo e = equipoService.crearEquipo("equipo", "desc", u);
        return e;
    }

    @Test
    public void invitarUsuarioAEquipoError() throws Exception {
        // GIVEN
        // Un usuario y un equipo en la BD
        Usuario u = crearUsuario("a@a", false);
        Equipo e = crearEquipo(u);

        when(managerUserSession.usuarioLogeado()).thenReturn(2L);
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        // WHEN, THEN
        // realizamos la petición POST para crear un invitación,
        // se devuelve el código de estado HTTP recurso creado

        String urlPost = "/invitacion/" + e.getId();
        this.mockMvc.perform(post(urlPost)
                .contentType(MediaType.TEXT_PLAIN)
                .content("a@a"))
                .andExpect(status().is4xxClientError());        
    }    
}
