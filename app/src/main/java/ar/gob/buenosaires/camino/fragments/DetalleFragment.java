package ar.gob.buenosaires.camino.fragments;

import android.media.MediaPlayer;
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
public class DetalleFragment extends BaseFragment implements MediaPlayer.OnCompletionListener {

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
    @InjectView(R.id.play)
    private View play;
    @InjectView(R.id.play_time)
    private TextView playTime;
    @InjectView(R.id.play_description)
    private TextView playDescription;
    @InjectView(R.id.play_progress)
    private SeekBar playProgress;
    @InjectView(R.id.description)
    private TextView description;

    @SaveState
    private Venue mVenue;
    private MediaPlayer mp;

    public static DetalleFragment newInstance(Venue venue) {
        DetalleFragment fragment = new DetalleFragment();
        fragment.mVenue = venue;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_detalle, container, false);
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
            Toast.makeText(getActivity(), "Next", Toast.LENGTH_SHORT).show();
        });
        next.setVisibility(View.INVISIBLE);
        check.setOnClickListener(v -> {
            finish();
        });
        play.setOnClickListener(v -> {
            if (mp == null) {
                mp = MediaPlayer.create(getActivity(), mVenue.getAudioResId());
                if (mp != null) {
                    mp.setOnCompletionListener(this);
                    mp.start();
                }
            } else {
                if (mp.isPlaying()) {
                    mp.pause();
                } else {
                    mp.start();
                }
            }
        });
        playTime.setText("00:00");
        playDescription.setText(mVenue.name + " producido por \"Movilidad Sustentable\"");
        playProgress.setProgress(10);
        playProgress.setEnabled(false);
        description.setText(mVenue.description);
    }

    @Override
    public void onCompletion(MediaPlayer m) {
        mp = null;
    }
}
