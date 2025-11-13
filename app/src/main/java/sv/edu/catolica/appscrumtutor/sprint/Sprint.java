package sv.edu.catolica.appscrumtutor.sprint;

import com.google.gson.annotations.SerializedName;

public class Sprint {


    @SerializedName("id")
    private long idSprint;

    @SerializedName("idProyecto")
    private long idProyecto;

    private String nombre;
    private String objetivo;
    private String fechaInicio;
    private String fechaFin;

    @SerializedName("idEstado")
    private int idEstado;

    @SerializedName("estadoDescripcion")
    private String estadoDescripcion;

    public long getIdSprint() { return idSprint; }
    public long getIdProyecto() { return idProyecto; }
    public String getNombre() { return nombre; }
    public String getObjetivo() { return objetivo; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFin() { return fechaFin; }

    public int getIdEstado() { return idEstado; }           // ← NECESARIO
    public String getEstadoDescripcion() { return estadoDescripcion; }
}