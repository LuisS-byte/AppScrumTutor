package sv.edu.catolica.appscrumtutor.sprint;

import com.google.gson.annotations.SerializedName;

public class TareaUpdateDTO {
    private Integer idHistoria;
    private String titulo;
    private String descripcion;
    private Integer idEstado;
    private String fechaLimite;
    private Long idAsignadoA;

    public TareaUpdateDTO(Integer idHistoria, String titulo,
                          String descripcion, Integer idEstado,
                          String fechaLimite, Long idAsignadoA) {

        this.idHistoria = idHistoria;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idEstado = idEstado;
        this.fechaLimite = fechaLimite;
        this.idAsignadoA = idAsignadoA;
    }

}
