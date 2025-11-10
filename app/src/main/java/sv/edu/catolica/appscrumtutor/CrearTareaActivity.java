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

public class CrearTareaActivity extends AppCompatActivity {
    private EditText etTituloTarea, etDescripcionTarea, etFechaLimite;
    private Spinner spEstadoTarea;
    private Button btnGuardarTarea;
    private String modo; // "crear" o "editar"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_tarea);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etTituloTarea = findViewById(R.id.etTituloTarea);
        etDescripcionTarea = findViewById(R.id.etDescripcionTarea);

        spEstadoTarea = findViewById(R.id.spEstadoTarea);
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea);
        etFechaLimite = findViewById(R.id.etFechaLimite);

        etFechaLimite.setFocusable(false); // Evita teclado
        etFechaLimite.setOnClickListener(v -> mostrarDatePicker());

        // === Detectar modo ===
        modo = getIntent().getStringExtra("modo");
        if (modo == null) modo = "crear";

        if (modo.equals("editar")) {
            precargarDatos();
        }


    }
    /** Muestra el selector de fecha y coloca el resultado en el campo */
    private void mostrarDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String fecha = String.format(Locale.getDefault(),
                            "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    etFechaLimite.setText(fecha);
                },
                year, month, day
        );
        datePicker.show();
    }

    /** Si viene del detalle, precarga los campos */
    private void precargarDatos() {
        String titulo = getIntent().getStringExtra("titulo");
        String descripcion = getIntent().getStringExtra("descripcion");
        String fecha = getIntent().getStringExtra("fecha");

        etTituloTarea.setText(titulo != null ? titulo : "");
        etDescripcionTarea.setText(descripcion != null ? descripcion : "");
        etFechaLimite.setText(fecha != null ? fecha : "");

        btnGuardarTarea.setText("Actualizar tarea");
    }

    /** Guarda o actualiza la tarea según el modo */
    private void guardarTarea() {
        String titulo = etTituloTarea.getText().toString().trim();
        String desc = etDescripcionTarea.getText().toString().trim();
        String fecha = etFechaLimite.getText().toString().trim();

        if (titulo.isEmpty()) {
            Toast.makeText(this, "Título requerido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (modo.equals("editar")) {
            Toast.makeText(this, "Tarea actualizada (mock): " + titulo, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tarea creada (mock): " + titulo, Toast.LENGTH_SHORT).show();
        }

        finish();
    }


}