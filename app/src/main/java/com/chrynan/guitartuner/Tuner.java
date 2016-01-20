package com.chrynan.guitartuner;

import android.Manifest;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.chrynan.guitartuner.tarsos.PitchDetectionResult;
import com.chrynan.guitartuner.tarsos.Yin;
import com.chrynan.guitartuner.util.AudioUtils;
import com.chrynan.guitartuner.util.PermissionUtils;

/**
 * Created by chRyNaN on 1/14/2016. This class binds the logic between the view and the pitch detection process.
 * This way a custom tuner view can be created and as long as it implements the TunerUpdate interface it can be
 * used instead of the default view.
 */
public class Tuner {
    private static final String TAG = Tuner.class.getSimpleName();
    private TunerUpdate view;
    private int sampleRate;
    private int bufferSize;
    private volatile int readSize;
    private volatile int amountRead;
    private volatile float[] buffer;
    private volatile short[] intermediaryBuffer;
    private AudioRecord audioRecord;
    private volatile Yin yin;
    private volatile Note currentNote;
    private volatile PitchDetectionResult result;
    private volatile boolean isRecording;
    private volatile Handler handler;
    private Thread thread;

    //provide the tuner view implementing the TunerUpdate to the constructor
    public Tuner(TunerUpdate view){
        this.view = view;
        if(view != null && view instanceof View &&
                PermissionUtils.hasPermission(((View) view).getContext(), Manifest.permission.RECORD_AUDIO)) {
            init();
        }
    }

    public void init(){
        this.sampleRate = AudioUtils.getSampleRate();
        this.bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
        this.readSize = bufferSize / 4;
        this.buffer = new float[readSize];
        this.intermediaryBuffer = new short[readSize];
        this.isRecording = false;
        this.audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, sampleRate, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        this.yin = new Yin(sampleRate, readSize);
        this.currentNote = new Note(Note.DEFAULT_FREQUENCY);
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void start(){
        Log.d(TAG, "start");
        if(audioRecord != null) {
            isRecording = true;
            audioRecord.startRecording();
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //Runs off the UI thread
                    findNote();
                }
            }, "Tuner Thread");
            thread.start();
        }
    }

    private void findNote(){
        Log.d(TAG, "findNote");
        while(isRecording){
            amountRead = audioRecord.read(intermediaryBuffer, 0, readSize);
            buffer = shortArrayToFloatArray(intermediaryBuffer);
            result = yin.getPitch(buffer);
            currentNote.changeTo(result.getPitch());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Runs on the UI thread
                    view.updateNote(currentNote, result);
                }
            });
        }
    }

    private float[] shortArrayToFloatArray(short[] array){
        float[] fArray = new float[array.length];
        for(int i = 0; i < array.length; i++){
            fArray[i] = (float) array[i];
        }
        return fArray;
    }

    public void stop(){
        Log.d(TAG, "stop");
        isRecording = false;
        if(audioRecord != null) {
            audioRecord.stop();
        }
    }

    public void release(){
        Log.d(TAG, "release");
        if(audioRecord != null) {
            audioRecord.release();
        }
    }

    public boolean isInitialized(){
        if(audioRecord != null){
            return true;
        }
        return false;
    }

}
