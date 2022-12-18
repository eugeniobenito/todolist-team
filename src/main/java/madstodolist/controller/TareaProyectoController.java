package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.*;
import madstodolist.model.Equipo;
import madstodolist.model.Proyecto;
import madstodolist.model.TareaProyecto;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.ProyectoService;
import madstodolist.service.TareaProyectoService;
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
public class TareaProyectoController {

    @Autowired
    EquipoService equipoService;

    @Autowired
    ManagerUserSession managerUserSession;

    @Autowired
    ProyectoService proyectoService;

    @Autowired
    TareaProyectoService tareaProyectoService;

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


    @PostMapping("/proyectos/{id}/tareas")
    public String nuevaTarea(@PathVariable(value="id") Long idProyecto,
                                  @ModelAttribute TareaProyectoData tareaProyectoData,
                                  Model model, RedirectAttributes flash,
                                  HttpSession session) {

        Proyecto p = proyectoService.getById(idProyecto);
        if(p == null)
            throw new ProyectoNotFoundException();
        Equipo equipo = p.getEquipo();

        userJoinedTeam(equipo);

        if(tareaProyectoData.getNombre() == "") throw new FormErrorException();
        TareaProyecto tarea = tareaProyectoService.crearTareaProyectoService(tareaProyectoData.getNombre(), p.getId());

        return "redirect:/proyectos/" + idProyecto;
    }

    @DeleteMapping("/proyectos/{id}/tareas/{tareaId}")
    @Transactional
    @ResponseBody
    public String eliminarTarea(@PathVariable(value="id") Long idProyecto,
                                   @PathVariable(value="tareaId") Long idTarea,
                                   Model model,
                                   HttpSession session) {
        Proyecto p = proyectoService.getById(idProyecto);
        if(p == null)
            throw new ProyectoNotFoundException();
        Equipo equipo = p.getEquipo();
        TareaProyecto tareaProyecto = tareaProyectoService.findById(idTarea);
        if(tareaProyecto == null ) throw new TareaNotFoundException();

        userJoinedTeam(equipo);

        tareaProyectoService.eliminarTareaProyecto(tareaProyecto.getId());
        return "";
    }

}
