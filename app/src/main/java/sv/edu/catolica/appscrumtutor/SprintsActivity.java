package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SprintsActivity extends AppCompatActivity {

    private LinearLayout layoutSprints;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sprints);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        layoutSprints = findViewById(R.id.layoutSprints);

        // Simulamos obtener sprints del proyecto
        String[][] sprints = {
                {"Sprint 1", "Finalizado", "01/10", "15/10"},
                {"Sprint 2", "Activo", "16/10", "31/10"},
                {"Sprint 3", "Planificado", "01/11", "15/11"}
        };

        for (String[] s : sprints) agregarSprint(s[0], s[1], s[2], s[3]);
    }


    private void agregarSprint(String nombre, String estado, String inicio, String fin) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_sprint, layoutSprints, false);

        TextView tvNombre = view.findViewById(R.id.tvNombreSprint);
        TextView tvEstado = view.findViewById(R.id.tvEstado);
        TextView tvFechas = view.findViewById(R.id.tvFechas);
        Button btnVer = view.findViewById(R.id.btnVerSprint);

        tvNombre.setText(nombre);
        tvEstado.setText(estado);
        tvFechas.setText("📅 " + inicio + " → " + fin);

        btnVer.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProyectoDetalleActivity.class);
            intent.putExtra("nombreSprint", nombre);
            startActivity(intent);
        });

        layoutSprints.addView(view);
    }
}