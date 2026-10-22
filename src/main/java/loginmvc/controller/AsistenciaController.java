package loginmvc.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import loginmvc.model.Asistencia;
import loginmvc.service.AsistenciaService;

@Controller
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @GetMapping("/asistencias")
    public String asistencias(Model model) {
        model.addAttribute(
                "listaAsistencias",
                asistenciaService.listarAsistencias());
        return "asistencias";
    }

    @PostMapping("/guardar-asistencia")
    public String guardarAsistencia(
            @RequestParam String alumno,
            @RequestParam String estado) {
        Asistencia asistencia = new Asistencia();
        asistencia.setAlumno(alumno);
        asistencia.setEstado(estado);
        asistenciaService.guardarAsistencia(asistencia);
        return "redirect:/asistencias";
    }

    @GetMapping("/registrar-asistencia")
    public String registrarAsistencia() {
        return "registrar-asistencia";
    }

    @PostMapping("/api/asistencias/qr")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> registrarPorQr(
            @RequestBody Map<String, String> body) {

        String carnet = body.get("carnet");
        Map<String, Object> respuesta = new HashMap<>();

        try {
            Asistencia asistencia = asistenciaService.registrarPorCarnet(carnet);
            respuesta.put("success", true);
            respuesta.put("alumno", asistencia.getAlumno());
            respuesta.put("hora", asistencia.getHora().toString());
            respuesta.put("llegada", asistencia.getPuntualidad());
            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {
            String mensaje = "YA_REGISTRADO".equals(e.getMessage())
                    ? "Este alumno ya tiene asistencia registrada hoy"
                    : e.getMessage();
            respuesta.put("success", false);
            respuesta.put("mensaje", mensaje);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }
    }
}