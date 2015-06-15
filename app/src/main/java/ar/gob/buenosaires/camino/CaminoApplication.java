package ar.gob.buenosaires.camino;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ar.gob.buenosaires.camino.model.Location;
import ar.gob.buenosaires.camino.model.Recorrido;
import ar.gob.buenosaires.camino.model.Venue;

import com.squareup.otto.Bus;

/**
 * Created by Leandro on 13/6/2015.
 */
public class CaminoApplication extends Application {

    private static CaminoApplication sInstance;
    private List<Recorrido> recorridos;
    private List<Venue> venues;
    private static Bus sBus;

    public static android.location.Location lastLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        recorridos = loadRecorridos();
        venues = loadVenues();

        sBus = new Bus();
    }

    public static CaminoApplication getInstance() {
        return sInstance;
    }


    public List<Recorrido> loadRecorridos() {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(CaminoApplication.getInstance().getAssets().open("recorridos.json")));
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
        return new Gson().fromJson(reader, new TypeToken<List<Recorrido>>() {
        }.getType());
    }

    public List<Venue> loadVenues() {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(CaminoApplication.getInstance().getAssets().open("venues.json")));
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
        return new Gson().fromJson(reader, new TypeToken<List<Venue>>() {
        }.getType());
    }

    public List<Recorrido> getRecorridos() {
        return recorridos;
    }

    public void setRecorridos(List<Recorrido> recorridos) {
        this.recorridos = recorridos;
    }

    public List<Venue> getVenuesByRecorrido(Recorrido recorrido) {
        List<Venue> res = new ArrayList<>();

        for(int id : recorrido.venues) {
            res.add(venues.get(id-1));
        }

        return res;
    }

    public static Bus getBus() {
        return sBus;
    }

    public Recorrido getCurrentRecorrido() {
        return getRecorridos().get(0);
    }

    public List<Venue> getCurrentVenues() {
        return getVenuesByRecorrido(getCurrentRecorrido());
    }
}
