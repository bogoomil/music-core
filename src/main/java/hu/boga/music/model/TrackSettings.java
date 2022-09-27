package hu.boga.music.model;

import java.awt.Color;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hu.boga.music.enums.NoteLength;
import hu.boga.music.enums.NoteName;
import hu.boga.music.enums.Tone;

public class TrackSettings {
    public int program;
    public int midiChannel;
    public int volume;

    public int rgb = new Random().nextInt();

    public TrackSettings() {

    }

    public TrackSettings(TrackSettings settings) {
        this.midiChannel = settings.midiChannel;
        this.program = settings.program;
        this.volume = settings.volume;
    }

    public static TrackSettings defaultSettings() {
        TrackSettings ts = new TrackSettings();
        ts.program = 49;
        ts.volume = 120;
        return ts;
    }


}
