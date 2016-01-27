package com.chrynan.guitartuner;

/**
 * Created by chRyNaN on 1/24/2016.
 */
public interface PitchControl {

    void play(double frequency);
    void play(Note note);
    void stop();

}
