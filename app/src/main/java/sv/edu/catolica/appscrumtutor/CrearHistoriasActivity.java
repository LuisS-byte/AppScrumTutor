package sv.edu.catolica.appscrumtutor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CrearHistoriasActivity extends AppCompatActivity {
    private EditText etTitulo, etDescripcion, etCriterios;
    private Spinner spPrioridad;
    private Button btnGuardarHistoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_historias);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etCriterios = findViewById(R.id.etCriterios);
        spPrioridad = findViewById(R.id.spPrioridad);
        btnGuardarHistoria = findViewById(R.id.btnGuardarHistoria);

        btnGuardarHistoria.setOnClickListener(v -> {
            String titulo = etTitulo.getText().toString().trim();
            String desc = etDescripcion.getText().toString().trim();
            String criterios = etCriterios.getText().toString().trim();
            String prioridad = spPrioridad.getSelectedItem().toString();

            if (titulo.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Historia creada (mock): " + titulo, Toast.LENGTH_SHORT).show();
            finish();

        });

    }
}