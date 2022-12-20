package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.mail.javamail.JavaMailSender;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.Random;

@Controller
public class LoginController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSession managerUserSession;

    @Autowired
    private JavaMailSender sender;

    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginData", new LoginData());
        return "formLogin";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginData loginData, Model model, HttpSession session) {

        // Llamada al servicio para comprobar si el login es correcto
        UsuarioService.LoginStatus loginStatus = usuarioService.login(loginData.geteMail(), loginData.getPassword());

        if (loginStatus == UsuarioService.LoginStatus.LOGIN_OK) {
            Usuario usuario = usuarioService.findByEmail(loginData.geteMail());

            managerUserSession.logearUsuario(usuario.getId());

            if(usuario.getIsAdmin()){ return "redirect:/registrados"; }

            return "redirect:/usuarios/" + usuario.getId() + "/tareas";
        } else if (loginStatus == UsuarioService.LoginStatus.USER_NOT_FOUND) {
            model.addAttribute("error", "No existe usuario");
            return "formLogin";
        } else if (loginStatus == UsuarioService.LoginStatus.ERROR_PASSWORD) {
            model.addAttribute("error", "Contraseña incorrecta");
            return "formLogin";
        }
        else if (loginStatus == UsuarioService.LoginStatus.USER_BLOCKED) {
            model.addAttribute("error", "Usuario bloqueado en el sistema");
            return "formLogin";
        }

        return "formLogin";
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("registroData", new RegistroData());
        model.addAttribute("existsAnyAdmin", usuarioService.existsAnyAdmin());
        return "formRegistro";
    }

   @PostMapping("/registro")
   public String registroSubmit(@Valid RegistroData registroData, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "formRegistro";
        }

        if (usuarioService.findByEmail(registroData.geteMail()) != null) {
            model.addAttribute("registroData", registroData);
            model.addAttribute("error", "El usuario " + registroData.geteMail() + " ya existe");
            return "formRegistro";
        }

        Usuario usuario = new Usuario(registroData.geteMail());
        usuario.setPassword(registroData.getPassword());
        usuario.setFechaNacimiento(registroData.getFechaNacimiento());
        usuario.setNombre(registroData.getNombre());
        usuario.setIsAdmin(registroData.getIsAdmin());

        usuarioService.registrar(usuario);
        return "redirect:/login";
   }

    @GetMapping("/reset")
    public String resetForm(Model model) {
        model.addAttribute("loginData", new LoginData());
        return "reset";
    }

    @PostMapping("/reset")
    public String resetSubmit(@ModelAttribute LoginData loginData, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "reset";
        }

        if (usuarioService.findByEmail(loginData.geteMail()) == null) {
            model.addAttribute("loginData", loginData);
            model.addAttribute("error", "El usuario no existe");
            return "reset";
        }
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(loginData.geteMail());
        email.setSubject("Restablecer contraseña");
        StringBuffer body = new StringBuffer();
        body.append("****************************************************\n");
        body.append("Correo electrónico AUTOMATIZADO - No responder a este mensaje\n");
        body.append("****************************************************\n");
        body.append("Fecha: ");
        body.append(new Date());
        body.append("\n");
        body.append("________________________________________________________________________________");
        body.append("\n");
        body.append("Este es su enlace para restablecer la contraseña:");
        body.append("\n");
        body.append("http://localhost:8080/newPassword/user=" + usuarioService.findByEmail(loginData.geteMail()).getId() + "code=" + usuarioService.findByEmail(loginData.geteMail()).getCode());
        body.append("\n");
        body.append("________________________________________________________________________________");
        email.setText(body.toString());
        sender.send(email);
        model.addAttribute("enviado", "Se ha enviado un correo electrónico para restablecer la contraseña");
        return "reset";
    }

    @GetMapping("/newPassword/user={id}code={code}")
    public String newPasswordForm(@PathVariable(value="id") Long idUsuario, @PathVariable(value="code") int code,  Model model) {
        model.addAttribute("loginData", new LoginData());
        model.addAttribute("code", code);
        model.addAttribute("idUsuario", idUsuario);
        return "newPassword";
    }

    @PostMapping("/newPassword/user={id}code={code}")
    public String newPasswordSubmit(@PathVariable(value="id") Long idUsuario, @PathVariable(value="code") int code, @ModelAttribute LoginData loginData, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "redirect:/newPassword/user=" + idUsuario + "code=" + code;
        }
        Usuario usuario = usuarioService.findById(idUsuario);
        usuario.setPassword(loginData.getPassword());
        usuario.setCode(new Random().ints(10000, 99999).findFirst().getAsInt());
        usuarioService.editar(usuario);
        return "redirect:/login";
    }

   @GetMapping("/logout")
   public String logout(HttpSession session) {
        managerUserSession.logout();
        return "redirect:/login";
   }
}
