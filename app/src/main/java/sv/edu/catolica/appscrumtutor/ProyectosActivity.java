package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProyectosActivity extends AppCompatActivity {


    LinearLayout layoutProjects;
    Button btnCrearProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proyectos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        layoutProjects = findViewById(R.id.layoutProjects);
        btnCrearProyecto = findViewById(R.id.btnCrearProyecto);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        String[][] proyectos = {
                {"Reservation Proyect", "Programa para gestión de reservas."},
                {"ControlDoc", "Gestión de documentos y archivos empresariales."}
        };

        if (proyectos.length == 0) {
            mostrarSinProyectos();
        } else {
            for (String[] p : proyectos) agregarProyecto(p[0], p[1]);
        }

        btnCrearProyecto.setOnClickListener(v ->
                Toast.makeText(this, "Crear nuevo proyecto (mock)", Toast.LENGTH_SHORT).show()
        );


        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_notifications)
                Toast.makeText(this, "Notificaciones (mock)", Toast.LENGTH_SHORT).show();
            else if (id == R.id.nav_profile)
                Toast.makeText(this, "Perfil (mock)", Toast.LENGTH_SHORT).show();
            return true;
        });
    }




    private void agregarProyecto(String nombre, String descripcion) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_proyecto, layoutProjects, false);

        TextView tvNombre = view.findViewById(R.id.tvNombreProyecto);
        TextView tvDesc = view.findViewById(R.id.tvDescripcionProyecto);
        Button btnIr = view.findViewById(R.id.btnIrProyecto);

        tvNombre.setText(nombre);
        tvDesc.setText(descripcion);
        btnIr.setOnClickListener(v -> {Intent intent = new Intent(this, ProyectoDetalleActivity.class);
        intent.putExtra("nombreProyecto", nombre);
        intent.putExtra("descripcionProyecto", descripcion);
        // En el futuro aquí también enviarás el idProyecto (para el backend)
        startActivity(intent);
    });

        layoutProjects.addView(view);
    }


    private void mostrarSinProyectos() {
        TextView msg = new TextView(this);
        msg.setText("No tienes proyectos");
        msg.setTextSize(18);
        msg.setPadding(0, 24, 0, 24);
        msg.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        layoutProjects.addView(msg);
    }

}