package sv.edu.catolica.appscrumtutor;

import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import sv.edu.catolica.appscrumtutor.miembros.ProyectoMiembroInvitacionDTO;
import sv.edu.catolica.appscrumtutor.miembros.ProyectoMiembroService;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.proyecto.Notificacion;
import sv.edu.catolica.appscrumtutor.proyecto.NotificacionService;
import sv.edu.catolica.appscrumtutor.proyecto.Proyecto;
import sv.edu.catolica.appscrumtutor.proyecto.ProyectoService;

public class NotificacionesActivity extends AppCompatActivity {

    private LinearLayout layoutNotificaciones;
    private long idUsuarioActual;

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

        obtenerUsuarioDesdeJWT();
    }

    /** Extrae el ID del usuario actual desde el JWT **/
    private void obtenerUsuarioDesdeJWT() {
        try {
            String token = getSharedPreferences("ScrumTutorPrefs", MODE_PRIVATE).getString("jwt", null);
            if (token == null || token.isEmpty()) {
                Toast.makeText(this, "Sesión expirada, inicia sesión nuevamente", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] partes = token.split("\\.");
            if (partes.length < 2) return;

            String payloadJson = new String(Base64.decode(partes[1], Base64.URL_SAFE), StandardCharsets.UTF_8);
            JSONObject payload = new JSONObject(payloadJson);
            idUsuarioActual = payload.getLong("uid");

            cargarNotificacionesNoLeidas();
        } catch (Exception e) {
            Toast.makeText(this, "Error al procesar token: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /** Carga las notificaciones del usuario actual **/
    private void cargarNotificacionesNoLeidas() {
        NotificacionService service = ApiClient.getClient(this).create(NotificacionService.class);
        service.getByUsuario(idUsuarioActual).enqueue(new Callback<List<Notificacion>>() {
            @Override
            public void onResponse(Call<List<Notificacion>> call, Response<List<Notificacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    layoutNotificaciones.removeAllViews();
                    List<Notificacion> notificaciones = response.body();

                    boolean hayNoLeidas = false;
                    for (Notificacion n : notificaciones) {
                        if (!n.isLeido()) { // solo las no leídas
                            agregarNotificacion(n);
                            hayNoLeidas = true;
                        }
                    }

                    if (!hayNoLeidas) {
                        agregarMensajeVacio("Sin notificaciones nuevas", "No tienes mensajes pendientes");
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
    }

    /** Muestra cada notificación **/
    private void agregarNotificacion(Notificacion n) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_notificacion, layoutNotificaciones, false);

        TextView tvTitulo = view.findViewById(R.id.tvTituloNoti);
        TextView tvMensaje = view.findViewById(R.id.tvMensajeNoti);
        TextView tvFecha = view.findViewById(R.id.tvFechaNoti);

        tvTitulo.setText(n.getIdTipo() == 5 ? "Invitación a proyecto" : "📢 Notificación");
        tvMensaje.setText(n.getMensaje());
        tvFecha.setText("📅 " + n.getFechaCreacion());

        // Si es tipo INVITACIÓN → mostrar modal
        view.setOnClickListener(v -> {
            if (n.getIdTipo() == 5 && n.getIdProyecto() != null) {
                mostrarModalInvitacion(n.getIdProyecto());
            } else {
                Toast.makeText(this, "Notificación normal", Toast.LENGTH_SHORT).show();
            }
        });

        layoutNotificaciones.addView(view);
    }

    /** Muestra mensaje cuando no hay notificaciones **/
    private void agregarMensajeVacio(String titulo, String mensaje) {
        View notiView = LayoutInflater.from(this).inflate(R.layout.item_notificacion, layoutNotificaciones, false);

        TextView tvTitulo = notiView.findViewById(R.id.tvTituloNoti);
        TextView tvMensaje = notiView.findViewById(R.id.tvMensajeNoti);
        TextView tvFecha = notiView.findViewById(R.id.tvFechaNoti);

        tvTitulo.setText(titulo);
        tvMensaje.setText(mensaje);
        tvFecha.setText("");

        layoutNotificaciones.addView(notiView);
    }

    /** Muestra el modal con los datos del proyecto **/
    private void mostrarModalInvitacion(Long idProyecto) {
        ProyectoService proyectoService = ApiClient.getClient(this).create(ProyectoService.class);
        proyectoService.getById(idProyecto).enqueue(new Callback<Proyecto>() {
            @Override
            public void onResponse(Call<Proyecto> call, Response<Proyecto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Proyecto proyecto = response.body();

                    View dialogView = LayoutInflater.from(NotificacionesActivity.this)
                            .inflate(R.layout.dialog_invitacion, null);
                    TextView tvNombre = dialogView.findViewById(R.id.tvNombreProyectoInvitacion);
                    tvNombre.setText(proyecto.getNombre());

                    AlertDialog dialog = new AlertDialog.Builder(NotificacionesActivity.this)
                            .setView(dialogView)
                            .setCancelable(true)
                            .create();

                    Button btnAceptar = dialogView.findViewById(R.id.btnAceptar);
                    Button btnRechazar = dialogView.findViewById(R.id.btnRechazar);

                    btnAceptar.setOnClickListener(v -> {
                        aceptarInvitacion(idProyecto);
                        dialog.dismiss();
                    });

                    btnRechazar.setOnClickListener(v -> {
                        Toast.makeText(NotificacionesActivity.this, "Invitación rechazada", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });

                    dialog.show();
                } else {
                    Toast.makeText(NotificacionesActivity.this, "Proyecto no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Proyecto> call, Throwable t) {
                Toast.makeText(NotificacionesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** PUT para aceptar la invitación **/
    private void aceptarInvitacion(Long idProyecto) {
        sv.edu.catolica.appscrumtutor.miembros.MiembroService miembroService =
                ApiClient.getClient(this).create(sv.edu.catolica.appscrumtutor.miembros.MiembroService.class);

        // 🔹 NUEVO: obtener también pendientes
        miembroService.getMiembrosPendientes(idProyecto).enqueue(new Callback<List<sv.edu.catolica.appscrumtutor.miembros.Miembro>>() {
            @Override
            public void onResponse(Call<List<sv.edu.catolica.appscrumtutor.miembros.Miembro>> call,
                                   Response<List<sv.edu.catolica.appscrumtutor.miembros.Miembro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<sv.edu.catolica.appscrumtutor.miembros.Miembro> miembros = response.body();

                    sv.edu.catolica.appscrumtutor.miembros.Miembro miembroUsuario = null;
                    for (sv.edu.catolica.appscrumtutor.miembros.Miembro m : miembros) {
                        if (m.getIdUsuario() == idUsuarioActual) {
                            miembroUsuario = m;
                            break;
                        }
                    }

                    if (miembroUsuario == null) {
                        Toast.makeText(NotificacionesActivity.this, "No se encontró tu invitación en este proyecto", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long idMiembro = miembroUsuario.getIdMiembro();

                    ProyectoMiembroService service = ApiClient.getClient(NotificacionesActivity.this)
                            .create(ProyectoMiembroService.class);
                    ProyectoMiembroInvitacionDTO dto = new ProyectoMiembroInvitacionDTO(true);

                    service.actualizarInvitacion(idProyecto, idMiembro, dto)
                            .enqueue(new Callback<sv.edu.catolica.appscrumtutor.miembros.Miembro>() {
                                @Override
                                public void onResponse(Call<sv.edu.catolica.appscrumtutor.miembros.Miembro> call,
                                                       Response<sv.edu.catolica.appscrumtutor.miembros.Miembro> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(NotificacionesActivity.this, "Invitación aceptada correctamente", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(NotificacionesActivity.this, "Error al aceptar invitación", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<sv.edu.catolica.appscrumtutor.miembros.Miembro> call, Throwable t) {
                                    Toast.makeText(NotificacionesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(NotificacionesActivity.this, "Error al obtener invitaciones pendientes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<sv.edu.catolica.appscrumtutor.miembros.Miembro>> call, Throwable t) {
                Toast.makeText(NotificacionesActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
