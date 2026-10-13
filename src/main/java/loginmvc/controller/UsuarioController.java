package loginmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import loginmvc.model.Usuario;
import loginmvc.repository.UsuarioRepository;
import loginmvc.util.SHA256;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/usuarios")
    public String usuarios() {

        return "usuarios";
    }

    @PostMapping("/guardar-usuario")
    public String guardarUsuario(

            @RequestParam String usuario,
            @RequestParam String password) {

        Usuario nuevo = new Usuario();

        nuevo.setUsuario(usuario);

        nuevo.setPasswordHash(
                SHA256.encrypt(password));

        nuevo.setNombre(usuario);

        nuevo.setCorreo(usuario + "@correo.com");

        nuevo.setRol("USER");

        nuevo.setEstado(true);

        usuarioRepository.save(nuevo);

        return "redirect:/usuarios";
    }
}