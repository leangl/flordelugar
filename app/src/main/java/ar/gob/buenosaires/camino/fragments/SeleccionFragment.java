package ar.gob.buenosaires.camino.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ar.gob.buenosaires.camino.R;
import roboguice.inject.InjectView;

/**
 * Created by Ignacio Saslavsky on 14/06/15.
 * correonano@gmail.com
 */
public class SeleccionFragment extends BaseFragment {

    @InjectView(R.id.barrio_spinner)
    private Spinner mBarrios;

    @InjectView(R.id.rubro_spinner)
    private Spinner mRubros;

    @InjectView(R.id.minutos_spinner)
    private Spinner mMinutos;

    @InjectView(R.id.boton)
    private View boton;

    public static SeleccionFragment newInstance() {
        SeleccionFragment fragment = new SeleccionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_seleccion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, getResources().getStringArray(R.array.barrios_array) );
        mBarrios.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, getResources().getStringArray(R.array.categorias_array) );
        mRubros.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text, getResources().getStringArray(R.array.tiempo_array) );
        mMinutos.setAdapter(adapter2);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(RecorridosFragment.newInstance(), true);
            }
        });
    }
}
