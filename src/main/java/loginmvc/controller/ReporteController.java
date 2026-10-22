package loginmvc.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import loginmvc.model.Asistencia;
import loginmvc.service.AsistenciaService;

@Controller
public class ReporteController {

    @Autowired
    private AsistenciaService asistenciaService;

    @GetMapping("/reportes")
    public String reportes(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) String alumno,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String llegada,
            Model model) {

        List<Asistencia> lista =
                asistenciaService.filtrar(desde, hasta, alumno, estado, llegada);

        model.addAttribute("listaReporte", lista);
        model.addAttribute("resumenAlumnos", asistenciaService.resumenPorAlumno(lista));

        model.addAttribute("totalRegistros", lista.size());
        model.addAttribute("totalTemprano", contar(lista, "Temprano"));
        model.addAttribute("totalPuntual", contar(lista, "Puntual"));
        model.addAttribute("totalTarde", contar(lista, "Tarde"));

        model.addAttribute("filtroDesde", desde);
        model.addAttribute("filtroHasta", hasta);
        model.addAttribute("filtroAlumno", alumno);
        model.addAttribute("filtroEstado", estado);
        model.addAttribute("filtroLlegada", llegada);

        return "reportes";
    }

    private long contar(List<Asistencia> lista, String llegada) {
        return lista.stream()
                .filter(a -> llegada.equals(a.getPuntualidad()))
                .count();
    }
}