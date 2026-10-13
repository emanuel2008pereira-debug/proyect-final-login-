package loginmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;

import loginmvc.model.Alumno;
import loginmvc.model.Bitacora;

import loginmvc.service.AlumnoService;

import loginmvc.repository.BitacoraRepository;

@Controller
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private BitacoraRepository bitacoraRepository;

    @GetMapping("/alumnos")
    public String alumnos(
            Model model,
            HttpSession session) {

        model.addAttribute(
                "listaAlumnos",
                alumnoService.listarAlumnos());

        /* MANDAR ROL A LA VISTA */

        model.addAttribute(
                "rol",
                session.getAttribute("rol"));

        return "alumnos";
    }

    @PostMapping("/guardar-alumno")
    public String guardarAlumno(
            Alumno alumno,
            HttpSession session) {

        alumnoService.guardarAlumno(alumno);

        Bitacora bitacora = new Bitacora();

        bitacora.setUsuario(
                session.getAttribute("usuario")
                .toString());

        bitacora.setAccion(
                "Guardó alumno: "
                + alumno.getNombre());

        bitacoraRepository.save(bitacora);

        return "redirect:/alumnos";
    }

    @GetMapping("/eliminar-alumno/{id}")
    public String eliminarAlumno(
            @PathVariable Long id,
            HttpSession session) {

        Alumno alumno =
                alumnoService.obtenerAlumnoPorId(id);

        Bitacora bitacora = new Bitacora();

        bitacora.setUsuario(
                session.getAttribute("usuario")
                .toString());

        bitacora.setAccion(
                "Eliminó alumno: "
                + alumno.getNombre());

        bitacoraRepository.save(bitacora);

        alumnoService.eliminarAlumno(id);

        return "redirect:/alumnos";
    }

    @GetMapping("/editar-alumno/{id}")
    public String editarAlumno(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        Alumno alumno =
                alumnoService.obtenerAlumnoPorId(id);

        model.addAttribute(
                "alumno",
                alumno);

        model.addAttribute(
                "rol",
                session.getAttribute("rol"));

        return "editar-alumno";
    }

}