package loginmvc.model;

public class ResumenAlumno {

    private final String alumno;
    private int total;
    private int temprano;
    private int puntual;
    private int tarde;
    private int presentes;
    private int ausentes;

    public ResumenAlumno(String alumno) {
        this.alumno = alumno;
    }

    public void sumar(Asistencia a) {
        total++;

        if ("Presente".equalsIgnoreCase(a.getEstado())) {
            presentes++;
        } else if ("Ausente".equalsIgnoreCase(a.getEstado())) {
            ausentes++;
        }

        if ("Temprano".equals(a.getPuntualidad())) {
            temprano++;
        } else if ("Puntual".equals(a.getPuntualidad())) {
            puntual++;
        } else if ("Tarde".equals(a.getPuntualidad())) {
            tarde++;
        }
    }

    public String getAlumno() {
        return alumno;
    }

    public int getTotal() {
        return total;
    }

    public int getTemprano() {
        return temprano;
    }

    public int getPuntual() {
        return puntual;
    }

    public int getTarde() {
        return tarde;
    }

    public int getPresentes() {
        return presentes;
    }

    public int getAusentes() {
        return ausentes;
    }
}