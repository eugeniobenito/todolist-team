package madstodolist.service;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Proyecto;
import madstodolist.model.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProyectoService {
    @Autowired
    ProyectoRepository proyectoRepository;

    @Autowired
    EquipoRepository equipoRepository;

    @Transactional
    public Proyecto crearProyecto(String nombre, Long equipoId){
        Equipo e = equipoRepository.findById(equipoId).orElse(null);
        if(e == null) {
            // TO DO
        }
        Proyecto p = new Proyecto(nombre, e);
        p = proyectoRepository.save(p);
        return p;
    }

    @Transactional(readOnly = true)
    public Proyecto getById(Long id){
        return proyectoRepository.findById(id).orElse(null);
    }
}
