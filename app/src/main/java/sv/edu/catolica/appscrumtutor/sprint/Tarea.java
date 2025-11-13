package sv.edu.catolica.appscrumtutor.sprint;

import com.google.gson.annotations.SerializedName;

public class Tarea {
    private int id;

    @SerializedName("idSprint")
    private Integer idSprint;

    @SerializedName("idHistoria")
    private Integer idHistoria;

    private String titulo;
    private String descripcion;

    @SerializedName("idEstado")
    private int idEstado;

    private String estado;

    @SerializedName("idAsignadoA")
    private Long idAsignadoA;

    @SerializedName("asignadoANombre")
    private String asignadoANombre;

    @SerializedName("fechaLimite")
    private String fechaLimite; // LocalDate → String

    @SerializedName("fechaCreacion")
    private String fechaCreacion; // LocalDateTime → String

    public int getId() {
        return id;
    }

    public Integer getIdSprint() {
        return idSprint;
    }

    public Integer getIdHistoria() {
        return idHistoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public String getEstado() {
        return estado;
    }

    public Long getIdAsignadoA() {
        return idAsignadoA;
    }

    public String getAsignadoANombre() {
        return asignadoANombre;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }
}


