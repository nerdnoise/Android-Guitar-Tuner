package com.chrynan.guitartuner;

import com.chrynan.guitartuner.tarsos.PitchDetectionResult;

/**
 * Created by chRyNaN on 1/14/2016.
 */
public interface TunerUpdate {

    void updateNote(Note newNote, PitchDetectionResult result);

}
