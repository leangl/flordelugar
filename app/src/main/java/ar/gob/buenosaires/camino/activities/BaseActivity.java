package ar.gob.buenosaires.camino.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import ar.gob.buenosaires.camino.R;
import ar.gob.buenosaires.camino.fragments.BaseFragment;
import roboguice.activity.RoboFragmentActivity;

public class BaseActivity extends RoboFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void start(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment);
        if (addToBackStack) {
            tx.addToBackStack(fragment.getClass().getSimpleName());
        }
        tx.commit();
    }
    
    public <T extends BaseFragment> void replace(T fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment, "").addToBackStack("").commit();
    }

    public <T extends BaseFragment> void add(T fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment, "").commit();
    }

    public <T extends BaseFragment> void start(T fragment) {
        add(fragment);
    }

    private ProgressDialog progressDialog;

    public void stopLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showLoading(boolean cancelable) {
        showLoading(cancelable, getString(R.string.loading));
    }

    public void showLoading(boolean cancelable, String message) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, null, message, true, cancelable);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    progressDialog = null;
                }
            });
            progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    progressDialog = null;
                }
            });
        } else {
            Log.d("baseActivity", "Already showing progress dialog.");
        }
    }
}
