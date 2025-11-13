package sv.edu.catolica.appscrumtutor.Learning;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QuizApi {

    @GET("api/quizzes")
    Call<List<QuizDTO>> listar();

    @GET("api/quizzes/{id}")
    Call<QuizWithPreguntasDTO> getById(@Path("id") int idQuiz);

    @POST("api/quizzes/{idQuiz}/progreso")
    Call<Void> guardarProgreso(@Path("idQuiz") int idQuiz,
                               @Body QuizProgresoRequest req);


}
