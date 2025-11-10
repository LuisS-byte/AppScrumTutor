package sv.edu.catolica.appscrumtutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LearningActivity extends AppCompatActivity {
    private LinearLayout layoutTemas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_learning);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_learning);

        HeaderController.setup(this);
        NavController.setup(this);

        layoutTemas = findViewById(R.id.layoutTemas);

        agregarTema("Introducción a Scrum",
                "Aprende los fundamentos y pilares del marco de trabajo Scrum.",
                R.drawable.ic_launcher_background);
        agregarTema("Roles en Scrum",
                "Descubre las responsabilidades del Product Owner, Scrum Master y equipo de desarrollo.",
                R.drawable.ic_launcher_background);
        agregarTema("Eventos en Scrum",
                "Conoce las reuniones clave que mantienen el flujo del equipo.",
                R.drawable.ic_launcher_background);
        agregarTema("Artefactos de Scrum",
                "Explora los elementos que proporcionan transparencia al progreso.",
                R.drawable.ic_launcher_background);
    }

    private void agregarTema(String titulo, String descripcion, int icono) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tema_learning, layoutTemas, false);

        TextView tvTitulo = view.findViewById(R.id.tvTituloTema);
        TextView tvDescripcion = view.findViewById(R.id.tvDescripcionTema);
        ImageView ivIcono = view.findViewById(R.id.ivIconTema);

        tvTitulo.setText(titulo);
        tvDescripcion.setText(descripcion);
        ivIcono.setImageResource(icono);

        // Ir al detalle
        view.setOnClickListener(v -> {
            Intent intent = new Intent(this, DetalleTemaActivity.class);
            intent.putExtra("titulo", titulo);
            intent.putExtra("descripcion", descripcion);
            startActivity(intent);
        });

        layoutTemas.addView(view);
    }
}