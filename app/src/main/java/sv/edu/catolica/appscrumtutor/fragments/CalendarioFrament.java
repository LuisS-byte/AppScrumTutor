package sv.edu.catolica.appscrumtutor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import sv.edu.catolica.appscrumtutor.R;
import sv.edu.catolica.appscrumtutor.network.ApiClient;
import sv.edu.catolica.appscrumtutor.sprint.Tarea;
import sv.edu.catolica.appscrumtutor.sprint.TareaService;

public class CalendarioFrament extends Fragment {
    private CalendarView calendarView;
    private TextView tvEventos;

    private long idSprint;

    private String fechaInicioSprint;
    private String fechaFinSprint;
    private Map<String, List<Tarea>> mapaTareasPorFecha = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        tvEventos = view.findViewById(R.id.tvEventos);

        // Obtener argumentos
        idSprint = getArguments().getLong("idSprint", -1);
        fechaInicioSprint = getArguments().getString("fechaInicioSprint", "");
        fechaFinSprint = getArguments().getString("fechaFinSprint", "");

        cargarTareasDesdeBackend();

        // Cuando cambia la fecha seleccionada
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {

            String fecha = String.format(Locale.getDefault(),
                    "%04d-%02d-%02d", year, month + 1, dayOfMonth);

            mostrarEventos(fecha);
        });

        // Mostrar rango del sprint al inicio
        tvEventos.setText("📌 Sprint del " + fechaInicioSprint + " al " + fechaFinSprint);

        return view;
    }

    private void cargarTareasDesdeBackend() {
        TareaService service = ApiClient.getClient(requireContext()).create(TareaService.class);

        service.listarPorSprint(idSprint).enqueue(new Callback<List<Tarea>>() {
            @Override
            public void onResponse(Call<List<Tarea>> call, Response<List<Tarea>> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                mapaTareasPorFecha.clear();

                for (Tarea t : response.body()) {
                    String fecha = t.getFechaLimite();
                    if (fecha == null || fecha.isEmpty()) continue;

                    mapaTareasPorFecha
                            .computeIfAbsent(fecha, k -> new ArrayList<>())
                            .add(t);
                }
            }

            @Override
            public void onFailure(Call<List<Tarea>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void mostrarEventos(String fecha) {

        // Mostrar advertencia si la fecha está fuera del sprint
        if (!estaDentroDelSprint(fecha)) {
            tvEventos.setText("📅 " + fecha + "\n\n⚠️ Fuera del rango del sprint.");
            return;
        }

        List<Tarea> tareas = mapaTareasPorFecha.get(fecha);

        if (tareas == null || tareas.isEmpty()) {
            tvEventos.setText("📅 " + fecha + "\n\nSin tareas programadas.");
            return;
        }

        StringBuilder sb = new StringBuilder("📅 " + fecha + "\n\n");

        for (Tarea t : tareas)
            sb.append("• ").append(t.getTitulo()).append("\n");

        tvEventos.setText(sb.toString());
    }

    private boolean estaDentroDelSprint(String fecha) {
        return fecha.compareTo(fechaInicioSprint) >= 0 &&
                fecha.compareTo(fechaFinSprint) <= 0;
    }
}
