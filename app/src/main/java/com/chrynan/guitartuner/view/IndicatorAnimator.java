package com.chrynan.guitartuner.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;

import com.chrynan.guitartuner.Note;

/**
 * Created by chRyNaN on 1/21/2016. This class encapsulates an ObjectAnimator which will be used to animate
 * the IndicatorView within a CircleTunerView. The ObjectAnimator updates the IndicatorView's angle value which
 * changes its position.
 */
public class IndicatorAnimator {
    private static final String TAG = IndicatorAnimator.class.getSimpleName();
    private IndicatorView view;
    private int duration;
    private float angle;
    private ObjectAnimator anim;

    public IndicatorAnimator(IndicatorView view){
        this.view = view;
        this.duration = 1000;
        this.angle = 0;
    }

    private void init(){
        if(anim != null && anim.isRunning()){
            anim.cancel();
        }
        this.anim = ObjectAnimator.ofFloat(view, "angle", view.getAngle(), angle);
        this.anim.setDuration(duration);
        this.anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(view != null){
                    view.showAngle();
                }
            }
        });
    }

    public IndicatorView getView(){
        return view;
    }

    public void setView(IndicatorView view){
        this.view = view;
    }

    public int getDuration(){
        return duration;
    }

    public void setDuration(int durationInMilliseconds){
        this.duration = durationInMilliseconds;
    }

    public float getAngle(){
        return angle;
    }

    public void setAngle(float angle){
        this.angle = angle;
    }

    public void start(){
        init();
        if(anim != null){
            anim.start();
        }
    }

    public void start(Note note, double angleInterval){
        if(angle != calculateNewAngle(note, angleInterval)){
            init();
            if (anim != null) {
                anim.start();
            }
        }
    }

    private float calculateNewAngle(Note note, double angleInterval){
        int angleDifference;
        double percentage;
        float newAngle;
        if(note.getActualFrequency() < note.getFrequency()){
            percentage = ((note.getActualFrequency() - note.getNoteBelowFrequency()) * 100)
                    / (note.getFrequency() - note.getNoteBelowFrequency());
            angleDifference = (int) ((percentage / 100) * angleInterval);
            newAngle = (float) ((angleInterval * note.getCToBNotesIndex()) - angleDifference);
        }else{
            percentage = ((note.getActualFrequency() - note.getFrequency()) * 100)
                    / (note.getNoteAboveFrequency() - note.getFrequency());
            angleDifference = (int) ((percentage / 100) * angleInterval);
            newAngle = (float) ((angleInterval * note.getCToBNotesIndex()) + angleDifference);
        }
        if(newAngle < 0){
            newAngle = 360 + newAngle;
        }else if(newAngle > 360){
            newAngle = newAngle % 360;
        }
        angle = (int) newAngle;
        Log.d(TAG, "note = " + note.getNote() + "; angle = " + angle);
        return angle;
    }

}
