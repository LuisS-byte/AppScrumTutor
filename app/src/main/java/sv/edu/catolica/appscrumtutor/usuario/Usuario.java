package sv.edu.catolica.appscrumtutor.usuario;

import com.google.gson.annotations.SerializedName;
import sv.edu.catolica.appscrumtutor.rol.Rol;

public class Usuario {
    @SerializedName("id")
    private Long idUsuario;
    private String nombre;
    private String correo;
    private Rol rol;       // Usa la clase del paquete rol
    private boolean activo;

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
