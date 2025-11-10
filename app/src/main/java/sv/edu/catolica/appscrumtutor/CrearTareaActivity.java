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
        etFechaLimite.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(
                    CrearTareaActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Formatear la fecha a YYYY-MM-DD
                        String fecha = String.format(Locale.getDefault(),
                                "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        etFechaLimite.setText(fecha);
                    },
                    year, month, day
            );

            datePicker.show();
        });
        btnGuardarTarea.setOnClickListener(v -> {
            String titulo = etTituloTarea.getText().toString().trim();
            String desc = etDescripcionTarea.getText().toString().trim();
            String fecha = etFechaLimite.getText().toString().trim();

            if (titulo.isEmpty()) {
                Toast.makeText(this, "Título requerido", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Tarea creada (mock): " + titulo, Toast.LENGTH_SHORT).show();
            finish();
        });

    }
}