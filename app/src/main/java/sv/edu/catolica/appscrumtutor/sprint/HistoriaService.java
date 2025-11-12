package sv.edu.catolica.appscrumtutor.sprint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HistoriaService {
    @GET("api/proyectos/{idProyecto}/historias")
    Call<List<Historia>> listarPorProyecto(@Path("idProyecto") long idProyecto);
}