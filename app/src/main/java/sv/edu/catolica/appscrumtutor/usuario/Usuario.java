package sv.edu.catolica.appscrumtutor.usuario;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    @SerializedName("id")
    private Long idUsuario;
    private String nombre;
    private String correo;

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
