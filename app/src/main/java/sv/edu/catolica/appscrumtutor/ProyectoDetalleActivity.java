package sv.edu.catolica.appscrumtutor;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import sv.edu.catolica.appscrumtutor.fragments.ActividadesFragment;
import sv.edu.catolica.appscrumtutor.fragments.BacklogFrament;
import sv.edu.catolica.appscrumtutor.fragments.CalendarioFrament;
import sv.edu.catolica.appscrumtutor.proyectoDetalle.AdapterProyecto;

public class ProyectoDetalleActivity extends AppCompatActivity {



    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proyecto_detalle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_projects); // ← marca el botón actual

        HeaderController.setup(this);
        NavController.setup(this);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        TextView tvSprintTitle = findViewById(R.id.tvSprintTitle);
        TextView tvTitulo = findViewById(R.id.tvProjectTitle);





        // Recuperar datos del Intent
        long idProyecto = getIntent().getLongExtra("idProyecto", -1);
        long idSprint = getIntent().getLongExtra("idSprint", -1);
        String nombreProyecto = getIntent().getStringExtra("nombreProyecto");
        String nombreSprint = getIntent().getStringExtra("nombreSprint");

        // --- Mostrar información real ---
        if (nombreProyecto != null && !nombreProyecto.isEmpty()) {
            tvTitulo.setText(nombreProyecto);
        } else {
            tvTitulo.setText("Proyecto sin nombre");
        }

        if (nombreSprint != null && !nombreSprint.isEmpty()) {
            tvSprintTitle.setText("Sprint actual: " + nombreSprint);
        } else {
            tvSprintTitle.setText("Sprint actual: N/A");
        }

        // Configurar adapter
        String fechaInicio = getIntent().getStringExtra("fechaInicioSprint");
        String fechaFin = getIntent().getStringExtra("fechaFinSprint");

        viewPager.setAdapter(new AdapterProyecto(this, idProyecto, idSprint, fechaInicio, fechaFin));

        viewPager.setUserInputEnabled(false);

        // Tabs
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Backlog Sprints"); break;
                case 1: tab.setText("Actividades"); break;
                case 2: tab.setText("Calendario"); break;
            }
        }).attach();


        Log.d("ProyectoDetalleActivity",
                "Recibido idProyecto=" + idProyecto + ", idSprint=" + idSprint +
                        ", nombreProyecto=" + nombreProyecto + ", nombreSprint=" + nombreSprint);

    }






}