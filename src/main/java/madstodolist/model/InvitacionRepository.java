package madstodolist.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface InvitacionRepository extends CrudRepository<Invitacion, Long>{
    List<Invitacion> findByIdUsuario(Long idUsuario);
    List<Invitacion> findByIdEquipoAndIdUsuario(Long idEquipo, Long idUsuario);
}
