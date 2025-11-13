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

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.CrearHistoriasActivity;
import sv.edu.catolica.appscrumtutor.DetalleHistoriasActivity;
import sv.edu.catolica.appscrumtutor.R;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.sprint.Historia;
import sv.edu.catolica.appscrumtutor.sprint.HistoriaService;

public class BacklogFrament extends Fragment {


    private LinearLayout layoutNoAsignadas;
    private LinearLayout layoutAsignadas;

    private Button btnCrearHistoria;
    private long idProyecto = -1;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_backlog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        layoutNoAsignadas = view.findViewById(R.id.layoutNoAsignadas);
        layoutAsignadas = view.findViewById(R.id.layoutAsignadas);

        btnCrearHistoria = view.findViewById(R.id.btnCrearHistoria);

        if (getArguments() != null) {
            idProyecto = getArguments().getLong("idProyecto", -1);
        }

        if (idProyecto != -1) {
            cargarHistoriasDesdeBackend();  // ya es seguro aquí
        } else {
            Toast.makeText(requireContext(), "Error: Proyecto no identificado", Toast.LENGTH_SHORT).show();
        }

        btnCrearHistoria.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), CrearHistoriasActivity.class);
            intent.putExtra("modo", "crear");
            intent.putExtra("idProyecto", idProyecto);
            startActivity(intent);
        });
    }

    private void cargarHistoriasDesdeBackend() {
        HistoriaService service = ApiClient.getClient(getContext()).create(HistoriaService.class);

        service.listarPorProyecto(idProyecto).enqueue(new Callback<List<Historia>>() {
            @Override
            public void onResponse(Call<List<Historia>> call, Response<List<Historia>> response) {
                if (!isAdded()) return;

                layoutNoAsignadas.removeAllViews();
                layoutAsignadas.removeAllViews();

                if (response.isSuccessful() && response.body() != null) {
                    for (Historia h : response.body()) {
                        if (h.getIdSprint() == null) {
                            agregarHistoriaNoAsignada(h);
                        } else {
                            agregarHistoriaAsignada(h);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Historia>> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarHistoriaNoAsignada(Historia historia) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.item_historia, layoutNoAsignadas, false);

        TextView tvTitulo = view.findViewById(R.id.tvTituloHistoria);
        TextView tvDescripcion = view.findViewById(R.id.tvDescripcionHistoria);
        TextView tvPrioridad = view.findViewById(R.id.tvPrioridadHistoria);

        tvTitulo.setText(historia.getTitulo());
        tvDescripcion.setText(historia.getDescripcion());
        tvPrioridad.setText("Prioridad: " + historia.getPrioridad());

        view.setOnClickListener(v -> abrirDetalle(historia));

        layoutNoAsignadas.addView(view);
    }

    private void agregarHistoriaAsignada(Historia historia) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.item_historia, layoutAsignadas, false);

        TextView tvTitulo = view.findViewById(R.id.tvTituloHistoria);
        TextView tvDescripcion = view.findViewById(R.id.tvDescripcionHistoria);
        TextView tvPrioridad = view.findViewById(R.id.tvPrioridadHistoria);

        tvTitulo.setText(historia.getTitulo());
        tvDescripcion.setText(historia.getDescripcion());
        tvPrioridad.setText("Sprint #" + historia.getIdSprint());

        view.setOnClickListener(v -> abrirDetalle(historia));

        layoutAsignadas.addView(view);
    }

    private void abrirDetalle(Historia historia) {
        Intent intent = new Intent(getActivity(), DetalleHistoriasActivity.class);
        intent.putExtra("idHistoria", historia.getId());
        intent.putExtra("idProyecto", idProyecto);
        intent.putExtra("idSprint", historia.getIdSprint());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (idProyecto != -1) {
            cargarHistoriasDesdeBackend();
        }
    }
}