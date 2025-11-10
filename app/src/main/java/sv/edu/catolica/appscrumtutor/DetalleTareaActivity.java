package sv.edu.catolica.appscrumtutor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetalleTareaActivity extends AppCompatActivity {

    private TextView tvTitulo, tvDescripcion, tvResponsable, tvEstado, tvFechaLimite, tvHistoria, tvSprint;
    private LinearLayout layoutComentarios;
    private EditText etNuevoComentario;
    private Button btnAgregarComentario, btnEditar, btnEliminar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_tarea);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content),
                (v, insets) -> {
                    v.setPadding(
                            insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                            insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                            insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                            insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
                    );
                    return insets;
                });


        // Referencias
        tvTitulo = findViewById(R.id.tvTituloTarea);
        tvDescripcion = findViewById(R.id.tvDescripcionTarea);
        tvResponsable = findViewById(R.id.tvResponsable);
        tvEstado = findViewById(R.id.tvEstado);
        tvFechaLimite = findViewById(R.id.tvFechaLimite);
        tvHistoria = findViewById(R.id.tvHistoria);
        tvSprint = findViewById(R.id.tvSprint);
        layoutComentarios = findViewById(R.id.layoutComentarios);
        etNuevoComentario = findViewById(R.id.etNuevoComentario);
        btnAgregarComentario = findViewById(R.id.btnAgregarComentario);
        btnEditar = findViewById(R.id.btnEditarTarea);
        btnEliminar = findViewById(R.id.btnEliminarTarea);

        // Cargar datos simulados (mock)
        tvTitulo.setText("Diseñar login");
        tvDescripcion.setText("Crear la interfaz XML y conectar con el backend");
        tvResponsable.setText("👤 Asignado a: José");
        tvEstado.setText("📌 Estado: En Progreso");
        tvFechaLimite.setText("📅 2025-11-15");
        tvHistoria.setText("🧩 Historia: Módulo de autenticación");
        tvSprint.setText("🏁 Sprint 1");

        // Botones de acción (simulados)
        btnEditar.setOnClickListener(v -> Toast.makeText(this, "Editar tarea (mock)", Toast.LENGTH_SHORT).show());
        btnEliminar.setOnClickListener(v -> Toast.makeText(this, "Eliminar tarea (mock)", Toast.LENGTH_SHORT).show());

        btnAgregarComentario.setOnClickListener(v -> {
            String comentario = etNuevoComentario.getText().toString().trim();
            if (comentario.isEmpty()) return;

            agregarComentario("Tú", comentario);
            etNuevoComentario.setText("");
        });






    }
    private void agregarComentario(String usuario, String texto) {
        TextView comentarioView = new TextView(this);
        comentarioView.setText("💬 " + usuario + ": " + texto);
        comentarioView.setPadding(0, 8, 0, 8);
        comentarioView.setTextColor(0xFF333333);
        layoutComentarios.addView(comentarioView);
    }


}