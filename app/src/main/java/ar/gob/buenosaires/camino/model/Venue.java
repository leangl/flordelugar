package ar.gob.buenosaires.camino.model;

import android.content.res.Resources;

import java.io.Serializable;
import java.util.List;

import ar.gob.buenosaires.camino.CaminoApplication;

/**
 * Created by Ignacio Saslavsky on 13/06/15.
 * correonano@gmail.com
 */
public class Venue implements Serializable {

    public long id;
    public String name;
    public Location location;
    public Assets assets;
    public String description;
    public List<Tag> tags;

    public android.location.Location getAndroidLocation() {
        android.location.Location l = new android.location.Location("");
        l.setLatitude(location.lat);
        l.setLongitude(location.lon);
        return l;
    }

    public int getImageResId() {
        Resources resources = CaminoApplication.getInstance().getResources();
        return resources.getIdentifier(assets.image, "drawable", CaminoApplication.getInstance().getPackageName());
    }

    public int getAudioResId() {
        Resources resources = CaminoApplication.getInstance().getResources();
        return resources.getIdentifier(assets.audio, "raw", CaminoApplication.getInstance().getPackageName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return id == venue.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public static final float WALK_SPEED = 1.38f;

    public int timeToCurrentLocation() {
        int seconds = (int) (distanceToCurrentLocation() / WALK_SPEED);
        return seconds / 60;
    }

    public int distanceToCurrentLocation() {
        if (getAndroidLocation() == null) return 0;
        return (int) getAndroidLocation().distanceTo(CaminoApplication.lastLocation);
    }
}
