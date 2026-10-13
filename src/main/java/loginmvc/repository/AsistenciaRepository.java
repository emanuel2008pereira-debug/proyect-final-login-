package loginmvc.repository;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import loginmvc.model.Asistencia;

public interface AsistenciaRepository
        extends JpaRepository<Asistencia, Long> {

    boolean existsByAlumnoIdAndFecha(Long alumnoId, LocalDate fecha);
}