package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;

    @Autowired
    EquipoService equipoService;

    @Autowired
    ManagerUserSession managerUserSession;

    @GetMapping("/about")
    public String about(Model model) {
        Long idUser = managerUserSession.usuarioLogeado();
        if (idUser != null) {
            Usuario user = usuarioService.findById(idUser);
            model.addAttribute("usuario", user);
        }
        Integer numberUser = 0;
        Integer numberTask = 0;
        for(Usuario u :usuarioService.findAll()) {
            numberUser++;
            for (Tarea t : tareaService.allTareasUsuario(u.getId())) {
                numberTask++;
            }
        }

        model.addAttribute("numberUser", numberUser);
        model.addAttribute("numberTask", numberTask);
        model.addAttribute("numberTeam", equipoService.findAllOrderedByName().size());
        return "about";
    }

}