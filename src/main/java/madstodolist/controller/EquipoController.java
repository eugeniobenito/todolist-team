package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.*;
import madstodolist.model.Equipo;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.lang.ref.ReferenceQueue;
import java.util.List;

@Controller
public class EquipoController {

    @Autowired
    EquipoService equipoService;

    @Autowired
    ManagerUserSession managerUserSession;

    @Autowired
    UsuarioService usuarioService;

    private void comprobarUsuarioLogeado(Long idUsuario, Equipo e) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!managerUserSession.isUsuarioLogeado())
            throw new UsuarioNoLogeadoException();
        Long idUser = managerUserSession.usuarioLogeado();
        Usuario u = usuarioService.findById(idUser);

        Long idAdmin = new Long(-1);
        if(e.getAdmin() != null)  idAdmin = e.getAdmin().getId();

        if(!u.getIsAdmin() && idUser != idAdmin) {
            if (!idUsuario.equals(idUsuarioLogeado))
                throw new UsuarioNoLogeadoException();
        }
    }

    private void comprobarUsuarioLogeado(Long idUsuario) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!managerUserSession.isUsuarioLogeado())
            throw new UsuarioNoLogeadoException();
        Long idUser = managerUserSession.usuarioLogeado();
        Usuario u = usuarioService.findById(idUser);
        if(!u.getIsAdmin()) {
            if (!idUsuario.equals(idUsuarioLogeado))
                throw new UsuarioNoLogeadoException();
        }
    }

    private void isAnyUserLogged() {
        if (!managerUserSession.isUsuarioLogeado())
            throw new UsuarioNoLogeadoException();
    }

    private void checkAdminUserLogged(Equipo e) {
        if (!managerUserSession.isUsuarioLogeado())
            throw new UsuarioNoLogeadoException();
        Long idUser = managerUserSession.usuarioLogeado();
        Usuario u = usuarioService.findById(idUser);
        Long idAdmin = new Long(-1);
        if(e.getAdmin() != null)  idAdmin = e.getAdmin().getId();
        if(!u.getIsAdmin() && idUser != idAdmin){
            throw new UsuarioNoAdminException();
        }
    }

    @GetMapping("/equipos")
    public String listadoEquipos(Model model, HttpSession session) {
        // middleware usuario logeado

        isAnyUserLogged();

        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();

        Usuario usuario = usuarioService.findById(idUsuarioLogeado);
        List<Equipo> equipos = equipoService.findAllOrderedByName();
        model.addAttribute("usuario", usuario);
        model.addAttribute("equipos", equipos);
        return "listaEquipos";
    }

    @GetMapping("/equipos/{id}")
    public String detallesEquipo(@PathVariable(value="id") Long idEquipo, Model model){
        isAnyUserLogged();
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();

        Usuario usuario = usuarioService.findById(idUsuarioLogeado);
        Equipo equipo = equipoService.recuperarEquipo(idEquipo);
        if(equipo == null)
            throw new EquipoNotFoundException();
        List<Usuario> usuarios = equipoService.usuariosEquipo(idEquipo);
        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        model.addAttribute("usuarios", usuarios);
        return "detallesEquipo";
    }

    @PostMapping("/equipos/{id}/usuarios/{userId}")
    public String anyadirUsuarioEquipo(@PathVariable(value="id") Long idEquipo, @PathVariable(value="userId") Long userId,
                                 Model model){
        comprobarUsuarioLogeado(userId); // revisamos que el recurso es suyo y esta autenticado
        Equipo equipo = equipoService.recuperarEquipo(idEquipo);
        if(equipo == null)
            throw new EquipoNotFoundException();
        equipoService.addUsuarioEquipo(userId, idEquipo);
        return "redirect:/equipos/" + idEquipo.toString();
    }

    @DeleteMapping("/equipos/{id}/usuarios/{userId}")
    @ResponseBody
    public String eliminarUsuarioEquipo(@PathVariable(value="id") Long idEquipo, @PathVariable(value="userId") Long userId,
                                       Model model){

        Equipo equipo = equipoService.recuperarEquipo(idEquipo);
        if(equipo == null)
            throw new EquipoNotFoundException();
        comprobarUsuarioLogeado(userId, equipo); // revisamos que el recurso es suyo y esta autenticado
        equipoService.removeUsuarioEquipo(userId, idEquipo);
        return "";
    }

    @GetMapping("/equipos/nuevo")
    public String formNuevoEquipo(@ModelAttribute EquipoData equipoData, Model model,
                                 HttpSession session) {

        isAnyUserLogged();
        Long idUsuario = managerUserSession.usuarioLogeado();
        Usuario usuario = usuarioService.findById(idUsuario);
        model.addAttribute("usuario", usuario);
        return "formNuevoEquipo";
    }
    @PostMapping("/equipos")
    public String nuevoEquipo(@ModelAttribute EquipoData equipoData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {


        isAnyUserLogged();
        Long idUser = managerUserSession.usuarioLogeado();
        Usuario u = usuarioService.findById(idUser);
        if(equipoData.getNombre() == "") throw new FormErrorException();
        Equipo e = equipoService.crearEquipo(equipoData.getNombre(), equipoData.getDescripcion(), u);
        return "redirect:/equipos";
    }

    @PostMapping("/equipos/{id}/editar")
    public String editarEquipo(@PathVariable(value="id") Long idEquipo, @ModelAttribute EquipoData equipoData,
                              Model model, RedirectAttributes flash,
                              HttpSession session) {
        Equipo equipo = equipoService.recuperarEquipo(idEquipo);
        if(equipo == null)
            throw new EquipoNotFoundException();

        checkAdminUserLogged(equipo);

        if(equipoData.getNombre() == "") throw new FormErrorException();

        Equipo e = equipoService.modificarEquipo(idEquipo, equipoData.getNombre(), equipoData.getDescripcion());
        return "redirect:/equipos";
    }

    @DeleteMapping("/equipos/{id}")
    @ResponseBody
    public String eliminarEquipo(@PathVariable(value="id") Long idEquipo,
                                        Model model){
        Equipo equipo = equipoService.recuperarEquipo(idEquipo);
        if(equipo == null)
            throw new EquipoNotFoundException();
        checkAdminUserLogged(equipo);

        equipoService.eliminarEquipo(idEquipo);

        return "";
    }

    @GetMapping("/equipos/{id}/editar")
    public String formEditarEquipo(@PathVariable(value="id") Long idEquipo, @ModelAttribute EquipoData equipoData,
                                 Model model, HttpSession session) {

        Equipo equipo = equipoService.recuperarEquipo(idEquipo);
        if(equipo == null)
            throw new EquipoNotFoundException();
        checkAdminUserLogged(equipo);

        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        Usuario usuario = usuarioService.findById(idUsuarioLogeado);

        equipoData.setNombre(equipo.getNombre());

        model.addAttribute("equipo", equipo);
        model.addAttribute("usuario", usuario);
        return "formEditarEquipo";
    }

}
