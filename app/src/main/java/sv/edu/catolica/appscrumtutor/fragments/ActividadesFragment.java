package sv.edu.catolica.appscrumtutor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.CrearTareaActivity;
import sv.edu.catolica.appscrumtutor.DetalleTareaActivity;
import sv.edu.catolica.appscrumtutor.R;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.sprint.Tarea;
import sv.edu.catolica.appscrumtutor.sprint.TareaService;

public class ActividadesFragment extends Fragment {


    private LinearLayout layoutPendiente, layoutEnProgreso, layoutHecho;
    private Button btnNuevaTarea;

    private long idProyecto;
    private long idSprint;

    public ActividadesFragment() { /* constructor vacío */ }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actividades, container, false);

        // Inicializar vistas

        layoutPendiente = view.findViewById(R.id.layoutPendiente);
        layoutEnProgreso = view.findViewById(R.id.layoutEnProgreso);
        layoutHecho = view.findViewById(R.id.layoutHecho);
        btnNuevaTarea = view.findViewById(R.id.btnNuevaTarea);

        if (getArguments() != null) {
            idProyecto = getArguments().getLong("idProyecto", -1);
            idSprint = getArguments().getLong("idSprint", -1);
        }

        cargarTareasDesdeBackend();

        btnNuevaTarea.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), CrearTareaActivity.class);
            i.putExtra("idSprint", idSprint);
            i.putExtra("idProyecto", idProyecto);
            startActivity(i);
        });

        return view;
    }

    private void cargarTareasDesdeBackend() {
        TareaService service = ApiClient.getClient(getContext()).create(TareaService.class);

        service.listarPorSprint(idSprint).enqueue(new Callback<List<Tarea>>() {
            @Override
            public void onResponse(Call<List<Tarea>> call, Response<List<Tarea>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {

                    layoutPendiente.removeAllViews();
                    layoutEnProgreso.removeAllViews();
                    layoutHecho.removeAllViews();

                    for (Tarea t : response.body()) {
                        switch (t.getIdEstado()) {
                            case 1: agregarTarea(layoutPendiente, t); break;
                            case 2: agregarTarea(layoutEnProgreso, t); break;
                            case 3: agregarTarea(layoutHecho, t); break;
                            default: agregarTarea(layoutPendiente, t);
                        }
                    }

                } else {
                    Toast.makeText(getContext(), "Error al cargar tareas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tarea>> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarTarea(LinearLayout contenedor, Tarea tarea) {

        View tareaView = LayoutInflater.from(getContext()).inflate(R.layout.item_tarea, contenedor, false);

        TextView tvTitulo = tareaView.findViewById(R.id.tvTitulo);
        TextView tvDescripcion = tareaView.findViewById(R.id.tvDescripcion);
        TextView tvFecha = tareaView.findViewById(R.id.tvFecha);
        TextView tvPrioridad = tareaView.findViewById(R.id.tvPrioridad);
        TextView tvResponsable = tareaView.findViewById(R.id.tvResponsable);
        CheckBox cbCompletado = tareaView.findViewById(R.id.cbCompletado);

        tvTitulo.setText(tarea.getTitulo());
        tvDescripcion.setText(tarea.getDescripcion());
        tvFecha.setText("📅 " + (tarea.getFechaLimite() != null ? tarea.getFechaLimite() : "N/A"));
        tvPrioridad.setText("🔵 Normal"); // si agregas prioridad la cambiamos
        tvResponsable.setText("👤 " + (tarea.getAsignadoANombre() != null ? tarea.getAsignadoANombre() : "Sin asignar"));

        tareaView.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), DetalleTareaActivity.class);
            i.putExtra("idTarea", tarea.getId());
            startActivity(i);
        });

        contenedor.addView(tareaView);
    }

}