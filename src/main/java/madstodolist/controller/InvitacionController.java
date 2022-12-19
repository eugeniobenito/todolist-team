package madstodolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.UsuarioNoAdminException;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Equipo;
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
}
