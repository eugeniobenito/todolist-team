package madstodolist.service;

import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public enum LoginStatus {LOGIN_OK, USER_NOT_FOUND, ERROR_PASSWORD, USER_BLOCKED }

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender sender;

    @Transactional
    public Usuario blockUser(Long idUser){
        logger.debug("Bloqueando al usuario " + idUser + "...");
        Usuario usuario = usuarioRepository.findById(idUser).orElse(null);
        if(usuario == null){
            throw new UsuarioServiceException("No existe usuario con id " + idUser);
        }
        usuario.setBlocked(true);
        usuarioRepository.save(usuario);
        return usuario;
    }

    @Transactional
    public Usuario unblockUser(Long idUser){
        logger.debug("Desbloqueando al usuario " + idUser + "...");
        Usuario usuario = usuarioRepository.findById(idUser).orElse(null);
        if(usuario == null){
            throw new UsuarioServiceException("No existe usuario con id " + idUser);
        }
        usuario.setBlocked(false);
        usuarioRepository.save(usuario);
        return usuario;
    }

    @Transactional(readOnly = true)
    public LoginStatus login(String eMail, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(eMail);
        if (!usuario.isPresent()) {
            return LoginStatus.USER_NOT_FOUND;
        } else if (!usuario.get().getPassword().equals(password)) {
            return LoginStatus.ERROR_PASSWORD;
        } else if (usuario.get().getBlocked()){
            return LoginStatus.USER_BLOCKED; 
        }
        else {
            return LoginStatus.LOGIN_OK;
        }
    }

    // Se añade un usuario en la aplicación.
    // El email y password del usuario deben ser distinto de null
    // El email no debe estar registrado en la base de datos
    @Transactional
    public Usuario registrar(Usuario usuario) {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioBD.isPresent())
            throw new UsuarioServiceException("El usuario " + usuario.getEmail() + " ya está registrado");
        else if (usuario.getEmail() == null)
            throw new UsuarioServiceException("El usuario no tiene email");
        else if (usuario.getPassword() == null)
            throw new UsuarioServiceException("El usuario no tiene password");
        else {
            try {
                SimpleMailMessage email = new SimpleMailMessage();
                email.setTo(usuario.getEmail());
                email.setSubject("Registro en ToDoList");
                StringBuffer body = new StringBuffer();
                body.append("****************************************************\n");
                body.append("Correo electrónico AUTOMATIZADO - No responder a este mensaje\n");
                body.append("****************************************************\n");
                body.append("Fecha: ");
                body.append(new Date());
                body.append("\n");
                body.append("________________________________________________________________________________");
                body.append("\n");
                body.append("Te has registrado correctamente en ToDoList con el siguiente usuario: " + usuario.getEmail() + ".");
                body.append("\n");
                body.append("http://localhost:8080/login");
                body.append("\n");
                body.append("________________________________________________________________________________");
                email.setText(body.toString());
                sender.send(email);
            } catch (MailAuthenticationException e){
                System.out.println("El correo introducido no es valido -> " + e.getMessage());
            }
            return usuarioRepository.save(usuario);
        }
    }

    @Transactional(readOnly = true)
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<Usuario> findAll() { return usuarioRepository.findAll(); }

    @Transactional(readOnly = true)
    public boolean existsAnyAdmin() { return (usuarioRepository.findByIsAdmin(true) != null && usuarioRepository.findByIsAdmin(true).size() > 0); }
    @Transactional
    public Usuario editar(Usuario usuario) {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioBD.isPresent())
            if (usuario.getPassword() == null){
                throw new UsuarioServiceException("El usuario no tiene password");
            } else if (usuario.getNombre() == null) {
                throw new UsuarioServiceException("El usuario no tiene nombre");
            }
            else{
                return usuarioRepository.save(usuario);
            }
        else throw new UsuarioServiceException("El usuario no existe en la base de datos");
    }

}
