package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.UsuarioNoAdminException;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSession managerUserSession;

    private void checkUserAdmin(Model model){
        Long idUser = managerUserSession.usuarioLogeado();
        Usuario user = usuarioService.findById(idUser);
        if(user == null){
            throw new UsuarioNoLogeadoException();
        }
        else {
            if(!user.getIsAdmin()){
                throw new UsuarioNoAdminException();
            }
            model.addAttribute("usuario", user);
        }
    }

    @GetMapping("/registrados")
    public String registrados(Model model){
        Iterable<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);

        checkUserAdmin(model);


        return "listaRegistrados";
    }

    @GetMapping("/registrados/{id}")
    public String detallesUsuario(@PathVariable(value="id") Long idUsuario, Model model){
        Usuario usuario = usuarioService.findById(idUsuario);

        if(usuario == null){
            throw new UsuarioNotFoundException();
        }

        checkUserAdmin(model);
        
        model.addAttribute("usuarioDetalles", usuario);

        return "detallesUsuario";
    }

    @PostMapping("/usuarios/{id}/block")
    public String bloquearUsuario(@PathVariable(value = "id") Long idUsuario, Model model){
        Usuario usuario = usuarioService.findById(idUsuario);

        if(usuario == null){
            throw new UsuarioNotFoundException();
        }
        checkUserAdmin(model);

        usuarioService.blockUser(idUsuario);

        return "redirect:/registrados";

    }

    @PostMapping("/usuarios/{id}/unblock")
    public String desbloquearUsuario(@PathVariable(value = "id") Long idUsuario, Model model){
        Usuario usuario = usuarioService.findById(idUsuario);

        if(usuario == null){
            throw new UsuarioNotFoundException();
        }
        checkUserAdmin(model);

        usuarioService.unblockUser(idUsuario);

        return "redirect:/registrados";

    }

    @GetMapping("/account")
    public String Account(Model model){
        Long idUser = managerUserSession.usuarioLogeado();
        if (idUser != null) {
            Usuario user = usuarioService.findById(idUser);
            model.addAttribute("usuario", user);
        }
        else{
            throw new UsuarioNotFoundException();
        }

        return "account";
    }

    @PostMapping("/account")
    public String edit(Usuario user, Model model) {
        Usuario usuario = usuarioService.findByEmail(user.getEmail());
        usuario.setPassword(user.getPassword());
        usuario.setNombre(user.getNombre());
        usuarioService.editar(usuario);

        return "redirect:/account";
    }
}
