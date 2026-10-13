package loginmvc.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import loginmvc.model.Usuario;
import loginmvc.repository.UsuarioRepository;

import loginmvc.util.SHA256;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario login(String usuario, String password) {

        Optional<Usuario> usuarioEncontrado =
                usuarioRepository.findByUsuario(usuario);

        if (usuarioEncontrado.isPresent()) {

            Usuario user = usuarioEncontrado.get();

            String passwordEncriptada =
                    SHA256.encrypt(password);

            System.out.println(passwordEncriptada);

            if (passwordEncriptada.equals(
                    user.getPasswordHash())) {

                System.out.println("LOGIN CORRECTO");

                return user;
            }

        }

        System.out.println("LOGIN INCORRECTO");

        return null;
    }

}