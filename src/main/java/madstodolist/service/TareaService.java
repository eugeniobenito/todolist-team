package madstodolist.service;

import madstodolist.controller.TareaData;
import madstodolist.model.Status;
import madstodolist.model.Tarea;
import madstodolist.model.TareaRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class TareaService {

    Logger logger = LoggerFactory.getLogger(TareaService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TareaRepository tareaRepository;

    @Transactional
    public Tarea nuevaTareaUsuario(Long idUsuario, String tituloTarea) {
        logger.debug("Añadiendo tarea " + tituloTarea + " al usuario " + idUsuario);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new TareaServiceException("Usuario " + idUsuario + " no existe al crear tarea " + tituloTarea);
        }
        Tarea tarea = new Tarea(usuario, tituloTarea);
        tareaRepository.save(tarea);
        return tarea;
    }

    @Transactional
    public Tarea nuevaTareaUsuario(Long idUsuario, TareaData tareaDTO) {
        logger.debug("Añadiendo tarea " + tareaDTO.getTitulo() + " al usuario " + idUsuario);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new TareaServiceException("Usuario " + idUsuario + " no existe al crear tarea " + tareaDTO.getTitulo());
        }

        if (tareaDTO.getFechaLimite() != null && tareaDTO.getFechaLimite().before(new Date())) {
            throw new TareaServiceException("No puedes crear una tarea con fecha límite en pasado");            
        }

        Tarea tarea = new Tarea(usuario, tareaDTO.getTitulo());
        tarea.setFechaLimite(tareaDTO.getFechaLimite());
        tareaRepository.save(tarea);
        return tarea;
    }

    @Transactional(readOnly = true)
    public List<Tarea> allTareasUsuario(Long idUsuario) {
        logger.debug("Devolviendo todas las tareas del usuario " + idUsuario);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new TareaServiceException("Usuario " + idUsuario + " no existe al listar tareas ");
        }
        List<Tarea> tareas = new ArrayList(usuario.getTareas());
        Collections.sort(tareas, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
        return tareas;
    }

    @Transactional(readOnly = true)
    public Tarea findById(Long tareaId) {
        logger.debug("Buscando tarea " + tareaId);
        return tareaRepository.findById(tareaId).orElse(null);
    }

    @Transactional
    public Tarea modificaTarea(Long idTarea, String nuevoTitulo) {
        logger.debug("Modificando tarea " + idTarea + " - " + nuevoTitulo);
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tarea.setTitulo(nuevoTitulo);
        tareaRepository.save(tarea);
        return tarea;
    }

    @Transactional
    public Tarea modificaTarea(Long idTarea, TareaData tareaDTO) {
        logger.debug("Modificando tarea " + idTarea + " - " + tareaDTO.getTitulo());
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }

        if (tareaDTO.getFechaLimite() != null && tareaDTO.getFechaLimite().before(new Date())) {
            throw new TareaServiceException("No puedes crear una tarea con fecha límite en pasado");            
        }

        tarea.setTitulo(tareaDTO.getTitulo());
        tarea.setFechaLimite(tareaDTO.getFechaLimite());
        tareaRepository.save(tarea);
        return tarea;
    }

    @Transactional
    public void changeStatus(Long idTarea, Status status) {
        logger.debug("Modificando tarea " + idTarea);
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tarea.changeStatus(status);
        tareaRepository.save(tarea);
    }

    @Transactional
    public void borraTarea(Long idTarea) {
        logger.debug("Borrando tarea " + idTarea);
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tareaRepository.delete(tarea);
    }
}
