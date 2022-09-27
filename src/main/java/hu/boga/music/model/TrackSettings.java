package hu.boga.music.model;

import java.awt.Color;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hu.boga.music.enums.NoteLength;
import hu.boga.music.enums.NoteName;
import hu.boga.music.enums.Tone;

public class TrackSettings {
    private int program;
    private int midiChannel;
    private int volume;

    private int rgb = new Random().nextInt();

    public TrackSettings() {

    }

    public TrackSettings(TrackSettings settings) {
        this.midiChannel = settings.midiChannel;
        this.program = settings.program;
        this.volume = settings.volume;
    }

    public int getProgram() {
        return program;
    }


    public static TrackSettings defaultSettings() {
        TrackSettings ts = new TrackSettings();
        ts.program = 49;
        ts.setVolume(120);
        return ts;
    }

    public int getMidiChannel() {
        return midiChannel;
    }

    public int getVolume() {
        return volume;
    }

    public void setProgram(int program) {
        this.program = program;
    }

    public void setMidiChannel(int midiChannel) {
        this.midiChannel = midiChannel;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @JsonIgnore
    public Color getColor() {
        return new Color(rgb);
    }

    public int getRgb() {
        return rgb;
    }

    public void setRgb(int rgb) {
        this.rgb = rgb;
    }

}
