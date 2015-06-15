package ar.gob.buenosaires.camino.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import ar.gob.buenosaires.camino.CaminoApplication;
import ar.gob.buenosaires.camino.R;
import ar.gob.buenosaires.camino.model.Venue;
import ar.gob.buenosaires.camino.utils.SaveState;
import roboguice.inject.InjectView;

/**
 * Created by Ignacio Saslavsky on 13/06/15.
 * correonano@gmail.com
 */
public class MapaFragment extends BaseFragment {

    @InjectView(R.id.place)
    private View place;
    @InjectView(R.id.place_title)
    private TextView placeTitle;
    @InjectView(R.id.time)
    private TextView placeTime;
    @InjectView(R.id.next)
    private View next;
    @InjectView(R.id.check)
    private View check;
    @InjectView(R.id.distance)
    private TextView distance;

    @SaveState
    private Venue mVenue;

    public static MapaFragment newInstance(Venue venue) {
        MapaFragment fragment = new MapaFragment();
        fragment.mVenue = venue;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_mapa, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        place.setBackgroundResource(mVenue.getImageResId());
        placeTitle.setText(mVenue.name);
        if (CaminoApplication.lastLocation != null) {
            placeTime.setText("Aprox. a " + mVenue.timeToCurrentLocation() + " MINUTOS");
        }
        next.setOnClickListener(v -> {
            start(DetalleFragment.newInstance(mVenue));
        });
        check.setOnClickListener(v -> {
            finish();
        });
        distance.setText(mVenue.distanceToCurrentLocation() + "");
    }

}
