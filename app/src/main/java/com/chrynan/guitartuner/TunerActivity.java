package com.chrynan.guitartuner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.chrynan.guitartuner.util.PermissionUtils;

/**
 * Created by chRyNaN on 1/18/2016.
 */
public class TunerActivity extends AppCompatActivity {
    private static final String TAG = TunerActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 4;
    private Tuner tuner;
    private Toolbar toolbar;
    private TunerUpdate view;

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuner_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        view = (TunerUpdate) findViewById(R.id.tuner_view);
        tuner = new Tuner(view);
    }

    @Override
    public void onResume(){
        Log.d(TAG, "onResume");
        super.onResume();
        if(tuner.isInitialized()) {
            tuner.start();
        }else{
            PermissionUtils.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        switch(requestCode){
            case PERMISSION_REQUEST_CODE:
                Log.d(TAG, "PERMISSION_REQUEST_CODE");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tuner.start();
                }
                break;
        }
    }

        @Override
    public void onPause(){
            Log.d(TAG, "onPause");
        super.onPause();
        tuner.stop();
    }

    @Override
    public void onDestroy(){
        Log.d(TAG, "onDestroy");
        tuner.release();
        super.onDestroy();
    }

}
