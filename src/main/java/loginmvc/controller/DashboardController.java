package loginmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

import loginmvc.service.AlumnoService;
import loginmvc.service.AsistenciaService;

@Controller
public class DashboardController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private AsistenciaService asistenciaService;

    @GetMapping("/dashboard")
    public String dashboard(
            Model model,
            HttpSession session) {

        /* VALIDAR SESION */

        if(session.getAttribute("usuario") == null) {

            return "redirect:/loginmvc";
        }

        model.addAttribute(
                "usuario",
                session.getAttribute("usuario"));

        model.addAttribute(
                "rol",
                session.getAttribute("rol"));

        model.addAttribute(
                "nombre",
                session.getAttribute("nombre"));

        model.addAttribute(
                "totalAlumnos",
                alumnoService.listarAlumnos().size());

        model.addAttribute(
                "totalAsistencias",
                asistenciaService.listarAsistencias().size());

        return "dashboard";
    }

}