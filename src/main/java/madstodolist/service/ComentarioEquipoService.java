package madstodolist.service;

import madstodolist.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComentarioEquipoService {
    Logger logger = LoggerFactory.getLogger(ComentarioEquipoService.class);

    @Autowired
    private ComentarioEquipoRepository comentarioEquipoRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Transactional
    public ComentarioEquipo crearComentario(String nombre, Long usuarioId, Long equipoId) {
        if(nombre == "") throw new ComentarioEquipoServiceException("El nombre no puede estar vacio");
        Usuario u = usuarioRepository.findById(usuarioId).orElse(null);
        if(u == null) throw new ComentarioEquipoServiceException("No existe el usuario");
        Equipo e = equipoRepository.findById(equipoId).orElse(null);
        if(e == null) throw new ComentarioEquipoServiceException("No existe el equipo");
        ComentarioEquipo comentario = new ComentarioEquipo(nombre, u, e);
        comentario = comentarioEquipoRepository.save(comentario);
        return comentario;
    }
}