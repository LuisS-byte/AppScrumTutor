package sv.edu.catolica.appscrumtutor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import sv.edu.catolica.appscrumtutor.R;
import retrofit2.Call;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.GestionarSprintActivity;
import sv.edu.catolica.appscrumtutor.MiembrosActivity;
import sv.edu.catolica.appscrumtutor.ProyectoDetalleActivity;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.sprint.Sprint;
import sv.edu.catolica.appscrumtutor.sprint.SprintService;

public class SprintsFragment extends Fragment {

    private LinearLayout layoutSprints;
    private long idProyecto = -1;
    private String nombreProyecto;

    public SprintsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sprints, container, false);

        layoutSprints = view.findViewById(R.id.layoutSprints);
        Button btnCrearSprint = view.findViewById(R.id.btnCrearSprint);
        Button btnVerMiembros = view.findViewById(R.id.btnVerMiembros);
        TextView tvTituloProyecto = view.findViewById(R.id.tvTituloProyecto);

        // Recuperar argumentos
        Bundle args = getArguments();
        if (args != null) {
            idProyecto = args.getLong("idProyecto", -1);
            nombreProyecto = args.getString("nombreProyecto");
        }

        if (idProyecto == -1) {
            Toast.makeText(getContext(), "Error: Proyecto no identificado", Toast.LENGTH_SHORT).show();
            return view;
        }

        if (nombreProyecto != null) {
            tvTituloProyecto.setText("Sprints de " + nombreProyecto);
        }

        // Botón Crear Sprint
        btnCrearSprint.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), GestionarSprintActivity.class);
            intent.putExtra("idProyecto", idProyecto);
            startActivity(intent);
        });

        // Botón Ver Miembros
        btnVerMiembros.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MiembrosActivity.class);
            intent.putExtra("idProyecto", idProyecto);
            startActivity(intent);
        });

        cargarSprintsDesdeBackend(idProyecto);

        return view;
    }

    private void cargarSprintsDesdeBackend(long idProyecto) {
        SprintService service = ApiClient.getClient(requireContext()).create(SprintService.class);

        service.listarPorProyecto(idProyecto).enqueue(new retrofit2.Callback<List<Sprint>>() {
            @Override
            public void onResponse(Call<List<Sprint>> call, Response<List<Sprint>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    layoutSprints.removeAllViews();
                    for (Sprint s : response.body()) {
                        agregarSprint(s);
                    }
                } else {
                    Toast.makeText(getContext(), "No se pudieron cargar los sprints", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Sprint>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarSprint(Sprint sprint) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_sprint, layoutSprints, false);

        TextView tvNombre = view.findViewById(R.id.tvNombreSprint);
        TextView tvEstado = view.findViewById(R.id.tvEstado);
        TextView tvFechas = view.findViewById(R.id.tvFechas);
        Button btnVer = view.findViewById(R.id.btnVerSprint);
        Button btnEditarSprint = view.findViewById(R.id.btnEditarSprint);

        tvNombre.setText(sprint.getNombre());
        tvEstado.setText(sprint.getEstadoDescripcion() != null ? sprint.getEstadoDescripcion() : "Sin estado");
        tvFechas.setText("📅 " + sprint.getFechaInicio() + " → " + sprint.getFechaFin());

        btnVer.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProyectoDetalleActivity.class);
            intent.putExtra("idProyecto", idProyecto);
            intent.putExtra("idSprint", sprint.getIdSprint());
            intent.putExtra("nombreSprint", sprint.getNombre());
            intent.putExtra("fechaInicioSprint", sprint.getFechaInicio());
            intent.putExtra("fechaFinSprint", sprint.getFechaFin());
            intent.putExtra("nombreProyecto", nombreProyecto);
            startActivity(intent);
        });

        btnEditarSprint.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), GestionarSprintActivity.class);
            intent.putExtra("idProyecto", idProyecto);
            intent.putExtra("idSprint", sprint.getIdSprint());
            startActivity(intent);
        });

        layoutSprints.addView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (idProyecto != -1) {
            cargarSprintsDesdeBackend(idProyecto);
        }
    }
}