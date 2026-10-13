package loginmvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import loginmvc.model.Alumno;
import loginmvc.repository.AlumnoRepository;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    public List<Alumno> listarAlumnos() {

        return alumnoRepository.findAll();

    }

    public void guardarAlumno(Alumno alumno) {

        alumnoRepository.save(alumno);

    }

    public void eliminarAlumno(Long id) {

        alumnoRepository.deleteById(id);

    }

    public Alumno obtenerAlumnoPorId(Long id) {

        return alumnoRepository
                .findById(id)
                .orElse(null);

    }

}