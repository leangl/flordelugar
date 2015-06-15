package ar.gob.buenosaires.camino.activities;

import android.os.Bundle;

import ar.gob.buenosaires.camino.R;
import ar.gob.buenosaires.camino.fragments.FinalFragment;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_main)
public class FinalActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start(FinalFragment.newInstance(), false);
    }


}
