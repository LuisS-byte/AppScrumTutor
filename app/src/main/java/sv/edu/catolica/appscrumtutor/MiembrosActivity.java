package sv.edu.catolica.appscrumtutor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.miembros.Miembro;
import sv.edu.catolica.appscrumtutor.miembros.MiembroService;
import sv.edu.catolica.appscrumtutor.miembros.ProyectoMiembroCreateDTO;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.proyecto.Notificacion;
import sv.edu.catolica.appscrumtutor.proyecto.NotificacionCreateDTO;
import sv.edu.catolica.appscrumtutor.proyecto.NotificacionService;
import sv.edu.catolica.appscrumtutor.rol.RolProyecto;
import sv.edu.catolica.appscrumtutor.rol.RolProyectoService;
import sv.edu.catolica.appscrumtutor.usuario.Usuario;
import sv.edu.catolica.appscrumtutor.usuario.UsuarioService;

public class MiembrosActivity extends AppCompatActivity {

    private LinearLayout layoutMiembros;
    private Button btnAgregarMiembro;
    private long idProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_miembros);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        layoutMiembros = findViewById(R.id.layoutMiembros);
        btnAgregarMiembro = findViewById(R.id.btnAgregarMiembro);

        idProyecto = getIntent().getLongExtra("idProyecto", -1L);
        if (idProyecto == -1L) {
            Toast.makeText(this, "ID de proyecto no recibido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarMiembros();
        btnAgregarMiembro.setOnClickListener(v -> mostrarDialogoAgregarMiembro());
    }

    /** Mostrar modal para agregar miembro **/
    private void mostrarDialogoAgregarMiembro() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_miembro, null);
        EditText etCorreo = dialogView.findViewById(R.id.etCorreoMiembro);
        Spinner spRol = dialogView.findViewById(R.id.spRolMiembro);
        Button btnConfirmar = dialogView.findViewById(R.id.btnConfirmarInvitacion);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        RolProyectoService rolService = ApiClient.getClient(this).create(RolProyectoService.class);
        rolService.getRolesProyecto().enqueue(new Callback<List<RolProyecto>>() {
            @Override
            public void onResponse(Call<List<RolProyecto>> call, Response<List<RolProyecto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RolProyecto> roles = response.body()
                            .stream()
                            .filter(r -> r.getIdRolProyecto() != 1)
                            .collect(Collectors.toList());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            MiembrosActivity.this,
                            android.R.layout.simple_spinner_item,
                            roles.stream().map(RolProyecto::getDescripcion).toArray(String[]::new)
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spRol.setAdapter(adapter);

                    btnConfirmar.setOnClickListener(v -> {
                        String correo = etCorreo.getText().toString().trim();
                        if (correo.isEmpty()) {
                            etCorreo.setError("Correo obligatorio");
                            return;
                        }

                        int rolSeleccionado = spRol.getSelectedItemPosition();
                        if (rolSeleccionado < 0) {
                            Toast.makeText(MiembrosActivity.this, "Selecciona un rol", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        long idRol = roles.get(rolSeleccionado).getIdRolProyecto();
                        agregarMiembroYNotificar(correo, idRol);
                        dialog.dismiss();
                    });

                    dialog.show();
                } else {
                    Toast.makeText(MiembrosActivity.this, "Error al cargar roles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RolProyecto>> call, Throwable t) {
                Toast.makeText(MiembrosActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Agregar miembro y enviar notificación **/
    private void agregarMiembroYNotificar(String correo, long idRol) {
        UsuarioService usuarioService = ApiClient.getClient(this).create(UsuarioService.class);

        usuarioService.getUsuarioPorCorreo(correo).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Long idUsuario = response.body().getIdUsuario();
                    if (idUsuario == null) {
                        Toast.makeText(MiembrosActivity.this, "El usuario no tiene ID válido", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ProyectoMiembroCreateDTO nuevoMiembro = new ProyectoMiembroCreateDTO();
                    nuevoMiembro.setIdUsuario(idUsuario);
                    nuevoMiembro.setIdRolProyecto(idRol);

                    MiembroService miembroService = ApiClient.getClient(MiembrosActivity.this).create(MiembroService.class);
                    miembroService.agregarMiembro(idProyecto, nuevoMiembro).enqueue(new Callback<Miembro>() {
                        @Override
                        public void onResponse(Call<Miembro> call, Response<Miembro> response) {
                            if (response.isSuccessful()) {
                                crearNotificacionInvitacion(idUsuario);
                            } else {
                                Toast.makeText(MiembrosActivity.this, "Error al agregar miembro", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Miembro> call, Throwable t) {
                            Toast.makeText(MiembrosActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(MiembrosActivity.this, "Correo no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(MiembrosActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Crear notificación de invitación **/
    private void crearNotificacionInvitacion(Long usuarioId) {
        NotificacionService notifService = ApiClient.getClient(this).create(NotificacionService.class);
        NotificacionCreateDTO notif = new NotificacionCreateDTO();
        notif.setIdUsuario(usuarioId);
        notif.setIdTipo(5);
        notif.setIdProyecto(idProyecto);
        notif.setMensaje("Has sido invitado a un nuevo proyecto");

        notifService.crearNotificacion(notif).enqueue(new Callback<Notificacion>() {
            @Override
            public void onResponse(Call<Notificacion> call, Response<Notificacion> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MiembrosActivity.this, "Invitación enviada correctamente", Toast.LENGTH_SHORT).show();
                    cargarMiembros();
                } else {
                    Toast.makeText(MiembrosActivity.this, "Error al enviar invitación", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Notificacion> call, Throwable t) {
                Toast.makeText(MiembrosActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Cargar lista de miembros **/
    private void cargarMiembros() {
        MiembroService service = ApiClient.getClient(this).create(MiembroService.class);
        service.getMiembros(idProyecto).enqueue(new Callback<List<Miembro>>() {
            @Override
            public void onResponse(Call<List<Miembro>> call, Response<List<Miembro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    layoutMiembros.removeAllViews();
                    List<Miembro> miembros = response.body();
                    if (miembros.isEmpty()) mostrarSinMiembros();
                    else for (Miembro m : miembros) agregarMiembro(m);
                } else {
                    Toast.makeText(MiembrosActivity.this, "Error al cargar miembros", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Miembro>> call, Throwable t) {
                Toast.makeText(MiembrosActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarSinMiembros() {
        TextView msg = new TextView(this);
        msg.setText("No hay miembros en este proyecto");
        msg.setTextSize(16);
        msg.setPadding(0, 24, 0, 24);
        msg.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        layoutMiembros.addView(msg);
    }

    /** Agregar miembro al layout con botón eliminar **/
    private void agregarMiembro(Miembro miembro) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_miembro, layoutMiembros, false);
        TextView tvNombre = view.findViewById(R.id.tvNombreMiembro);
        TextView tvRol = view.findViewById(R.id.tvRolMiembro);
        TextView tvEstado = view.findViewById(R.id.tvEstadoMiembro);
        Button btnEliminar = view.findViewById(R.id.btnEliminarMiembro);

        tvNombre.setText(miembro.getUsuarioNombre());
        tvRol.setText("Rol: " + miembro.getRolDescripcion());
        tvEstado.setText("Activo");
        tvEstado.setTextColor(0xFF4CAF50);

        btnEliminar.setOnClickListener(v -> eliminarMiembro(miembro.getIdMiembro()));

        layoutMiembros.addView(view);
    }

    /** Lógica para eliminar un miembro **/
    private void eliminarMiembro(Long idMiembro) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Deseas eliminar este miembro del proyecto?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    MiembroService service = ApiClient.getClient(this).create(MiembroService.class);
                    service.eliminarMiembro(idProyecto, idMiembro).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(MiembrosActivity.this, "Miembro eliminado correctamente", Toast.LENGTH_SHORT).show();
                                cargarMiembros();
                            } else {
                                Toast.makeText(MiembrosActivity.this, "Error al eliminar miembro", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(MiembrosActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
