package sv.edu.catolica.appscrumtutor;

import android.app.DatePickerDialog;
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

import java.util.Calendar;
import java.util.Locale;

public class GestionarSprintActivity extends AppCompatActivity {
    private EditText etNombreSprint, etObjetivo, etFechaInicio, etFechaFin;
    private Spinner spEstado;
    private Button btnGuardar;
    private boolean esEdicion = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestionar_sprint);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etNombreSprint = findViewById(R.id.etNombreSprint);
        etObjetivo = findViewById(R.id.etObjetivoSprint);
        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
        spEstado = findViewById(R.id.spEstadoSprint);
        btnGuardar = findViewById(R.id.btnGuardarSprint);

        configurarSelectorFecha(etFechaInicio);
        configurarSelectorFecha(etFechaFin);

        // Detectar si venimos a editar
        esEdicion = getIntent().getBooleanExtra("editar", false);

        if (esEdicion) {
            String nombre = getIntent().getStringExtra("nombre");
            String objetivo = getIntent().getStringExtra("objetivo");
            String inicio = getIntent().getStringExtra("inicio");
            String fin = getIntent().getStringExtra("fin");
            String estado = getIntent().getStringExtra("estado");

            etNombreSprint.setText(nombre);
            etObjetivo.setText(objetivo);
            etFechaInicio.setText(inicio);
            etFechaFin.setText(fin);
            btnGuardar.setText("Guardar cambios");
        }

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombreSprint.getText().toString().trim();
            String objetivo = etObjetivo.getText().toString().trim();
            String inicio = etFechaInicio.getText().toString().trim();
            String fin = etFechaFin.getText().toString().trim();

            if (nombre.isEmpty() || inicio.isEmpty() || fin.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            if (esEdicion)
                Toast.makeText(this, "Sprint actualizado (mock): " + nombre, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Sprint creado (mock): " + nombre, Toast.LENGTH_SHORT).show();

            finish();
        });
    }

    private void configurarSelectorFecha(EditText campo) {
        campo.setFocusable(false);
        campo.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int año = c.get(Calendar.YEAR);
            int mes = c.get(Calendar.MONTH);
            int dia = c.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(
                    this,
                    (view, añoSel, mesSel, diaSel) -> {
                        String fecha = String.format(Locale.getDefault(), "%04d-%02d-%02d", añoSel, mesSel + 1, diaSel);
                        campo.setText(fecha);
                    },
                    año, mes, dia
            ).show();
        });
    }
}