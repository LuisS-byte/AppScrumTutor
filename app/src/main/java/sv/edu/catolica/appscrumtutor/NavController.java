package sv.edu.catolica.appscrumtutor;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavController {

    public static void setup(Activity activity) {
        BottomNavigationView bottomNav = activity.findViewById(R.id.bottomNavigation);
        if (bottomNav == null) return;

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_projects) {
                intent = new Intent(activity, ProyectosActivity.class);
            } else if (id == R.id.nav_notifications) {
                intent = new Intent(activity, NotificacionesActivity.class);
            } else if (id == R.id.nav_profile) {
                intent = new Intent(activity, PerfilActivity.class);
            } else if (id == R.id.nav_learning) {
                intent = new Intent(activity, LearningActivity.class);
            }

            if (intent != null && !(activity.getClass().equals(intent.getComponent().getClassName()))) {
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
                activity.finish();
            }

            return true;
        });
    }





}
