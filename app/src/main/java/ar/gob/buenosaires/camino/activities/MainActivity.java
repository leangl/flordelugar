package ar.gob.buenosaires.camino.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.widget.Toast;

import ar.gob.buenosaires.camino.CaminoApplication;
import ar.gob.buenosaires.camino.R;
import ar.gob.buenosaires.camino.fragments.DetalleFragment;
import ar.gob.buenosaires.camino.fragments.MapaFragment;
import ar.gob.buenosaires.camino.fragments.SeleccionFragment;
import ar.gob.buenosaires.camino.model.Venue;
import ar.gob.buenosaires.camino.services.NarratorService;
import ar.gob.buenosaires.camino.utils.LocationClient;
import ar.gob.buenosaires.camino.utils.LocationEnabled;
import roboguice.inject.ContentView;
import rx.Subscription;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private static final int RESOLUTION_REQUEST_CODE = 6732;
    private Subscription mSubscription;

    public static final String INTENT_SHOW_VENUE_EXTRA = "show_venue";
    public static final String INTENT_SHOW_VENUE_MAP_EXTRA = "show_venue_map";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_SHOW_VENUE_EXTRA)) {
            Venue v = (Venue) intent.getSerializableExtra(INTENT_SHOW_VENUE_EXTRA);
            start(DetalleFragment.newInstance(v), false);
        } else if (intent.hasExtra(INTENT_SHOW_VENUE_MAP_EXTRA)) {
            Venue v = (Venue) intent.getSerializableExtra(INTENT_SHOW_VENUE_MAP_EXTRA);
            start(MapaFragment.newInstance(v), false);
        } else {
            start(SeleccionFragment.newInstance(), false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSubscription = LocationClient.requestLocation().subscribe(l -> {
            // nothing
        }, throwable -> {
            LocationClient.LocationException e = (LocationClient.LocationException) throwable;
            try {
                e.startResolutionForResult(this, RESOLUTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e1) {
                Toast.makeText(this, "Error desconocido al obtener su posición.", Toast.LENGTH_SHORT).show();
            }
        });

        startService(new Intent(this, NarratorService.class));
    }
    @Override
    protected void onStop() {
        mSubscription.unsubscribe();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLUTION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Conectado al GPS", Toast.LENGTH_SHORT).show();
                CaminoApplication.getBus().post(new LocationEnabled());
            } else {
                Toast.makeText(this, "Por favor habilite el GPS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(INTENT_SHOW_VENUE_EXTRA)) {
            Venue v = (Venue) intent.getSerializableExtra(INTENT_SHOW_VENUE_EXTRA);
            start(DetalleFragment.newInstance(v), false);
        } else if (intent.hasExtra(INTENT_SHOW_VENUE_MAP_EXTRA)) {
            Venue v = (Venue) intent.getSerializableExtra(INTENT_SHOW_VENUE_MAP_EXTRA);
            start(MapaFragment.newInstance(v), false);
        }
    }
}
