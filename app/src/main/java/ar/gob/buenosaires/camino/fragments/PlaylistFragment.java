package ar.gob.buenosaires.camino.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ar.gob.buenosaires.camino.CaminoApplication;
import ar.gob.buenosaires.camino.R;
import ar.gob.buenosaires.camino.model.Venue;
import roboguice.inject.InjectView;

/**
 * Created by Ignacio Saslavsky on 13/06/15.
 * correonano@gmail.com
 */
public class PlaylistFragment extends BaseFragment {

    @InjectView(R.id.resultados)
    private ListView mList;

    private RecorridoAdapter mAdapter;

    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new RecorridoAdapter(CaminoApplication.getInstance().getVenuesByRecorrido(CaminoApplication.getInstance().getRecorridos().get(0)));
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venue venue = (Venue) mAdapter.getItem(position);
                start(MapaFragment.newInstance(venue));
            }
        });
        Log.d("xx", CaminoApplication.getInstance().getRecorridos().toString());


    }

    private class RecorridoAdapter extends BaseAdapter {

        private List<Venue> mRecorrido;

        private RecorridoAdapter(List<Venue> recorrido) {
            mRecorrido = recorrido;
        }

        @Override
        public int getCount() {
            return mRecorrido.size();
        }

        @Override
        public Object getItem(int position) {
            return mRecorrido.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if (convertView == null) {
                v = LayoutInflater.from(getActivity()).inflate(R.layout.camino_line, parent, false);
                v.setTag(new ViewHolder(v));
            } else {
                v = convertView;
            }
            Venue recorrido = (Venue) getItem(position);

            ViewHolder holder = (ViewHolder) v.getTag();


            holder.title.setText(recorrido.name);
            holder.tiempo.setText(recorrido.description.substring(0, 30) + "...");

            return v;
        }

        private class ViewHolder {

            private TextView title;
            private TextView tiempo;
            private TextView tag1;
            private TextView tag2;
            private TextView tag3;

            private ViewHolder(View v) {
                title = (TextView) v.findViewById(R.id.title_camino);
                tiempo = (TextView) v.findViewById(R.id.tiempo_camino);
                tag1 = (TextView) v.findViewById(R.id.tag1);
                tag2 = (TextView) v.findViewById(R.id.tag2);
                tag3 = (TextView) v.findViewById(R.id.tag3);
            }
        }

    }
}
