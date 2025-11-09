package sv.edu.catolica.appscrumtutor;

import android.os.Bundle;
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

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import sv.edu.catolica.appscrumtutor.fragments.ActividadesFragment;
import sv.edu.catolica.appscrumtutor.fragments.BacklogFrament;
import sv.edu.catolica.appscrumtutor.fragments.CalendarioFrament;

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


         tabLayout = findViewById(R.id.tabLayout);
         viewPager = findViewById(R.id.viewPager);

        // Recuperar datos del Intent
        String nombreProyecto = getIntent().getStringExtra("nombreProyecto");
        String descripcionProyecto = getIntent().getStringExtra("descripcionProyecto");

        // Mostrar el nombre arriba
        TextView tvTitulo = findViewById(R.id.tvProjectTitle);
        if (nombreProyecto != null) {
            tvTitulo.setText(nombreProyecto);
        }

        // Asignar el adaptador que controla los Fragments
        viewPager.setAdapter(new ProjectPagerAdapter(this));

        // Sincronizar las pestañas con los fragments
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Backlog"); break;
                case 1: tab.setText("Actividades"); break;
                case 2: tab.setText("Calendario"); break;
            }
        }).attach();
    }

    private static class ProjectPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        public ProjectPagerAdapter(@NonNull AppCompatActivity activity) {
            super(activity);
            fragments.add(new BacklogFrament());
            fragments.add(new ActividadesFragment());
            fragments.add(new CalendarioFrament());
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }
}