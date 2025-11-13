package sv.edu.catolica.appscrumtutor.sprint;

public class TareaCreateDTO { private Integer idSprint;
    private Integer idHistoria;
    private String titulo;
    private String descripcion;
    private Integer idEstado;
    private String fechaLimite;
    private Long idAsignadoA;

    public TareaCreateDTO(Integer idSprint, Integer idHistoria, String titulo,
                          String descripcion, Integer idEstado,
                          String fechaLimite, Long idAsignadoA) {
        this.idSprint = idSprint;
        this.idHistoria = idHistoria;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idEstado = idEstado;
        this.fechaLimite = fechaLimite;
        this.idAsignadoA = idAsignadoA;
    }
}