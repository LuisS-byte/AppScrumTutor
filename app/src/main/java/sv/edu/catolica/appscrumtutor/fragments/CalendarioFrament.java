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
import java.util.List;
import java.util.Locale;

import sv.edu.catolica.appscrumtutor.R;
public class CalendarioFrament extends Fragment {

    private TextView tvEventos;
    private CalendarView calendarView;
    private List<String> tareasConFecha;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflamos la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        // Referencias a los elementos del XML
        tvEventos = view.findViewById(R.id.tvEventos);
        calendarView = view.findViewById(R.id.calendarView);

        // Simulación de datos con fechas (luego se reemplaza por datos reales)
        tareasConFecha = new ArrayList<>();
        tareasConFecha.add("2025-11-10 - Diseñar interfaz de login");
        tareasConFecha.add("2025-11-12 - Revisar backlog");
        tareasConFecha.add("2025-11-15 - Entregar sprint");

        // Evento al seleccionar una fecha
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String fechaSeleccionada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            StringBuilder eventos = new StringBuilder("📅 " + fechaSeleccionada + "\n\n");
            for (String t : tareasConFecha) {
                if (t.startsWith(fechaSeleccionada)) {
                    eventos.append("• ").append(t.substring(11)).append("\n");
                }
            }

            if (eventos.toString().equals("📅 " + fechaSeleccionada + "\n\n")) {
                eventos.append("Sin tareas programadas.");
            }

            tvEventos.setText(eventos.toString());
        });

        return view;
    }
}
