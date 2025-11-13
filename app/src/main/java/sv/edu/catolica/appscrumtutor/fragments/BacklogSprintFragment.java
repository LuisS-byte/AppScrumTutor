package sv.edu.catolica.appscrumtutor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.CrearTareaActivity;
import sv.edu.catolica.appscrumtutor.R;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.sprint.Historia;
import sv.edu.catolica.appscrumtutor.sprint.HistoriaService;
import sv.edu.catolica.appscrumtutor.sprint.Tarea;
import sv.edu.catolica.appscrumtutor.sprint.TareaService;

public class BacklogSprintFragment extends Fragment {
    private LinearLayout layoutBacklog;
    private long idSprint = -1;

    public BacklogSprintFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sprint_backlog, container, false);

        layoutBacklog = view.findViewById(R.id.layoutSprintBacklog);

        // Obtener idSprint desde argumentos
        if (getArguments() != null) {
            idSprint = getArguments().getLong("idSprint", -1);
        }

        if (idSprint != -1) {
            cargarHistorias();
        }

        return view;
    }

    private void cargarHistorias() {

        HistoriaService api = ApiClient.getClient(requireContext()).create(HistoriaService.class);

        api.listarPorSprint(idSprint).enqueue(new Callback<List<Historia>>() {
            @Override
            public void onResponse(Call<List<Historia>> call, Response<List<Historia>> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                layoutBacklog.removeAllViews();

                for (Historia h : response.body()) {
                    agregarHistoria(h);
                }
            }

            @Override
            public void onFailure(Call<List<Historia>> call, Throwable t) {}
        });
    }

    private void agregarHistoria(Historia h) {

        View card = LayoutInflater.from(getContext())
                .inflate(R.layout.item_historia, layoutBacklog, false);

        TextView tvTitulo = card.findViewById(R.id.tvTituloHistoria);
        TextView tvDescripcion = card.findViewById(R.id.tvDescripcionHistoria);
        TextView tvPrioridad = card.findViewById(R.id.tvPrioridadHistoria);

        tvTitulo.setText(h.getTitulo());
        tvDescripcion.setText(h.getDescripcion());
        tvPrioridad.setText("Prioridad: " + h.getPrioridad());

        layoutBacklog.addView(card);
    }
}