package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.*;
import madstodolist.model.ComentarioEquipo;
import madstodolist.model.Equipo;
import madstodolist.model.Proyecto;
import madstodolist.model.Usuario;
import madstodolist.service.ComentarioEquipoService;
import madstodolist.service.EquipoService;
import madstodolist.service.ProyectoService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProyectoController {

    @Autowired
    EquipoService equipoService;

    @Autowired
    ManagerUserSession managerUserSession;

    @Autowired
    ProyectoService proyectoService;

    @Autowired
    UsuarioService usuarioService;

    private void checkAdminOfTeam(Equipo e) {
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

    @Transactional
    private void userJoinedTeam(Equipo e) {
        if (!managerUserSession.isUsuarioLogeado())
            throw new UsuarioNoLogeadoException();
        Long idUser = managerUserSession.usuarioLogeado();
        Usuario u = usuarioService.findById(idUser);
        Long idAdmin = new Long(-1);
        if(e.getAdmin() != null)  idAdmin = e.getAdmin().getId();
        List<Usuario> usuarios = equipoService.usuariosEquipo(e.getId());
        if(!usuarios.contains(u) && idUser != idAdmin){
            throw new UsuarioNotJoinedTeamException();
        }
    }


    @GetMapping("/equipos/{id}/proyectos/nuevo")
    public String formNuevoProyecto(@PathVariable(value="id") Long idEquipo,
                                 @ModelAttribute ProyectoData proyectoData, Model model,
                                 HttpSession session) {
        Equipo equipo = equipoService.recuperarEquipo(idEquipo);
        if(equipo == null)
            throw new EquipoNotFoundException();
        checkAdminOfTeam(equipo);



        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo",equipo);
        return "formNuevoProyecto";
    }

    @DeleteMapping("/equipos/{id}/proyectos/{proyectoId}")
    @ResponseBody
    public String eliminarProyecto(@PathVariable(value="id") Long idEquipo,
                                    @PathVariable(value="proyectoId") Long idProyecto,
                                    @ModelAttribute ProyectoData proyectoData, Model model,
                                    HttpSession session) {
        Equipo equipo = equipoService.recuperarEquipo(idEquipo);
        if(equipo == null)
            throw new EquipoNotFoundException();
        checkAdminOfTeam(equipo);

        proyectoService.eliminarProyecto(idProyecto);
        return "";
    }

    @PostMapping("/equipos/{id}/proyectos")
    public String nuevoProyecto(@PathVariable(value="id") Long idEquipo,
                                  @ModelAttribute ProyectoData proyectoData,
                                  Model model, RedirectAttributes flash,
                                  HttpSession session) {

        Equipo equipo = equipoService.recuperarEquipo(idEquipo);
        if(equipo == null)
            throw new EquipoNotFoundException();
        checkAdminOfTeam(equipo);

        if(proyectoData.getNombre() == "") throw new FormErrorException();
        Proyecto proyecto = proyectoService.crearProyecto(proyectoData.getNombre(), equipo.getId());

        return "redirect:/equipos/" + idEquipo;
    }

    @GetMapping("/proyectos/{id}")
    public String proyectoData(@PathVariable(value="id") Long idProyecto,
                                    @ModelAttribute TareaProyectoData tareaProyectoData, Model model,
                                    HttpSession session) {
        Proyecto p = proyectoService.getById(idProyecto);
        if(p == null)
            throw new ProyectoNotFoundException();
        Equipo equipo = p.getEquipo();

        userJoinedTeam(equipo);

        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", usuario);
        model.addAttribute("proyecto", p);
        return "detallesProyecto";
    }

}
