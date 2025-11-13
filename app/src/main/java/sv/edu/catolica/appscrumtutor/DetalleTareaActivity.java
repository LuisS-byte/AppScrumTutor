package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.sprint.Tarea;
import sv.edu.catolica.appscrumtutor.sprint.TareaService;

public class DetalleTareaActivity extends AppCompatActivity {

    private TextView tvTitulo, tvDescripcion, tvResponsable, tvEstado, tvFechaLimite, tvHistoria, tvSprint;
    private LinearLayout layoutComentarios;
    private EditText etNuevoComentario;
    private Button btnAgregarComentario, btnEditar, btnEliminar;

    private int idTarea;
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

        idTarea = getIntent().getIntExtra("idTarea", -1);
        if (idTarea == -1) {
            Toast.makeText(this, "Error: tarea inválida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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

        cargarDetalle();

        btnEditar.setOnClickListener(v -> irAEditar());

        btnEliminar.setOnClickListener(v -> confirmarEliminar());

        btnAgregarComentario.setOnClickListener(v -> {
            String txt = etNuevoComentario.getText().toString().trim();
            if (txt.isEmpty()) {
                Toast.makeText(this, "Comentario vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            agregarComentario("Tú", txt);
            etNuevoComentario.setText("");
        });
    }

    private void cargarDetalle() {
        TareaService api = ApiClient.getClient(this).create(TareaService.class);

        api.detalle(idTarea).enqueue(new Callback<Tarea>() {
            @Override
            public void onResponse(Call<Tarea> call, Response<Tarea> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(DetalleTareaActivity.this, "No se pudo cargar tarea", Toast.LENGTH_SHORT).show();
                    return;
                }

                Tarea t = response.body();
                tvTitulo.setText(t.getTitulo());
                tvDescripcion.setText(t.getDescripcion());
                tvResponsable.setText("👤 Asignado a: " + (t.getAsignadoANombre() != null ? t.getAsignadoANombre() : "Sin asignar"));
                tvEstado.setText("📌 Estado: " + t.getEstado());
                tvFechaLimite.setText("📅 Fecha límite: " + (t.getFechaLimite() != null ? t.getFechaLimite() : "-"));
                tvHistoria.setText("🧩 Historia: " + (t.getIdHistoria() != null ? t.getIdHistoria() : "Ninguna"));
                tvSprint.setText("🏁 Sprint: " + (t.getIdSprint() != null ? t.getIdSprint() : "-"));
            }

            @Override
            public void onFailure(Call<Tarea> call, Throwable t) {
                Toast.makeText(DetalleTareaActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void irAEditar() {
        Intent intent = new Intent(this, CrearTareaActivity.class);
        intent.putExtra("modo", "editar");
        intent.putExtra("idTarea", idTarea);
        intent.putExtra("idProyecto", getIntent().getLongExtra("idProyecto", -1));
        intent.putExtra("idSprint", getIntent().getLongExtra("idSprint", -1));
        startActivity(intent);
    }

    private void confirmarEliminar() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar tarea")
                .setMessage("¿Seguro que deseas eliminar esta tarea?")
                .setPositiveButton("Sí", (d, w) -> eliminar())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminar() {
        TareaService api = ApiClient.getClient(this).create(TareaService.class);

        api.eliminar(idTarea).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetalleTareaActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DetalleTareaActivity.this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DetalleTareaActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarComentario(String usuario, String texto) {
        TextView tv = new TextView(this);
        tv.setText("💬 " + usuario + ": " + texto);
        tv.setPadding(0, 8, 0, 8);
        tv.setTextColor(0xFF333333);
        layoutComentarios.addView(tv);
    }
}