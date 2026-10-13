package loginmvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import loginmvc.model.Bitacora;
import loginmvc.repository.BitacoraRepository;

@Service
public class BitacoraService {

    @Autowired
    private BitacoraRepository bitacoraRepository;

    public void guardar(

            String usuario,
            String accion) {

        Bitacora bitacora =
                new Bitacora();

        bitacora.setUsuario(usuario);

        bitacora.setAccion(accion);

        bitacoraRepository.save(bitacora);

    }

}