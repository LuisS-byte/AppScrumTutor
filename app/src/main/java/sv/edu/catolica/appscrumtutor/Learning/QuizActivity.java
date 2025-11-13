package sv.edu.catolica.appscrumtutor.Learning;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.R;
import sv.edu.catolica.appscrumtutor.network.ApiClient;

public class QuizActivity extends AppCompatActivity {
    private TextView tvPregunta;
    private RadioGroup opciones;
    private RadioButton opA, opB, opC;
    private Button btn;

    private QuizApi api;
    private List<QuizPreguntaDTO> preguntas;

    private int index = 0;
    private int correctas = 0;
    private int idQuiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvPregunta = findViewById(R.id.tvPregunta);
        opciones = findViewById(R.id.opciones);
        opA = findViewById(R.id.opA);
        opB = findViewById(R.id.opB);
        opC = findViewById(R.id.opC);
        btn = findViewById(R.id.btnSiguiente);

        idQuiz = getIntent().getIntExtra("idQuiz", -1);
        api = ApiClient.getClient(this).create(QuizApi.class);


        cargarQuiz();
    }


    private void cargarQuiz() {
        api.getById(idQuiz).enqueue(new Callback<QuizWithPreguntasDTO>() {
            @Override
            public void onResponse(Call<QuizWithPreguntasDTO> call, Response<QuizWithPreguntasDTO> response) {
                preguntas = response.body().getPreguntas();
                mostrarPregunta();
            }

            @Override
            public void onFailure(Call<QuizWithPreguntasDTO> call, Throwable t) {}
        });
    }

    private void mostrarPregunta() {
        QuizPreguntaDTO p = preguntas.get(index);

        tvPregunta.setText(p.getPregunta());
        opA.setText(p.getOpcionA());
        opB.setText(p.getOpcionB());
        opC.setText(p.getOpcionC());

        btn.setOnClickListener(v -> {
            if (validarRespuesta(p)) correctas++;

            index++;

            if (index < preguntas.size()) {
                mostrarPregunta();
            } else {
                enviarProgreso();
            }
        });
    }

    private boolean validarRespuesta(QuizPreguntaDTO p) {
        int checked = opciones.getCheckedRadioButtonId();

        if (checked == opA.getId()) return p.getOpcionCorrecta().equalsIgnoreCase("A");
        if (checked == opB.getId()) return p.getOpcionCorrecta().equalsIgnoreCase("B");
        if (checked == opC.getId()) return p.getOpcionCorrecta().equalsIgnoreCase("C");

        return false;
    }

    private void enviarProgreso() {
        BigDecimal puntaje = BigDecimal.valueOf((correctas * 100.0) / preguntas.size());

        api.guardarProgreso(idQuiz, new QuizProgresoRequest(puntaje))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        tvPregunta.setText("Quiz terminado\nPuntaje: " + puntaje + "%");
                        opciones.setVisibility(RadioGroup.GONE);
                        btn.setVisibility(Button.GONE);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {}
                });
    }
}
