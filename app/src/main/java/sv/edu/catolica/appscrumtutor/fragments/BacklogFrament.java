package sv.edu.catolica.appscrumtutor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import sv.edu.catolica.appscrumtutor.CrearHistoriasActivity;
import sv.edu.catolica.appscrumtutor.R;

public class BacklogFrament extends Fragment {


    Button btnCrearHistoria;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backlog, container, false);

        btnCrearHistoria = view.findViewById(R.id.btnCrearHistoria);

        btnCrearHistoria.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CrearHistoriasActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
