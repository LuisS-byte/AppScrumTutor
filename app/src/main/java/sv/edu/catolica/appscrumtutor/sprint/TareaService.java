package sv.edu.catolica.appscrumtutor.sprint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TareaService {

    // === LISTAR TAREAS POR SPRINT ===
    @GET("api/sprints/{idSprint}/tareas")
    Call<List<Tarea>> listarPorSprint(@Path("idSprint") long idSprint);

    // === DETALLE DE TAREA ===
    @GET("api/tareas/{idTarea}")
    Call<Tarea> detalle(@Path("idTarea") long idTarea);

    // === CREAR TAREA ===
    @POST("api/tareas")
    Call<Tarea> crear(@Body TareaCreateDTO dto);

    // === EDITAR TAREA (incluye cambiar estado) ===
    @PUT("api/tareas/{idTarea}")
    Call<Tarea> actualizar(
            @Path("idTarea") long idTarea,
            @Body TareaUpdateDTO dto
    );

    // === ASIGNAR RESPONSABLE ===
    @PUT("api/tareas/{idTarea}/asignar")
    Call<Tarea> asignarResponsable(
            @Path("idTarea") long idTarea,
            @Body TareaAssignDTO dto
    );

    // === ELIMINAR TAREA ===
    @DELETE("api/tareas/{idTarea}")
    Call<Void> eliminar(@Path("idTarea") long idTarea);

}
