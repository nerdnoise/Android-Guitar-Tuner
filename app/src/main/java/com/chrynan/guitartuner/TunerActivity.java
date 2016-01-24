package com.chrynan.guitartuner;

import android.Manifest;
import android.animation.Animator;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.chrynan.guitartuner.util.PermissionUtils;

/**
 * Created by chRyNaN on 1/18/2016.
 * Circular Reveal Animation Transition help from here: https://gist.github.com/ferdy182/d9b3525aa65b5b4c468a
 */
public class TunerActivity extends AppCompatActivity {
    private static final String TAG = TunerActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TunerFragment tunerFragment;
    private PitchFragment pitchFragment;
    private boolean showCancel;

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        showCancel = false;
        setContentView(R.layout.tuner_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tunerFragment = new TunerFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, tunerFragment, TunerFragment.TAG).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if(showCancel) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }else{
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if(showCancel){
                    transitionBackToTunerFragment(pitchFragment.unreveal(pitchFragment.getX(), pitchFragment.getY()));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case TunerFragment.AUDIO_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(tunerFragment != null) {
                        tunerFragment.start();
                    }
                }
                break;
        }
    }

    public void transitionToPitchFragment(Note note, float x, float y){
        pitchFragment = PitchFragment.newInstance(note, x, y);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, pitchFragment, PitchFragment.TAG).commit();
        tunerFragment.stop();
        showCancel = true;
        invalidateOptionsMenu();
    }

    public void transitionBackToTunerFragment(Animator anim){
        if(anim != null){
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // remove the fragment only when the animation finishes
                    getSupportFragmentManager().beginTransaction().remove(pitchFragment).commit();
                    //to prevent flashing the fragment before removing it, execute pending transactions inmediately
                    getSupportFragmentManager().executePendingTransactions();
                    tunerFragment.start();
                    showCancel = false;
                    invalidateOptionsMenu();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            anim.start();
        }else{
            getSupportFragmentManager().beginTransaction().remove(pitchFragment).commit();
            tunerFragment.start();
            showCancel = false;
            invalidateOptionsMenu();
        }
    }

}
