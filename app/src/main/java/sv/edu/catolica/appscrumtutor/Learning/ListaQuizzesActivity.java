package sv.edu.catolica.appscrumtutor.Learning;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.R;
import sv.edu.catolica.appscrumtutor.network.ApiClient;

public class ListaQuizzesActivity extends AppCompatActivity {
    private LinearLayout layout;
    private QuizApi quizApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_quizzes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        layout = findViewById(R.id.layoutQuizzes);
        quizApi = ApiClient.getClient(this).create(QuizApi.class);


        cargar();
    }


    private void cargar() {
        quizApi.listar().enqueue(new Callback<List<QuizDTO>>() {
            @Override
            public void onResponse(Call<List<QuizDTO>> call, Response<List<QuizDTO>> response) {
                if (!response.isSuccessful()) return;

                for (QuizDTO quiz : response.body()) {
                    Button btn = new Button(ListaQuizzesActivity.this);
                    btn.setText(quiz.getTitulo());
                    btn.setOnClickListener(v -> abrirQuiz(quiz.getId()));
                    layout.addView(btn);
                }
            }

            @Override
            public void onFailure(Call<List<QuizDTO>> call, Throwable t) {}
        });
    }

    private void abrirQuiz(int idQuiz) {
        Intent i = new Intent(this, QuizActivity.class);
        i.putExtra("idQuiz", idQuiz);
        startActivity(i);
    }
}
