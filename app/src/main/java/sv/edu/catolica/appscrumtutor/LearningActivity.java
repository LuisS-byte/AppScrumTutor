package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.Learning.QuizActivity;
import sv.edu.catolica.appscrumtutor.Learning.QuizApi;
import sv.edu.catolica.appscrumtutor.Learning.QuizDTO;
import sv.edu.catolica.appscrumtutor.Learning.TipApi;
import sv.edu.catolica.appscrumtutor.Learning.TipDTO;
import sv.edu.catolica.appscrumtutor.network.ApiClient;

public class LearningActivity extends AppCompatActivity {
    private LinearLayout layoutTemas;
    private TipApi tipApi;

    private LinearLayout layoutQuizzes;
    private QuizApi quizApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_learning);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_learning);

        HeaderController.setup(this);
        NavController.setup(this);

        layoutTemas = findViewById(R.id.layoutTemas);

        layoutQuizzes = findViewById(R.id.layoutQuizzes);
        quizApi = ApiClient.getClient(this).create(QuizApi.class);

        cargarQuizzes();

        tipApi = ApiClient.getClient(this).create(TipApi.class);

        cargarTemasDesdeBackend();
    }

    private void cargarTemasDesdeBackend() {
        tipApi.listarTodos().enqueue(new Callback<List<TipDTO>>() {
            @Override
            public void onResponse(Call<List<TipDTO>> call, Response<List<TipDTO>> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                for (TipDTO tip : response.body()) {
                    agregarTema(tip.getSeccion(), tip.getMensaje(), R.drawable.ic_launcher_background);
                }
            }

            @Override
            public void onFailure(Call<List<TipDTO>> call, Throwable t) {}
        });
    }
    private void cargarQuizzes() {
        quizApi.listar().enqueue(new Callback<List<QuizDTO>>() {
            @Override
            public void onResponse(Call<List<QuizDTO>> call, Response<List<QuizDTO>> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                for (QuizDTO quiz : response.body()) {
                    agregarQuizCard(quiz);
                }
            }

            @Override
            public void onFailure(Call<List<QuizDTO>> call, Throwable t) {}
        });
    }


    private void agregarQuizCard(QuizDTO quiz) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_quiz, layoutQuizzes, false);

        TextView tvTitulo = view.findViewById(R.id.tvQuizTitulo);
        TextView tvDescripcion = view.findViewById(R.id.tvQuizDescripcion);
        ImageView icono = view.findViewById(R.id.ivQuizIcon);

        tvTitulo.setText(quiz.getTitulo());
        tvDescripcion.setText(quiz.getDescripcion());
        icono.setImageResource(R.drawable.ic_launcher_background); // o un ícono personalizado

        view.setOnClickListener(v -> {
            Intent i = new Intent(this, QuizActivity.class);
            i.putExtra("idQuiz", quiz.getId());
            startActivity(i);
        });

        layoutQuizzes.addView(view);
    }



    private void agregarTema(String titulo, String descripcion, int icono) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tema_learning, layoutTemas, false);

        TextView tvTitulo = view.findViewById(R.id.tvTituloTema);
        TextView tvDescripcion = view.findViewById(R.id.tvDescripcionTema);
        ImageView ivIcono = view.findViewById(R.id.ivIconTema);

        tvTitulo.setText(titulo);
        tvDescripcion.setText(descripcion);
        ivIcono.setImageResource(icono);

        view.setOnClickListener(v -> {
            Intent intent = new Intent(this, DetalleTemaActivity.class);
            intent.putExtra("titulo", titulo);
            intent.putExtra("descripcion", descripcion);
            startActivity(intent);
        });

        layoutTemas.addView(view);
    }
}