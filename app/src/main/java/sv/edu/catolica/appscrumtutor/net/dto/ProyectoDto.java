// sv/edu/catolica/appscrumtutor/net/dto/ProyectoDto.java
package sv.edu.catolica.appscrumtutor.net.dto;

import java.time.LocalDate;

public class ProyectoDto {
    public Long id;
    public String nombre;
    public String descripcion;
    public String fechaInicio; // usa String para evitar problemas con java.time en Android
    public String fechaFin;
    public Long idUsuario;
}
