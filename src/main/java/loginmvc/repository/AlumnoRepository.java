package loginmvc.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import loginmvc.model.Alumno;

public interface AlumnoRepository
        extends JpaRepository<Alumno, Long> {

    Optional<Alumno> findByCarnet(String carnet);
}