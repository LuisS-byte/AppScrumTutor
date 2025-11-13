package sv.edu.catolica.appscrumtutor.sprint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TareaService {

    @GET("api/sprints/{idSprint}/tareas")
    Call<List<Tarea>> listarPorSprint(@Path("idSprint") long idSprint);
}
