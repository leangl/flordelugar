package ar.gob.buenosaires.camino.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ar.gob.buenosaires.camino.activities.BaseActivity;
import ar.gob.buenosaires.camino.utils.SaveState;
import roboguice.fragment.RoboFragment;

/**
 * Created by Leandro on 23/03/2015.
 */
public class BaseFragment extends RoboFragment {

    /**
     * Automatically restores Fragment state annotated with {@link SaveState} annotation.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Camino", "Restoring fragment state");
        if (savedInstanceState != null) {
            try {
                for (Field field : getFields(getClass())) {
                    if (field.isAnnotationPresent(SaveState.class)) {
                        Log.d("Camino", "Restoring fragment state for field: " + field.getName());
                        field.setAccessible(true);
                        Serializable value = savedInstanceState.getSerializable(field.getName());
                        field.set(this, value);
                    }
                }
            } catch (Exception e) {
                Log.e("Camino", "Error restoring fragment state.", e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Automatically saves Fragment state annotated with {@link SaveState} annotation.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d("Camino", "Saving fragment state");
        try {
            for (Field field : getFields(getClass())) {
                if (field.isAnnotationPresent(SaveState.class)) {

                    field.setAccessible(true);
                    if (field.get(this) != null && !Serializable.class.isAssignableFrom(field.get(this).getClass())) {
                        throw new Exception("Field " + field.getName() + " annotated with SaveState but not serializable.");
                    }

                    outState.putSerializable(field.getName(), (Serializable) field.get(this));
                }
            }
        } catch (Exception e) {
            Log.e("Camino", "Error saving fragment state", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Return all public or private fields in the given class.
     *
     * @param startClass
     * @return
     */
    private static Iterable<Field> getFields(Class<?> startClass) {

        List<Field> currentClassFields = new ArrayList<Field>(Arrays.asList(startClass.getDeclaredFields()));

        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null && !(parentClass.equals(BaseFragment.class))) {
            List<Field> parentClassFields = (List<Field>) getFields(parentClass);
            currentClassFields.addAll(parentClassFields);
        }

        return currentClassFields;
    }

    public void start(Fragment fragment) {
        start(fragment, true);
    }

    public void start(Fragment fragment, boolean addToBackstack) {
        ((BaseActivity) getActivity()).start(fragment, addToBackstack);
    }

    protected void stopLoading() {
        if (getBaseActivity() != null) getBaseActivity().stopLoading();
    }

    protected void showLoading() {
        if (getBaseActivity() != null) getBaseActivity().showLoading(true);
    }

    protected void showLoading(boolean cancelable) {
        if (getBaseActivity() != null) getBaseActivity().showLoading(cancelable);
    }

    protected void showLoading(boolean cancelable, String message) {
        if (getBaseActivity() != null) getBaseActivity().showLoading(cancelable, message);
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public void finish() {
        if (getBaseActivity() != null) getBaseActivity().getSupportFragmentManager().popBackStack();
    }
}
