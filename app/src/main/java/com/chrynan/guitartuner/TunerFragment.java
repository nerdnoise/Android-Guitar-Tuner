package com.chrynan.guitartuner;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chrynan.guitartuner.util.PermissionUtils;
import com.chrynan.guitartuner.view.DialView;
import com.chrynan.guitartuner.view.TunerView;

/*
 * Copyright 2016 chRyNaN
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Created by chRyNaN on 1/22/2016.
 */
public class TunerFragment extends Fragment {
    public static final String TAG = TunerFragment.class.getSimpleName();
    public static final int AUDIO_PERMISSION_REQUEST_CODE = 4;
    private TunerView view;
    private Tuner tuner;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.tuner_fragment, parent, false);
        view = (TunerView) v.findViewById(R.id.tuner_view);
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

    public void stop(){
        if(tuner != null){
            tuner.stop();
        }
    }

}
