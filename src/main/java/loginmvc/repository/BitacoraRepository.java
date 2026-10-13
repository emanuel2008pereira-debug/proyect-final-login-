package loginmvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import loginmvc.model.Bitacora;

public interface BitacoraRepository
        extends JpaRepository<Bitacora, Long> {

}