package loginmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import loginmvc.model.Usuario;
import loginmvc.model.Bitacora;

import loginmvc.repository.BitacoraRepository;

import loginmvc.service.UsuarioService;

@Controller
public class HomeController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BitacoraRepository bitacoraRepository;

    /* LOGIN */

    @GetMapping("/")
    public String inicio() {

        return "login";

    }

    @GetMapping("/loginmvc")
    public String login() {

        return "login";

    }

    /* PROCESAR LOGIN */

    @PostMapping("/procesar-login")
    public String procesarLogin(

            @RequestParam String usuario,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        Usuario user =
                usuarioService.login(usuario, password);

        if(user != null) {

            /* GUARDAR SESION */

            session.setAttribute(
                    "usuario",
                    user.getUsuario());

            session.setAttribute(
                    "rol",
                    user.getRol());

            session.setAttribute(
                    "nombre",
                    user.getNombre());

            /* BITACORA */

            Bitacora bitacora =
                    new Bitacora();

            bitacora.setUsuario(
                    user.getUsuario());

            bitacora.setAccion(
                    "Inició sesión en el sistema");

            bitacoraRepository.save(bitacora);

            /* REDIRECCIONAR */

            return "redirect:/dashboard";
        }

        model.addAttribute(
                "error",
                "Usuario o contraseña incorrectos");

        return "login";
    }

    /* LOGOUT */

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/loginmvc";
    }

}