package sv.edu.catolica.appscrumtutor.sprint;

public class SprintUpdateDTO {
    private String nombre;
    private String objetivo;
    private String fechaInicio;
    private String fechaFin;
    private int idEstado;

    public SprintUpdateDTO(String nombre, String objetivo,
                           String fechaInicio, String fechaFin, int idEstado) {
        this.nombre = nombre;
        this.objetivo = objetivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idEstado = idEstado;
    }
}
