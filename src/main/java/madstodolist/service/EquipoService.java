package madstodolist.service;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EquipoService {
    Logger logger = LoggerFactory.getLogger(EquipoService.class);

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Transactional
    public Equipo crearEquipo(String nombre) {
        if(nombre == "") throw new EquipoServiceException("El nombre del equipo no puede estar vacio");
        Equipo e = new Equipo(nombre);
        equipoRepository.save(e);
        return e;
    }

    @Transactional(readOnly = true)
    public Equipo recuperarEquipo(Long id) {

        return equipoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Equipo> findAllOrderedByName() {
        return equipoRepository.findAllByOrderByNombreAsc();
    }

    @Transactional
    public void addUsuarioEquipo(Long userId, Long equipoId) {
        Usuario user = usuarioRepository.findById(userId).orElse(null);
        Equipo equipo = equipoRepository.findById(equipoId).orElse(null);
        if(equipo == null) throw new EquipoServiceException("No existe el equipo con id " + equipoId);
        if(user == null) throw new EquipoServiceException("No existe el usuario con id " + userId);
        equipo.addUsuario(user);
    }

    @Transactional
    public void removeUsuarioEquipo(Long userId, Long equipoId){
        Usuario user = usuarioRepository.findById(userId).orElse(null);
        Equipo equipo = equipoRepository.findById(equipoId).orElse(null);
        if(equipo == null) throw new EquipoServiceException("No existe el equipo con id " + equipoId);
        if(user == null) throw new EquipoServiceException("No existe el usuario con id " + userId);
        equipo.removeUsuario(user);
    }

    @Transactional(readOnly = true)
    public List<Usuario> usuariosEquipo(Long equipoId) {
        Equipo e = equipoRepository.findById(equipoId).orElse(null);
        List<Usuario> l = new ArrayList<>(e.getUsuarios());
        return l;
    }

    @Transactional
    public Equipo modificarEquipo(Long equipoId, String nombre){
        Equipo e = equipoRepository.findById(equipoId).orElse(null);
        if(e == null) throw new EquipoServiceException("No existe el equipo con id " + equipoId);
        if(nombre == "") throw new EquipoServiceException("El nombre no puede estar vacio");
        e.setNombre(nombre);
        equipoRepository.save(e);
        return e;
    }

    @Transactional
    public Equipo modificarEquipo(Long equipoId, String nombre, String descripcion){
        Equipo e = equipoRepository.findById(equipoId).orElse(null);
        if(e == null) throw new EquipoServiceException("No existe el equipo con id " + equipoId);
        if(nombre == "") throw new EquipoServiceException("El nombre no puede estar vacio");
        e.setNombre(nombre);
        e.setDescripcion(descripcion);
        equipoRepository.save(e);
        return e;
    }

    @Transactional
    public void eliminarEquipo(Long equipoId){
        Equipo e = equipoRepository.findById(equipoId).orElse(null);
        if(e == null) throw new EquipoServiceException("No existe el equipo con id " + equipoId);
        equipoRepository.delete(e);
    }
}