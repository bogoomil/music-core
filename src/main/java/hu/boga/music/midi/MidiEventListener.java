package hu.boga.music.midi;

public interface MidiEventListener {

    void processTickEvent(int tick);

    void processMeasureEvent(int measure);

}
