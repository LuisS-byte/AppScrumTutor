package sv.edu.catolica.appscrumtutor.sprint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SprintService {

    @GET("/api/proyectos/{idProyecto}/sprints")
    Call<List<Sprint>> listarPorProyecto(@Path("idProyecto") long idProyecto);

    @POST("/api/sprints")
    Call<Sprint> crearSprint(@Body SprintCreateDTO dto);

    @PUT("/api/sprints/{idSprint}")
    Call<Sprint> actualizarSprint(@Path("idSprint") long idSprint,
                                  @Body SprintUpdateDTO dto);

    @DELETE("/api/sprints/{idSprint}")
    Call<Void> eliminarSprint(@Path("idSprint") long idSprint);

    @GET("/api/sprints/{idSprint}")
    Call<Sprint> detalle(@Path("idSprint") long idSprint);



}