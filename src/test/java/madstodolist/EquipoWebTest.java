package madstodolist;


import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Equipo;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.TareaService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class EquipoWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EquipoService equipoService;

    @MockBean
    private ManagerUserSession managerUserSession;

    private Usuario createUser(){
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario.setIsAdmin(false);
        usuario.setBlocked(false);
        return usuarioService.registrar(usuario);
    }

    private Usuario createAdmin(){
        Usuario usuario = new Usuario("admin@ua");
        usuario.setPassword("123");
        usuario.setIsAdmin(true);
        usuario.setBlocked(false);
        return usuarioService.registrar(usuario);
    }
    @Test
    public void getSomeEquipos() throws Exception{

        Equipo e1 = equipoService.crearEquipo("PruebaEquipo1");
        Equipo e2 = equipoService.crearEquipo("PruebaEquipo2");


        Usuario usuario = createUser();
        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        this.mockMvc.perform(get("/equipos"))
                .andExpect(content().string
                        (allOf(containsString("PruebaEquipo1"),
                                containsString("PruebaEquipo2"))));
    }

    @Test
    public void usuarioNoLogeadoNoVeEquipos() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(get("/equipos")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    public void usuarioNoLogeadoNoVeInfoEquipos() throws Exception {
        Equipo e1 = equipoService.crearEquipo("PruebaEquipo1");

        MockHttpServletResponse response = this.mockMvc.perform(get("/equipos/" + e1.getId().toString())).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    public void equipoNoExiste() throws Exception {

        Usuario usuario = createUser();
        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        MockHttpServletResponse response = this.mockMvc.perform(get("/equipos/100")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void getInfoEquipoSinMiembros() throws Exception{

        Equipo e1 = equipoService.crearEquipo("PruebaEquipo1");

        Usuario usuario = createUser();
        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        this.mockMvc.perform(get("/equipos/" + e1.getId().toString()))
                .andExpect(content().string
                        (allOf(containsString(e1.getNombre()),
                                containsString(e1.getId().toString()),
                                containsString("El equipo no tiene usuarios que le pertenecen"))));
    }

    @Test
    @Transactional
    public void getInfoEquipoConMiembros() throws Exception{

        Equipo e1 = equipoService.crearEquipo("PruebaEquipo1");

        Usuario usuario = createUser();
        e1.addUsuario(usuario);
        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        this.mockMvc.perform(get("/equipos/" + e1.getId().toString()))
                .andExpect(content().string
                        (allOf(containsString(e1.getNombre()),
                                containsString(e1.getId().toString()),
                                not(containsString("El equipo no tiene usuarios que le pertenecen")))));
    }

    @Test
    public void showJoinOrLeaveButton() throws Exception{
        Equipo e1 = equipoService.crearEquipo("PruebaEquipo1");


        Usuario usuario = createUser();
        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        this.mockMvc.perform(get("/equipos"))
                .andExpect(content().string
                        (allOf(containsString("Unirse"))));

        equipoService.addUsuarioEquipo(usuario.getId(), e1.getId());


        this.mockMvc.perform(get("/equipos"))
                .andExpect(content().string
                        (allOf(containsString("Abandonar"))));
    }

    @Test
    @Transactional
    public void showJoinOrLeaveButtonOnEquipoDetalles() throws Exception{
        Equipo e1 = equipoService.crearEquipo("PruebaEquipo1");


        Usuario usuario = createUser();
        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        this.mockMvc.perform(get("/equipos/" + e1.getId().toString()))
                .andExpect(content().string
                        (allOf(containsString("Unirse"))));

        equipoService.addUsuarioEquipo(usuario.getId(), e1.getId());


        this.mockMvc.perform(get("/equipos/" + e1.getId().toString()))
                .andExpect(content().string
                        (allOf(containsString("Abandonar"))));
    }

    @Test
    @Transactional
    public void controladoresUnirseYAbandonarEquipo() throws Exception {
        Equipo e1 = equipoService.crearEquipo("PruebaEquipo1");


        Usuario usuario = createUser();
        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        Assertions.assertThat(usuario.getEquipos()).hasSize(0);

        this.mockMvc.perform(post("/equipos/" + e1.getId().toString() + "/usuarios/" + usuario.getId().toString()));
        this.mockMvc.perform(get("/equipos"))
                .andExpect(content().string
                        (allOf(containsString("Abandonar"))));


        this.mockMvc.perform(post("/equipos/" + e1.getId().toString() + "/usuarios/" + usuario.getId().toString()));

        Assertions.assertThat(usuario.getEquipos()).hasSize(1);

        this.mockMvc.perform(delete("/equipos/" + e1.getId().toString() + "/usuarios/" + usuario.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipos/" + e1.getId().toString()));



        this.mockMvc.perform(get("/equipos"))
                .andExpect(content().string
                        (allOf(containsString("Abandonar"))));


    }

    @Test
    @Transactional
    public void controllerCrearEquipo() throws Exception {

        Usuario usuario = createUser();
        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);


        this.mockMvc.perform(post("/equipos").param("nombre", "Equipo nuevo 1"));
        this.mockMvc.perform(get("/equipos"))
                .andExpect(content().string
                        (allOf(containsString("Equipo nuevo 1"))));

    }

    @Test
    public void controllerCrearEquipoVista() throws Exception {

        Usuario usuario = createUser();
        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        this.mockMvc.perform(get("/equipos/nuevo"))
                .andExpect(content().string
                        (allOf(containsString("Crear equipo"))));

    }

    @Test
    public void botonNuevoEquipo() throws Exception {
        Usuario usuario = createUser();
        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        this.mockMvc.perform(get("/equipos"))
                .andExpect(content().string
                        (allOf(containsString("Nuevo equipo"))));

    }

    @Test
    public void errorAlCrearEquipoVacio() throws Exception {
        Usuario u = createUser();
        when(managerUserSession.usuarioLogeado()).thenReturn(u.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);
        MockHttpServletResponse response = this.mockMvc.perform(post("/equipos").param("nombre", "")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void unauthorizedGetNuevoEquipo() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(get("/equipos/nuevo")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    public void unauthorizedGetNuevoEquipoC2() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(post("/equipos").param("nombre", "aaa")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(401);
    }


    @Test
    public void equipoNotFoundUnirseAbandonarEquipo() throws Exception {
        Usuario usuario = createUser();
        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);



        MockHttpServletResponse response = this.mockMvc.perform(post("/equipos/200/usuarios/" + usuario.getId().toString() )).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);

         response = this.mockMvc.perform(delete("/equipos/200/usuarios/" + usuario.getId().toString() )).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void showDeleteAndEditButtonIfIsAdmin() throws Exception{
        Equipo e1 = equipoService.crearEquipo("PruebaEquipo1");


        Usuario usuario = createAdmin();
        Usuario usuario2 = createUser();

        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        this.mockMvc.perform(get("/equipos"))
                .andExpect(content().string(
                        allOf(containsString("Editar"), containsString("Eliminar"))));

        when(managerUserSession.usuarioLogeado()).thenReturn(usuario2.getId());
        this.mockMvc.perform(get("/equipos"))
                .andExpect(content().string(
                        allOf(not(containsString("Editar")), not(containsString("Eliminar")))));

    }

    @Test
    public void deleteControllerMethod() throws Exception{
        Equipo e1 = equipoService.crearEquipo("PruebaEquipo1");


        Usuario usuario = createAdmin();

        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        this.mockMvc.perform(delete("/equipos/" + e1.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipos"));

        Assertions.assertThat(equipoService.recuperarEquipo(e1.getId())).isNull();

    }

    @Test
    public void checkAuthDeleteEquipoController() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(delete("/equipos/200")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(401);

        Usuario usuario = createUser();
        Usuario admin = createAdmin();
        when(managerUserSession.usuarioLogeado()).thenReturn(admin.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

         response = this.mockMvc.perform(delete("/equipos/200/")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);

        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        Equipo e = equipoService.crearEquipo("prueba");

        response = this.mockMvc.perform(delete("/equipos/" + e.getId().toString())).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(401);


    }

    @Test
    public void editarEquipoPOSTController() throws Exception {
        Equipo equipo = equipoService.crearEquipo("equipo");
        Usuario usuario = createAdmin();

        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        this.mockMvc.perform(post("/equipos/" + equipo.getId().toString() + "/editar").param("nombre", "UATeam"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipos"));

        equipo = equipoService.recuperarEquipo(equipo.getId());
        Assertions.assertThat(equipo.getNombre()).isEqualTo("UATeam");
    }

    @Test
    public void editarEquipoPOSTControllerAuth() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(post("/equipos/200/editar")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(401);

        Usuario usuario = createUser();
        Usuario admin = createAdmin();
        when(managerUserSession.usuarioLogeado()).thenReturn(admin.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        response = this.mockMvc.perform(post("/equipos/200/editar")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
        Equipo e = equipoService.crearEquipo("prueba");
        response = this.mockMvc.perform(post("/equipos/" + e.getId().toString() +"/editar").param("nombre", "")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());


        response = this.mockMvc.perform(post("/equipos/" + e.getId().toString() + "/editar")).andReturn().getResponse();
        Assertions.assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    public void editarEquipoGET() throws Exception {
        Equipo equipo = equipoService.crearEquipo("equipo");
        Usuario usuario = createAdmin();

        when(managerUserSession.usuarioLogeado()).thenReturn(usuario.getId());
        when(managerUserSession.isUsuarioLogeado()).thenReturn(true);

        this.mockMvc.perform(get("/equipos/" + equipo.getId().toString() + "/editar").param("nombre", "UATeam"))
                .andExpect(content().string(allOf(containsString("Modificación del equipo " + equipo.getNombre()))));

    }

}
