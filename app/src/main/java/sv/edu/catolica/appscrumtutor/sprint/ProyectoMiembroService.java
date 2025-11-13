package sv.edu.catolica.appscrumtutor.sprint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProyectoMiembroService {

    @GET("api/proyectos/{idProyecto}/miembros")
    Call<List<ProyectoMiembroDto>> listar(@Path("idProyecto") long idProyecto);
}
