package madstodolist.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason="El usuario no tiene permisos administrador")
public class UsuarioNoAdminException extends RuntimeException {
}
