package sv.edu.catolica.appscrumtutor.sprint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SprintService {

    @GET("/api/proyectos/{idProyecto}/sprints")
    Call<List<Sprint>> listarPorProyecto(@Path("idProyecto") long idProyecto);

}