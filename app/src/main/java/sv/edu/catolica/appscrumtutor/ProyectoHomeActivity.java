package sv.edu.catolica.appscrumtutor;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import sv.edu.catolica.appscrumtutor.proyectoDetalle.AdapterProyectoHome;

public class ProyectoHomeActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proyecto_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_projects); // ← marca el botón actual

        HeaderController.setup(this);
        NavController.setup(this);

        tabLayout = findViewById(R.id.tabLayoutProyectoHome);
        viewPager = findViewById(R.id.viewPagerProyectoHome);

        TextView tvTitulo = findViewById(R.id.tvProjectTitle);

        long idProyecto = getIntent().getLongExtra("idProyecto", -1);
        String nombreProyecto = getIntent().getStringExtra("nombreProyecto");

        if (nombreProyecto != null) tvTitulo.setText(nombreProyecto);

        viewPager.setAdapter(new AdapterProyectoHome(this, idProyecto, nombreProyecto));

        new TabLayoutMediator(tabLayout, viewPager, (tab, pos) -> {
            switch (pos) {
                case 0: tab.setText("Backlog"); break;
                case 1: tab.setText("Sprints"); break;
            }
        }).attach();
    }
}