package loginmvc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import loginmvc.model.Alumno;
import loginmvc.model.Asistencia;
import loginmvc.repository.AlumnoRepository;
import loginmvc.repository.AsistenciaRepository;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    public List<Asistencia> listarAsistencias() {
        return asistenciaRepository.findAll();
    }

    public void guardarAsistencia(Asistencia asistencia) {
        asistenciaRepository.save(asistencia);
    }

    public Asistencia registrarPorCarnet(String carnet) {
        Alumno alumno = alumnoRepository.findByCarnet(carnet)
                .orElseThrow(() ->
                        new RuntimeException("Alumno no encontrado con carnet: " + carnet));

        LocalDate hoy = LocalDate.now();

        boolean yaRegistrado =
                asistenciaRepository.existsByAlumnoIdAndFecha(alumno.getId(), hoy);
        if (yaRegistrado) {
            throw new RuntimeException("YA_REGISTRADO");
        }

        Asistencia asistencia = new Asistencia();
        asistencia.setAlumnoId(alumno.getId());
        asistencia.setAlumno(alumno.getNombre() + " " + alumno.getApellido());
        asistencia.setEstado("Presente");
        asistencia.setFecha(hoy);
        asistencia.setHora(LocalDateTime.now());

        return asistenciaRepository.save(asistencia);
    }
}