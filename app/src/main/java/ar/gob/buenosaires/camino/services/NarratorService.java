package ar.gob.buenosaires.camino.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.squareup.otto.Subscribe;

import ar.gob.buenosaires.camino.CaminoApplication;
import ar.gob.buenosaires.camino.R;
import ar.gob.buenosaires.camino.model.Venue;
import ar.gob.buenosaires.camino.utils.LocationClient;
import ar.gob.buenosaires.camino.utils.LocationEnabled;
import ar.gob.buenosaires.camino.utils.NarratorNotification;
import rx.Subscription;

/**
 * Created by Leandro on 13/6/2015.
 */
public class NarratorService extends Service implements MediaPlayer.OnCompletionListener {

    public static final String ACTION_TOGGLE = "toggle";
    private static final int MIN_DISTANCE = 200;

    private Subscription mLocationSubscription;
    private Venue mLastVenue;
    private boolean mNarrating;

    private Location mLastLocation;
    private MediaPlayer mp;

    @Override
    public void onCreate() {
        super.onCreate();
        CaminoApplication.getBus().register(this);
        requestLocationUpdates();
    }
    private void requestLocationUpdates() {
        mLocationSubscription = LocationClient.requestLocation().subscribe(location -> {
            mLastLocation = location;
            CaminoApplication.lastLocation = location;

            Venue nextVenue = null;

            for (Venue venue : CaminoApplication.getInstance().getCurrentVenues()) {
                if (location.distanceTo(venue.getAndroidLocation()) < MIN_DISTANCE) {
                    if (!mNarrating && (isNext(venue))) {
                        nextVenue = venue;
                        break;
                    }
                }
            }
            if (nextVenue != null) {
                startNarration(nextVenue);
            } else {
                if (!mNarrating) {
                    notifyNextVenue();
                }
            }

        }, error -> {
            // TODO
        });
    }

    private void notifyNextVenue() {
        float nextVenueDistance = Float.MAX_VALUE;
        Venue nextVenue = null;
        for (Venue venue : CaminoApplication.getInstance().getCurrentVenues()) {
            if (isNext(venue)) {
                float distance = mLastLocation.distanceTo(venue.getAndroidLocation());
                if (distance < nextVenueDistance) {
                    nextVenue = venue;
                    nextVenueDistance = distance;
                }
            }
        }

        if (nextVenue != null) {
            NarratorNotification.waiting(this, nextVenue);
        } else {
            NarratorNotification.cancel(this);
        }
    }
    private boolean isNext(Venue venue) {
        return mLastVenue == null || venue.id > mLastVenue.id;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CaminoApplication.getBus().unregister(this);
        if (mLocationSubscription != null) {
            mLocationSubscription.unsubscribe();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_TOGGLE.equals(action)) {
                if (mNarrating) {
                    pauseCurrentNarration();
                } else {
                    startNarration(mLastVenue);
                }
            }
        }

        return START_STICKY;
    }

    private void startNarration(Venue venue) {
        if (mp == null) {
            mp = MediaPlayer.create(this, venue.getAudioResId());
            mp.setOnCompletionListener(this);
        }
        mp.start();
        mNarrating = true;

        mLastVenue = venue;
        NarratorNotification.notify(this, new NarratorNotification.Info(mLastVenue, R.drawable.ic_av_pause));
    }

    private void pauseCurrentNarration() {
        if (mp != null) {
            mp.pause();
        }
        mNarrating = false;

        NarratorNotification.notify(this, new NarratorNotification.Info(mLastVenue, R.drawable.ic_av_play_arrow));
    }

    @Subscribe
    public void onLocationEnabled(LocationEnabled le) {
        requestLocationUpdates();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mNarrating = false;

        notifyNextVenue();
    }

}
