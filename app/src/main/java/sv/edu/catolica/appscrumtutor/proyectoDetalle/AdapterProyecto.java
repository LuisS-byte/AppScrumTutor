package sv.edu.catolica.appscrumtutor.proyectoDetalle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

import sv.edu.catolica.appscrumtutor.fragments.ActividadesFragment;
import sv.edu.catolica.appscrumtutor.fragments.BacklogFrament;
import sv.edu.catolica.appscrumtutor.fragments.CalendarioFrament;
public class AdapterProyecto  extends FragmentStateAdapter {

    private final long idProyecto;
    private final long idSprint;
    private final List<Fragment> fragments = new ArrayList<>();

    public AdapterProyecto(@NonNull AppCompatActivity activity, long idProyecto, long idSprint) {
        super(activity);
        this.idProyecto = idProyecto;
        this.idSprint = idSprint;

        Bundle args = new Bundle();
        args.putLong("idProyecto", idProyecto);
        args.putLong("idSprint", idSprint);

        BacklogFrament backlog = new BacklogFrament();
        backlog.setArguments(args);

        ActividadesFragment actividades = new ActividadesFragment();
        actividades.setArguments(args);

        CalendarioFrament calendario = new CalendarioFrament();
        calendario.setArguments(args);

        fragments.add(backlog);
        fragments.add(actividades);
        fragments.add(calendario);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}