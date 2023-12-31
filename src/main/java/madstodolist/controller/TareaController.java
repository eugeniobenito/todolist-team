package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.controller.exception.StatusNotValidException;
import madstodolist.controller.exception.TareaNotFoundException;
import madstodolist.model.Invitacion;
import madstodolist.model.Status;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.InvitacionService;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

@Controller
public class TareaController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;

    @Autowired
    InvitacionService invitacionService;

    @Autowired
    ManagerUserSession managerUserSession;

    private void comprobarUsuarioLogeado(Long idUsuario) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }

    public boolean isEnumValue(String status) {
        for (Enum value : Status.class.getEnumConstants()) {
            if (value.name().equals(status)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/usuarios/{id}/tareas/nueva")
    public String formNuevaTarea(@PathVariable(value="id") Long idUsuario,
                                 @ModelAttribute TareaData tareaData, Model model,
                                 HttpSession session) {

        comprobarUsuarioLogeado(idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        model.addAttribute("usuario", usuario);
        return "formNuevaTarea";
    }

    @PostMapping("/usuarios/{id}/tareas/nueva")
    public String nuevaTarea(@PathVariable(value="id") Long idUsuario, @ModelAttribute TareaData tareaData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) throws ParseException {

        comprobarUsuarioLogeado(idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        tareaService.nuevaTareaUsuario(idUsuario, tareaData);
        flash.addFlashAttribute("mensaje", "Tarea creada correctamente");
        return "redirect:/usuarios/" + idUsuario + "/tareas";
     }

    @GetMapping("/usuarios/{id}/tareas")
    public String listadoTareas(@PathVariable(value="id") Long idUsuario, Model model, HttpSession session) {

        comprobarUsuarioLogeado(idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        List<Tarea> tareas = tareaService.allTareasUsuario(idUsuario);
        HashMap<Invitacion, String> invitaciones = invitacionService.obtenerInvitacionesConNombreDelEquipo(idUsuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("tareas", tareas);
        model.addAttribute("invitaciones", invitaciones);
        return "listaTareas";
    }

    @GetMapping("/tareas/{id}/editar")
    public String formEditaTarea(@PathVariable(value="id") Long idTarea, @ModelAttribute TareaData tareaData,
                                 Model model, HttpSession session) {

        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        comprobarUsuarioLogeado(tarea.getUsuario().getId());

        HashMap<Invitacion, String> invitaciones = invitacionService.obtenerInvitacionesConNombreDelEquipo(tarea.getUsuario().getId()); 
        model.addAttribute("invitaciones", invitaciones);
        model.addAttribute("tarea", tarea);
        model.addAttribute("usuario", tarea.getUsuario());
        tareaData.setTitulo(tarea.getTitulo());
        tareaData.setFechaLimite(tarea.getFechaLimite());
        return "formEditarTarea";
    }

    @PostMapping("/tareas/{id}/editar")
    public String grabaTareaModificada(@PathVariable(value="id") Long idTarea, @ModelAttribute TareaData tareaData,
                                       Model model, RedirectAttributes flash, HttpSession session) {
        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        Long idUsuario = tarea.getUsuario().getId();

        comprobarUsuarioLogeado(idUsuario);

        tareaService.modificaTarea(idTarea, tareaData);
        flash.addFlashAttribute("mensaje", "Tarea modificada correctamente");
        return "redirect:/usuarios/" + tarea.getUsuario().getId() + "/tareas";
    }

    @DeleteMapping("/tareas/{id}")
    @ResponseBody
    // La anotación @ResponseBody sirve para que la cadena devuelta sea la resupuesta
    // de la petición HTTP, en lugar de una plantilla thymeleaf
    public String borrarTarea(@PathVariable(value="id") Long idTarea, RedirectAttributes flash, HttpSession session) {
        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        comprobarUsuarioLogeado(tarea.getUsuario().getId());

        tareaService.borraTarea(idTarea);
        return "";
    }

    @PatchMapping("/tareas/{id}")
    @ResponseBody
    public String cambiarEstadoTarea(@PathVariable(value="id") Long idTarea, @RequestBody String status) {
        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        if (!isEnumValue(status)) {
            throw new StatusNotValidException();            
        }

        comprobarUsuarioLogeado(tarea.getUsuario().getId());

        tareaService.changeStatus(idTarea, Status.valueOf(status));
        return "";
    }

}
