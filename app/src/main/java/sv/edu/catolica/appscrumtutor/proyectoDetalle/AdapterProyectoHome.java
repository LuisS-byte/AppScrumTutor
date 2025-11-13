package sv.edu.catolica.appscrumtutor.proyectoDetalle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import sv.edu.catolica.appscrumtutor.fragments.BacklogFrament;
import sv.edu.catolica.appscrumtutor.fragments.SprintsFragment;

public class AdapterProyectoHome extends FragmentStateAdapter {
    private long idProyecto;
    private String nombreProyecto;

    public AdapterProyectoHome(@NonNull AppCompatActivity activity,
                               long idProyecto,
                               String nombreProyecto) {
        super(activity);
        this.idProyecto = idProyecto;
        this.nombreProyecto = nombreProyecto;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle args = new Bundle();
        args.putLong("idProyecto", idProyecto);
        args.putString("nombreProyecto", nombreProyecto);

        switch (position) {
            case 0:
                BacklogFrament backlog = new BacklogFrament();
                backlog.setArguments(args);
                return backlog;

            case 1:
                SprintsFragment sprints = new SprintsFragment();
                sprints.setArguments(args);
                return sprints;

            default:
                throw new IllegalStateException("Posición inválida: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}