package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.*;
import madstodolist.model.ComentarioEquipo;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.ComentarioEquipoService;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ComentarioEquipoController {

    @Autowired
    EquipoService equipoService;

    @Autowired
    ManagerUserSession managerUserSession;

    @Autowired
    ComentarioEquipoService comentarioEquipoService;

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

    private void userJoinedTeam(Equipo e) {
        if (!managerUserSession.isUsuarioLogeado())
            throw new UsuarioNoLogeadoException();
        Long idUser = managerUserSession.usuarioLogeado();
        Usuario u = usuarioService.findById(idUser);
        Long idAdmin = new Long(-1);
        if(e.getAdmin() != null)  idAdmin = e.getAdmin().getId();
        if(!e.getUsuarios().contains(u) && idUser != idAdmin){
            throw new UsuarioNotJoinedTeamException();
        }
    }

    @DeleteMapping("/equipos/{id}/comentarios/{commentId}")
    @ResponseBody
    public String eliminarUsuarioEquipo(@PathVariable(value="id") Long idEquipo, @PathVariable(value="commentId") Long commentId,
                                       Model model){

        Equipo equipo = equipoService.recuperarEquipo(idEquipo);
        if(equipo == null)
            throw new EquipoNotFoundException();

        checkAdminOfTeam(equipo);
        ComentarioEquipo comentario = comentarioEquipoService.recuperarComentario(commentId);
        if(comentario == null) {
            throw new ComentarioNotFoundException();
        }
        comentarioEquipoService.eliminarComentario(commentId);
        return "";
    }

    @PostMapping("/equipos/{id}/comentarios")
    public String nuevoComentario(@PathVariable(value="id") Long idEquipo,
                                  @ModelAttribute ComentarioEquipoData comentarioData,
                              Model model, RedirectAttributes flash,
                              HttpSession session) {

        Equipo equipo = equipoService.recuperarEquipo(idEquipo);
        if(equipo == null)
            throw new EquipoNotFoundException();
        userJoinedTeam(equipo);

        if(comentarioData.getComentario() == "") throw new FormErrorException();
        Long idUser = managerUserSession.usuarioLogeado();
        ComentarioEquipo comentario = comentarioEquipoService.crearComentario(comentarioData.getComentario(), idUser, idEquipo);
        return "redirect:/equipos/" + idEquipo;
    }
}
