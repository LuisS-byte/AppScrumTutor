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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

import sv.edu.catolica.appscrumtutor.CrearTareaActivity;
import sv.edu.catolica.appscrumtutor.DetalleTareaActivity;
import sv.edu.catolica.appscrumtutor.R;

public class ActividadesFragment extends Fragment {


    private LinearLayout layoutPendiente, layoutEnProgreso, layoutHecho;
    private Button btnNuevaTarea;

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

        // Cargar datos mock iniciales
        List<String[]> tareasMock = Arrays.asList(
                new String[]{"Diseñar login", "Diseñar UI en XML", "22/05", "Alta", "José"},
                new String[]{"Conectar backend", "Implementar API REST", "25/05", "Media", "Carlos"},
                new String[]{"Configurar base de datos", "PostgreSQL local", "20/05", "Baja", "Ana"}
        );

        for (String[] t : tareasMock)
            agregarTarea(layoutPendiente, t[0], t[1], t[2], t[3], t[4]);

        // Acción del botón para abrir formulario de nueva tarea
        btnNuevaTarea.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CrearTareaActivity.class);
            startActivity(intent);
        });

        return view;
    }

    /**
     * Crea dinámicamente una tarjeta de tarea dentro del contenedor indicado
     */
    private void agregarTarea(LinearLayout contenedor, String titulo, String descripcion,
                              String fecha, String prioridad, String responsable) {

        View tareaView = LayoutInflater.from(getContext()).inflate(R.layout.item_tarea, contenedor, false);

        TextView tvTitulo = tareaView.findViewById(R.id.tvTitulo);
        TextView tvDescripcion = tareaView.findViewById(R.id.tvDescripcion);
        TextView tvFecha = tareaView.findViewById(R.id.tvFecha);
        TextView tvPrioridad = tareaView.findViewById(R.id.tvPrioridad);
        TextView tvResponsable = tareaView.findViewById(R.id.tvResponsable);
        CheckBox cbCompletado = tareaView.findViewById(R.id.cbCompletado);

        tvTitulo.setText(titulo);
        tvDescripcion.setText(descripcion);
        tvFecha.setText("📅 " + fecha);
        tvPrioridad.setText("🔵 " + prioridad);
        tvResponsable.setText("👤 " + responsable);

        tareaView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DetalleTareaActivity.class);
            startActivity(intent);
        });
        // Efecto visual: marcar completada
        cbCompletado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            float alpha = isChecked ? 0.5f : 1f;
            tvTitulo.setAlpha(alpha);
            tvDescripcion.setAlpha(alpha);

            // (Opcional) mover de Pendiente a Hecho visualmente
            if (isChecked && contenedor != layoutHecho) {
                ((ViewGroup) tareaView.getParent()).removeView(tareaView);
                layoutHecho.addView(tareaView);
            } else if (!isChecked && contenedor == layoutHecho) {
                ((ViewGroup) tareaView.getParent()).removeView(tareaView);
                layoutPendiente.addView(tareaView);
            }
        });

        contenedor.addView(tareaView);
    }




}