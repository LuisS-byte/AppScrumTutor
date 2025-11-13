package sv.edu.catolica.appscrumtutor.sprint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface HistoriaService {
    @GET("api/proyectos/{idProyecto}/historias")
    Call<List<Historia>> listarPorProyecto(@Path("idProyecto") long idProyecto);

    @POST("api/historias")
    Call<Historia> crear(@Body HistoriaCreateDTO historia);

    @GET("api/sprints/{idSprint}/historias")
    Call<List<Historia>> listarPorSprint(@Path("idSprint") long idSprint);

    @GET("api/historias/{idHistoria}")
    Call<Historia> detalle(@Path("idHistoria") long idHistoria);

    @PUT("api/historias/{idHistoria}")
    Call<Historia> actualizar(
            @Path("idHistoria") long idHistoria,
            @Body HistoriaUpdateDTO historiaUpdateDTO
    );

    @PUT("api/historias/{idHistoria}/asignar-sprint/{idSprint}")
    Call<Historia> asignarSprint(
            @Path("idHistoria") long idHistoria,
            @Path("idSprint")  long idSprint
    );

    @DELETE("api/historias/{idHistoria}")
    Call<Void> eliminar(@Path("idHistoria") long idHistoria);
}