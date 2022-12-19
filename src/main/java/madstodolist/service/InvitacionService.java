package madstodolist.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import madstodolist.controller.InvitacionData;
import madstodolist.model.Invitacion;
import madstodolist.model.InvitacionRepository;

@Service
public class InvitacionService {
    
    @Autowired
    private InvitacionRepository invitacionRepository;

    @Transactional
    public void invitar(InvitacionData invitacionDTO) {
        Invitacion invitacion = new Invitacion(invitacionDTO.getIdEquipo(),invitacionDTO.getIdUsuario());
        invitacionRepository.save(invitacion);
    }

    @Transactional(readOnly = true)
    public List<Invitacion> obtenerInvitacionesDelUsuario(Long idUsuario) {
        List<Invitacion> invitaciones = new ArrayList<Invitacion>();
        invitaciones = invitacionRepository.findByIdUsuario(idUsuario);
        return invitaciones;
    }
}
