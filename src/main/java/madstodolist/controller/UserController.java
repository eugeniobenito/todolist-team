package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSession managerUserSession;
    
    @GetMapping("/registrados")
    public String registrados(Model model){
        Iterable<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);

        Long idUser = managerUserSession.usuarioLogeado();
        if (idUser != null) {
            Usuario user = usuarioService.findById(idUser);
            model.addAttribute("usuario", user);
        }
        return "listaRegistrados";
    }
}
