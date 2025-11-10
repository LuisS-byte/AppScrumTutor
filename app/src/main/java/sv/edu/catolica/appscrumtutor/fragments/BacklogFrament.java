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

import java.util.Arrays;
import java.util.List;

import sv.edu.catolica.appscrumtutor.CrearHistoriasActivity;
import sv.edu.catolica.appscrumtutor.DetalleHistoriasActivity;
import sv.edu.catolica.appscrumtutor.R;

public class BacklogFrament extends Fragment {

    private LinearLayout layoutSprintTareas, layoutBacklogTareas;
    private Button btnCrearHistoria;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backlog, container, false);

        // Referencias
        layoutSprintTareas = view.findViewById(R.id.layoutSprintTareas);
        layoutBacklogTareas = view.findViewById(R.id.layoutBacklogTareas);
        btnCrearHistoria = view.findViewById(R.id.btnCrearHistoria);

        // Historias simuladas (mock)
        List<String[]> historiasMock = Arrays.asList(
                new String[]{"Como cliente quiero iniciar sesión", "Para acceder a mi cuenta", "Alta"},
                new String[]{"Como administrador quiero crear usuarios", "Para gestionar el sistema", "Media"},
                new String[]{"Como cliente quiero recuperar mi contraseña", "Para volver a acceder", "Baja"}
        );

        // Agregar al backlog visualmente
        for (String[] h : historiasMock) {
            agregarHistoria(layoutBacklogTareas, h[0], h[1], h[2]);
        }

        // Botón para crear nueva historia
        btnCrearHistoria.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CrearHistoriasActivity.class);
            intent.putExtra("modo", "crear");
            startActivity(intent);
        });

        return view;
    }

    // Método para agregar una historia en pantalla
    private void agregarHistoria(LinearLayout contenedor, String titulo, String descripcion, String prioridad) {
        View historiaView = LayoutInflater.from(getContext()).inflate(R.layout.item_historia, contenedor, false);

        TextView tvTitulo = historiaView.findViewById(R.id.tvTituloHistoria);
        TextView tvDescripcion = historiaView.findViewById(R.id.tvDescripcionHistoria);
        TextView tvPrioridad = historiaView.findViewById(R.id.tvPrioridadHistoria);

        tvTitulo.setText(titulo);
        tvDescripcion.setText(descripcion);
        tvPrioridad.setText("Prioridad: " + prioridad);

        // Navegar al detalle de la historia
        historiaView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DetalleHistoriasActivity.class);
            intent.putExtra("titulo", titulo);
            intent.putExtra("descripcion", descripcion);
            intent.putExtra("prioridad", prioridad);
            startActivity(intent);
        });

        contenedor.addView(historiaView);
    }
}
