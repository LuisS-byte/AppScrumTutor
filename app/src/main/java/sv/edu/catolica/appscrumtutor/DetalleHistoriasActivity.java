package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetalleHistoriasActivity extends AppCompatActivity {
    private TextView tvTituloHistoria, tvDescripcionHistoria, tvPrioridad, tvEstado, tvCriteriosAceptacion;
    private Button btnEditarHistoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_historias);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });
// Referencias UI
        tvTituloHistoria = findViewById(R.id.tvTituloHistoria);
        tvDescripcionHistoria = findViewById(R.id.tvDescripcionHistoria);
        tvPrioridad = findViewById(R.id.tvPrioridad);
        tvEstado = findViewById(R.id.tvEstado);
        tvCriteriosAceptacion = findViewById(R.id.tvCriteriosAceptacion);
        btnEditarHistoria = findViewById(R.id.btnEditarHistoria);

        // Cargar datos recibidos por Intent
        String titulo = getIntent().getStringExtra("titulo");
        String descripcion = getIntent().getStringExtra("descripcion");
        String prioridad = getIntent().getStringExtra("prioridad");

        tvTituloHistoria.setText(titulo);
        tvDescripcionHistoria.setText(descripcion);
        tvPrioridad.setText("Prioridad: " + prioridad);
        tvEstado.setText("Estado: Pendiente"); // Mock hasta integrar backend
        tvCriteriosAceptacion.setText("• Mostrar mensaje de error si el login falla.\n• Redirigir al dashboard tras inicio exitoso.");

        // Acción del botón Editar → abrir HistoriaFormActivity
        btnEditarHistoria.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearHistoriasActivity.class);
            intent.putExtra("modo", "editar");
            intent.putExtra("titulo", titulo);
            intent.putExtra("descripcion", descripcion);
            intent.putExtra("criterios", tvCriteriosAceptacion.getText().toString());
            startActivity(intent);
        });

    }
}