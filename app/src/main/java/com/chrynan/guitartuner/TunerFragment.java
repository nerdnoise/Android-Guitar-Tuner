package com.chrynan.guitartuner;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chrynan.guitartuner.util.PermissionUtils;
import com.chrynan.guitartuner.view.CircleTunerView;
import com.chrynan.guitartuner.view.DialView;

/**
 * Created by chRyNaN on 1/22/2016.
 */
public class TunerFragment extends Fragment {
    public static final String TAG = TunerFragment.class.getSimpleName();
    public static final int AUDIO_PERMISSION_REQUEST_CODE = 4;
    private CircleTunerView view;
    private Tuner tuner;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.tuner_fragment, parent, false);
        view = (CircleTunerView) v.findViewById(R.id.tuner_view);
        view.addOnNoteSelectedListener(new DialView.OnNoteSelectedListener() {
            @Override
            public void onNoteSelected(Note note, float x, float y) {
                Log.d(TAG, "onNoteSelected: note = " + note + "; x = " + x + "; y = " + y);
                if(getActivity() instanceof TunerActivity){
                    ((TunerActivity) getActivity()).transitionToPitchFragment(note, x, y);
                }
            }
        });
        tuner = new Tuner(view);
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(tuner.isInitialized()) {
            tuner.start();
        }else{
            PermissionUtils.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        tuner.stop();
    }

    @Override
    public void onDestroy(){
        tuner.release();
        super.onDestroy();
    }

    public void start(){
        if(tuner != null){
            tuner.start();
        }
    }

}
