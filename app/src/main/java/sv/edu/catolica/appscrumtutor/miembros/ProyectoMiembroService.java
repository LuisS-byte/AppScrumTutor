package sv.edu.catolica.appscrumtutor.miembros;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProyectoMiembroService {

    @PUT("api/proyectos/{idProyecto}/miembros/{idMiembro}/invitacion")
    Call<Miembro> actualizarInvitacion(
            @Path("idProyecto") long idProyecto,
            @Path("idMiembro") long idMiembro,
            @Body ProyectoMiembroInvitacionDTO dto
    );
}
