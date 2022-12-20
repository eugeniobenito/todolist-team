package madstodolist.service;

import madstodolist.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class TareaProyectoService {
    @Autowired
    ProyectoRepository proyectoRepository;

    @Autowired
    TareaProyectoRepository tareaProyectoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Transactional
    public TareaProyecto crearTareaProyectoService(String nombre, Long proyectoId){
        Proyecto p = proyectoRepository.findById(proyectoId).orElse(null);
        if(p == null) throw new TareaProyectoServiceException("No se ha encontrado la entidad");
        TareaProyecto tp = new TareaProyecto(nombre, p);
        tp = tareaProyectoRepository.save(tp);
        return tp;
    }

    @Transactional(readOnly = true)
    public TareaProyecto findById(Long tareaId){
        return tareaProyectoRepository.findById(tareaId).orElse(null);
    }

    @Transactional
    public void eliminarTareaProyecto(Long tareaId){
        TareaProyecto tarea = tareaProyectoRepository.findById(tareaId).orElse(null);
        if(tarea == null) { throw new TareaProyectoServiceException("No se ha encontrado la entidad"); }
        Set<TareaProyecto> tareas = tarea.getProyecto().getTareasProyecto();
        tareas.remove(tarea);
        proyectoRepository.save(tarea.getProyecto());
        tareaProyectoRepository.delete(tarea);
    }

    @Transactional
    public TareaProyecto cambiarEstado(Long tareaId, Status status){
        TareaProyecto tarea = findById(tareaId);
        if(tarea == null) { throw new TareaProyectoServiceException("No se ha encontrado la entidad"); }
        tarea.setStatus(status);
        tarea = tareaProyectoRepository.save(tarea);
        return tarea; 
    }

    @Transactional
    public void addUsuario(Long tareaProyectoId, Long userId) {
        Usuario usuario = usuarioRepository.findById(userId).orElse(null);
        if(usuario == null) throw new TareaProyectoServiceException("No se ha encontrado el usuario");
        TareaProyecto tarea = tareaProyectoRepository.findById(tareaProyectoId).orElse(null);
        if(tarea == null) throw new TareaProyectoServiceException("No se ha encontrado la tarea");

        tarea.addUsuario(usuario);
    }
}
