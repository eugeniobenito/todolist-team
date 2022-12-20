package madstodolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.UsuarioNoAdminException;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Equipo;
import madstodolist.model.Invitacion;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.InvitacionService;
import madstodolist.service.UsuarioService;

@Controller
public class InvitacionController {
    
    @Autowired
    InvitacionService invitacionService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EquipoService equipoService;

    @Autowired
    ManagerUserSession managerUserSession;

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

    private void comprobarUsuarioLogeado(Long idUsuario) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }

    @PostMapping("/invitacion/{id}")
    @ResponseBody
    public String invitar(@PathVariable(value="id") Long idEquipo, @RequestBody String correoUsuario) {
        Usuario usuario = usuarioService.findByEmail(correoUsuario);
        Equipo equipo = equipoService.recuperarEquipo(idEquipo);

        if(usuario == null){
            throw new UsuarioNotFoundException();
        }

        InvitacionData invitacionDTO = new InvitacionData(idEquipo, usuario.getId());

        checkAdminUserLogged(equipo);

        try {
            invitacionService.invitar(invitacionDTO);
        } catch (Exception e) {
            return "";
        }        

        return "";
    }

    @DeleteMapping("/invitacion/{id}")
    @ResponseBody
    public String denegar(@PathVariable(value="id") Long idInvitacion) {
        Invitacion invitacion = invitacionService.findById(idInvitacion);
        comprobarUsuarioLogeado(invitacion.getUsuarioId());
        invitacionService.denegar(invitacion);
        return "";
    }

    @PatchMapping("/invitacion/{id}")
    @ResponseBody
    public String aceptar(@PathVariable(value="id") Long idInvitacion) {
        Invitacion invitacion = invitacionService.findById(idInvitacion);
        comprobarUsuarioLogeado(invitacion.getUsuarioId());
        invitacionService.aceptar(invitacion);
        return "";
    }
}
