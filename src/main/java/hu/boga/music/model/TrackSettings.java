package hu.boga.music.model;

import java.awt.Color;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hu.boga.music.enums.NoteLength;
import hu.boga.music.enums.NoteName;
import hu.boga.music.enums.Tone;

public class TrackSettings {
    private int program;
//    private NoteLength length;
//    private NoteLength gapLength;
//    private int octave;
//    private NoteName scaleRoot;
//    private Tone scaleTone;
    private int midiChannel;
    private int volume;
//    private boolean pattern;

    private int rgb = new Random().nextInt();

    public TrackSettings() {

    }

    public TrackSettings(TrackSettings settings) {
//        this.scaleRoot = settings.scaleRoot;
//        this.length = settings.length;
        this.midiChannel = settings.midiChannel;
//        this.octave = settings.octave;
        this.program = settings.program;
//        this.scaleTone = settings.scaleTone;
        this.volume = settings.volume;
    }

    public int getProgram() {
        return program;
    }

//    public NoteLength getLength() {
//        return length;
//    }
//
//    public int getOctave() {
//        return octave;
//    }
//
//    public NoteName getScaleRoot() {
//        return scaleRoot;
//    }
//
//    public Tone getScaleTone() {
//        return scaleTone;
//    }

    public static TrackSettings defaultSettings() {
        TrackSettings ts = new TrackSettings();
//        ts.scaleRoot = NoteName.C;
//        ts.length = NoteLength.HARMICKETTED;
//        ts.gapLength = NoteLength.HARMICKETTED;
//        ts.scaleTone = Tone.MAJ;
        ts.program = 49;
//        ts.octave = 2;
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

//    public void setLength(NoteLength length) {
//        this.length = length;
//    }
//
//    public void setOctave(int octave) {
//        this.octave = octave;
//    }
//
//    public void setScaleRoot(NoteName scaleRoot) {
//        this.scaleRoot = scaleRoot;
//    }
//
//    public void setScaleTone(Tone scaleTone) {
//        this.scaleTone = scaleTone;
//    }

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

//    public void setGapLength(NoteLength selectedNoteLength) {
//        this.gapLength = selectedNoteLength;
//    }
//
//    public NoteLength getGapLength() {
//        return gapLength;
//    }
//
//    public boolean isPattern() {
//        return pattern;
//    }
//
//    public void setPattern(boolean pattern) {
//        this.pattern = pattern;
//    }

}
