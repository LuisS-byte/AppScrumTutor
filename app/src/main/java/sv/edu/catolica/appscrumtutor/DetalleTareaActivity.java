package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
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


        // === Cargar datos del intent (mock o reales) ===
        String titulo = getIntent().getStringExtra("titulo");
        String descripcion = getIntent().getStringExtra("descripcion");
        String responsable = getIntent().getStringExtra("responsable");
        String estado = getIntent().getStringExtra("estado");
        String fecha = getIntent().getStringExtra("fecha");


        tvTitulo.setText(titulo != null ? titulo : "Diseñar login");
        tvDescripcion.setText(descripcion != null ? descripcion : "Crear la interfaz XML y conectar con el backend");
        tvResponsable.setText("👤 Asignado a: " + (responsable != null ? responsable : "José"));
        tvEstado.setText("📌 Estado: " + (estado != null ? estado : "En Progreso"));
        tvFechaLimite.setText("📅 Fecha límite: " + (fecha != null ? fecha : "2025-11-15"));
        tvHistoria.setText("🧩 Historia: Módulo de autenticación");
        tvSprint.setText("🏁 Sprint 1");

        // === Botones principales ===
        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearTareaActivity.class);
            intent.putExtra("modo", "editar");
            intent.putExtra("titulo", titulo);
            intent.putExtra("descripcion", descripcion);
            intent.putExtra("fecha", fecha);
            intent.putExtra("responsable", responsable);
            startActivity(intent);
        });
        // Botones de acción (simulados)

        btnEliminar.setOnClickListener(v -> Toast.makeText(this, "Eliminar tarea (mock)", Toast.LENGTH_SHORT).show());

        btnAgregarComentario.setOnClickListener(v -> {
            String comentario = etNuevoComentario.getText().toString().trim();
            if (comentario.isEmpty()) {
                Toast.makeText(this, "Comentario vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            agregarComentario("Tú", comentario);
            etNuevoComentario.setText("");
        });





    }


    /** Agrega visualmente un comentario al contenedor */
    private void agregarComentario(String usuario, String texto) {
        TextView comentarioView = new TextView(this);
        comentarioView.setText("💬 " + usuario + ": " + texto);
        comentarioView.setPadding(0, 8, 0, 8);
        comentarioView.setTextColor(0xFF333333);
        layoutComentarios.addView(comentarioView);
    }

}