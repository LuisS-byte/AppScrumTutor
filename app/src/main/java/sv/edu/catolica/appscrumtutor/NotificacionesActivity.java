package sv.edu.catolica.appscrumtutor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotificacionesActivity extends AppCompatActivity {

    private LinearLayout layoutNotificaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notificaciones);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        layoutNotificaciones = findViewById(R.id.layoutNotificaciones);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_notifications);

        HeaderController.setup(this);
        NavController.setup(this);

        // Datos mock
        agregarNotificacion("Recordatorio de tarea", "La tarea 'Diseñar login' vence mañana", "10/11/2025");
        agregarNotificacion("Nuevo comentario", "Ana comentó en 'Configurar base de datos'", "09/11/2025");
        agregarNotificacion("Sprint finalizado", "El Sprint 2 ha sido completado", "08/11/2025");
    }


    private void agregarNotificacion(String titulo, String mensaje, String fecha) {
        View notiView = LayoutInflater.from(this).inflate(R.layout.item_notificacion, layoutNotificaciones, false);

        TextView tvTitulo = notiView.findViewById(R.id.tvTituloNoti);
        TextView tvMensaje = notiView.findViewById(R.id.tvMensajeNoti);
        TextView tvFecha = notiView.findViewById(R.id.tvFechaNoti);

        tvTitulo.setText(titulo);
        tvMensaje.setText(mensaje);
        tvFecha.setText("📅 " + fecha);

        layoutNotificaciones.addView(notiView);
    }


}