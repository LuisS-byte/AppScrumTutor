package sv.edu.catolica.appscrumtutor;

import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.proyecto.Notificacion;
import sv.edu.catolica.appscrumtutor.proyecto.NotificacionService;

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

        cargarNotificacionesNoLeidas();
    }

    private void cargarNotificacionesNoLeidas() {
        try {
            String token = getSharedPreferences("ScrumTutorPrefs", MODE_PRIVATE).getString("jwt", null);
            if (token == null || token.isEmpty()) {
                Toast.makeText(this, "Sesión expirada, inicia sesión nuevamente", Toast.LENGTH_SHORT).show();
                return;
            }

            // Decodificar el payload del token para obtener uid
            String[] partes = token.split("\\.");
            if (partes.length < 2) return;
            String payloadJson = new String(Base64.decode(partes[1], Base64.URL_SAFE), StandardCharsets.UTF_8);
            JSONObject payload = new JSONObject(payloadJson);
            long userId = payload.getLong("uid");

            NotificacionService service = ApiClient.getClient(this).create(NotificacionService.class);
            service.getByUsuario(userId).enqueue(new Callback<List<Notificacion>>() {
                @Override
                public void onResponse(Call<List<Notificacion>> call, Response<List<Notificacion>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        layoutNotificaciones.removeAllViews();
                        List<Notificacion> notificaciones = response.body();

                        boolean hayNoLeidas = false;
                        for (Notificacion n : notificaciones) {
                            if (!n.isLeido()) { // solo las no leídas
                                agregarNotificacion("📢 Notificación", n.getMensaje(), n.getFechaCreacion());
                                hayNoLeidas = true;
                            }
                        }

                        if (!hayNoLeidas) {
                            agregarNotificacion("Sin notificaciones nuevas", "No tienes mensajes pendientes", "");
                        }
                    } else {
                        Toast.makeText(NotificacionesActivity.this, "Error al cargar notificaciones", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Notificacion>> call, Throwable t) {
                    Toast.makeText(NotificacionesActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error al procesar token: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
