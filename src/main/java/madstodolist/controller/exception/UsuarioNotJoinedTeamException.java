package madstodolist.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Usuario no pertenece al equipo")
public class UsuarioNotJoinedTeamException extends RuntimeException {
}
