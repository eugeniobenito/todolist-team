package madstodolist.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    private JavaMailSender sender;

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
        try{
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(usuario.getEmail());
            email.setSubject("Invitación a un equipo");
            StringBuffer body = new StringBuffer();
            body.append("****************************************************\n");
            body.append("Correo electrónico AUTOMATIZADO - No responder a este mensaje\n");
            body.append("****************************************************\n");
            body.append("Fecha: ");
            body.append(new Date());
            body.append("\n");
            body.append("________________________________________________________________________________");
            body.append("\n");
            body.append("Solicitud de unión del equipo " + equipo.getNombre() + ", para aceptar la invitación haga clic en el siguiente enlace: ");
            body.append("\n");
            body.append("http://localhost:8080/login");
            body.append("\n");
            body.append("________________________________________________________________________________");
            email.setText(body.toString());
            sender.send(email);
        } catch (MailAuthenticationException e){
            System.out.println("El correo introducido no es valido -> " + e.getMessage());
        }
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

    @Transactional(readOnly = true)
    public HashMap<Invitacion, String> obtenerInvitacionesConNombreDelEquipo(Long idUsuario) {
        List<Invitacion> invitaciones = this.obtenerInvitacionesDelUsuario(idUsuario);
        HashMap<Invitacion, String> invitacionesConNombreDelEquipo = new HashMap<>();

        for (Invitacion invitacion : invitaciones) {
            String nombreEquipo = equipoService.recuperarEquipo(invitacion.getEquipoId()).getNombre();
            invitacionesConNombreDelEquipo.put(invitacion, nombreEquipo);
        }

        return invitacionesConNombreDelEquipo;
    }

    @Transactional(readOnly = true)
    public Invitacion findById(Long idInvitacion) {
        Invitacion invitacion = invitacionRepository.findById(idInvitacion).orElse(null);

        if (invitacion == null)
            throw new InvitacionServiceException("Invitacion no existe");

        return invitacion;
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
