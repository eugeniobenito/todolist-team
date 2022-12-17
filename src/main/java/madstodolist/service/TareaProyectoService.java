package madstodolist.service;

import madstodolist.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TareaProyectoService {
    @Autowired
    ProyectoRepository proyectoRepository;

    @Autowired
    TareaProyectoRepository tareaProyectoRepository;

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
}
