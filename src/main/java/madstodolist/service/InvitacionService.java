package madstodolist.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import madstodolist.controller.InvitacionData;
import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Invitacion;
import madstodolist.model.InvitacionRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;

@Service
public class InvitacionService {

    @Autowired
    private InvitacionRepository invitacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private EquipoService equipoService;

    private void checkInvitacionRepetida(InvitacionData invitacionDTO) {
        List<Invitacion> invitacion = invitacionRepository.findByIdEquipoAndIdUsuario(invitacionDTO.getIdEquipo(),
                invitacionDTO.getIdUsuario());

        if (invitacion.size() != 0) {
            throw new InvitacionServiceException("Ya existe esta invitacion");   
        }
    }

    @Transactional
    public void invitar(InvitacionData invitacionDTO) {
        Usuario usuario = usuarioRepository.findById(invitacionDTO.getIdUsuario()).orElse(null);
        Equipo equipo = equipoRepository.findById(invitacionDTO.getIdEquipo()).orElse(null);

        if (usuario == null)
            throw new InvitacionServiceException("Usuario " + invitacionDTO.getIdUsuario() + " no existe");

        if (equipo == null)
            throw new EquipoServiceException("No existe el equipo con id " + invitacionDTO.getIdEquipo());

        checkInvitacionRepetida(invitacionDTO);

        Invitacion invitacion = new Invitacion(invitacionDTO.getIdEquipo(), invitacionDTO.getIdUsuario());
        invitacionRepository.save(invitacion);
    }

    @Transactional(readOnly = true)
    public List<Invitacion> obtenerInvitacionesDelUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario == null)
            throw new InvitacionServiceException("Usuario " + idUsuario + " no existe");

        List<Invitacion> invitaciones = new ArrayList<Invitacion>();
        invitaciones = invitacionRepository.findByIdUsuario(idUsuario);
        return invitaciones;
    }

    @Transactional
    public void aceptar(Invitacion invitacion) {
        equipoService.addUsuarioEquipo(invitacion.getUsuarioId(), invitacion.getEquipoId());
        invitacionRepository.delete(invitacion);
    }    

    @Transactional
    public void denegar(Invitacion invitacion) {
        invitacionRepository.delete(invitacion);
    }  
}
