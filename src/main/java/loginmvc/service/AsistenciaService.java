package loginmvc.service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import loginmvc.model.Alumno;
import loginmvc.model.Asistencia;
import loginmvc.model.ResumenAlumno;
import loginmvc.repository.AlumnoRepository;
import loginmvc.repository.AsistenciaRepository;

@Service
public class AsistenciaService {

    private static final ZoneId ZONA = ZoneId.of("America/El_Salvador");

    @Value("${asistencia.entrada:}")
    private String horaEntrada;

    @Value("${asistencia.temprano:10}")
    private int minutosTemprano;

    @Value("${asistencia.tolerancia:10}")
    private int toleranciaMinutos;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    public List<Asistencia> listarAsistencias() {
        return asistenciaRepository.findAll();
    }

    public void guardarAsistencia(Asistencia asistencia) {
        if (asistencia.getFecha() == null) {
            asistencia.setFecha(LocalDate.now(ZONA));
        }
        asistenciaRepository.save(asistencia);
    }

    public Asistencia registrarPorCarnet(String carnet) {
        Alumno alumno = alumnoRepository.findByCarnet(carnet)
                .orElseThrow(() ->
                        new RuntimeException("Alumno no encontrado con carnet: " + carnet));

        LocalDateTime ahora = LocalDateTime.now(ZONA);
        LocalDate hoy = ahora.toLocalDate();

        boolean yaRegistrado =
                asistenciaRepository.existsByAlumnoIdAndFecha(alumno.getId(), hoy);
        if (yaRegistrado) {
            throw new RuntimeException("YA_REGISTRADO");
        }

        Asistencia asistencia = new Asistencia();
        asistencia.setAlumnoId(alumno.getId());
        asistencia.setAlumno(alumno.getNombre() + " " + alumno.getApellido());
        asistencia.setEstado("Presente");
        asistencia.setFecha(hoy);
        asistencia.setHora(ahora);
        asistencia.setPuntualidad(clasificarLlegada(ahora.toLocalTime()));

        return asistenciaRepository.save(asistencia);
    }

    public String clasificarLlegada(LocalTime hora) {
        LocalTime entrada = LocalTime.parse(
                (horaEntrada == null || horaEntrada.isBlank())
                        ? "07:00"
                        : horaEntrada.trim());

        if (!hora.isAfter(entrada.minusMinutes(minutosTemprano))) {
            return "Temprano";
        }
        if (!hora.isAfter(entrada.plusMinutes(toleranciaMinutos))) {
            return "Puntual";
        }
        return "Tarde";
    }

    public List<Asistencia> filtrar(LocalDate desde,
                                    LocalDate hasta,
                                    String alumno,
                                    String estado,
                                    String llegada) {

        String buscado = normalizar(alumno == null ? "" : alumno.trim());

        return asistenciaRepository.findAll().stream()
                .filter(a -> desde == null
                        || (a.getFecha() != null && !a.getFecha().isBefore(desde)))
                .filter(a -> hasta == null
                        || (a.getFecha() != null && !a.getFecha().isAfter(hasta)))
                .filter(a -> buscado.isEmpty()
                        || normalizar(a.getAlumno()).contains(buscado))
                .filter(a -> estado == null || estado.isBlank()
                        || estado.equalsIgnoreCase(a.getEstado()))
                .filter(a -> llegada == null || llegada.isBlank()
                        || llegada.equals(a.getPuntualidad()))
                .sorted(Comparator
                        .comparing(Asistencia::getFecha,
                                Comparator.nullsLast(Comparator.<LocalDate>reverseOrder()))
                        .thenComparing(Asistencia::getHora,
                                Comparator.nullsLast(Comparator.<LocalDateTime>reverseOrder())))
                .collect(Collectors.toList());
    }

    public List<ResumenAlumno> resumenPorAlumno(List<Asistencia> lista) {
        Map<String, ResumenAlumno> mapa = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (Asistencia a : lista) {
            String nombre = (a.getAlumno() == null || a.getAlumno().isBlank())
                    ? "(sin nombre)"
                    : a.getAlumno().trim();
            mapa.computeIfAbsent(nombre, ResumenAlumno::new).sumar(a);
        }

        return new ArrayList<>(mapa.values());
    }

    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }
}