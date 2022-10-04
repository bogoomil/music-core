package hu.boga.music.midi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import hu.boga.music.enums.NoteLength;
import hu.boga.music.model.Note;
import hu.boga.music.model.Track;

/**
 * Soundfont sf2 fájlokat a /usr/local/share/soundfonts könyvtárban keresi,
 * default.sf2 néven
 *
 * @author kunb
 */

public class MidiEngine {

    public static final int CHORD_CHANNEL = 0;
    public static final int MEASURE_START_MESSAGE_TYPE = 39;
    public static final int TICK_START_MESSAGE_TYPE = 40;
    public static final int TICK_END_MESSAGE_TYPE = 41;

    /**
     * negyed hang felbontása tick-ekre. 4 negyed = MidiEngine.TICKS_IN_MEASURE
     * tick;
     */
    public static final int RESOLUTION = 8;

    public static final int TICKS_IN_MEASURE = RESOLUTION * 4;

    private static Synthesizer synth;

    private static Sequencer sequencer;

    private static final List<MidiEventListener> LISTENERS = new ArrayList<>();

    // private static List<Integer> measureStarts;
    private static float tempoFactor = 1;

    public static double getNoteLenghtInMs(NoteLength length, int tempo) {
        int tickCount = length.getErtek();
        return getTickLengthInMillis(tempo) * tickCount;
    }

    public static Synthesizer getSynth() {
        if (synth == null) {
            initSynth();
        }
        if (!synth.isOpen()) {
            try {
                synth.open();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
        return synth;
    }

    public static Sequencer getSequencer() {
        if (sequencer == null) {
            try {
                initSequencer();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }

        return sequencer;
    }

    private static void initSequencer() throws MidiUnavailableException {
        sequencer = MidiSystem.getSequencer();

        sequencer.addMetaEventListener(new MetaEventListener() {

            @Override
            public void meta(MetaMessage meta) {
                String s = new String(meta.getData());
                int val = Integer.parseInt(s);
                if (meta.getType() == MEASURE_START_MESSAGE_TYPE) {
                    LISTENERS.forEach(l -> l.processMeasureEvent(val));
                } else if (meta.getType() == TICK_START_MESSAGE_TYPE) {
                    LISTENERS.forEach(l -> l.processTickEvent(val));
                }
            }
        });
    }

    public static double getTickLengthInMillis(int tempo) {
        double msInNegyed = 60000d / tempo; // 120-as tempo esetén 500 ms egy
        // negyed
        // // hang hossza
        double measureLengthInMs = msInNegyed * 4; // ütem hossza 120-as
        // temponál

        double tickLengthInMs = measureLengthInMs / MidiEngine.TICKS_IN_MEASURE;
        // // 2000 ms
        return tickLengthInMs;
    }

    private static void initSynth() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void playTracks(List<Track> tracks, int tempo, int loopCount) throws InvalidMidiDataException, MidiUnavailableException, IOException {
        Sequencer sequencer = getSequencer(loopCount);
        Sequence seq = new Sequence(Sequence.PPQ, MidiEngine.RESOLUTION);
        prepareMidiTracks(tracks, seq, true);
        sequencer.setSequence(seq);
        sequencer.setTickPosition(0);
        sequencer.start();
        sequencer.setTempoInBPM(tempo);
    }

    private static Sequencer getSequencer(int loopCount) throws MidiUnavailableException {
        Sequencer sequencer = MidiEngine.getSequencer();
        sequencer.setTempoFactor(tempoFactor);
        sequencer.setLoopCount(loopCount);
        if (!sequencer.isOpen()) {
            sequencer.open();
        }
        return sequencer;
    }

    public static void exportMidi(List<Track> tracks, int tempo, String fileName) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        Sequencer sequencer = getSequencer(1);
        Sequence seq = new Sequence(Sequence.PPQ, MidiEngine.RESOLUTION);
        prepareMidiTracks(tracks, seq, false);
        sequencer.setSequence(seq);
        sequencer.setTickPosition(0);
        sequencer.setTempoInBPM(tempo);
        File file = new File(fileName);
        MidiSystem.write(seq, 1, file);

    }

    private static void prepareMidiTracks(List<Track> tracks, Sequence seq, boolean withMetamessages) {
        AtomicInteger count = new AtomicInteger(0);
        tracks.stream().sorted(Comparator.comparing(Track::getFirstEmptyMeasureTick)).forEach(t -> {
            try {
                javax.sound.midi.Track track = MidiEngine.getInstrumentTrack(seq, t.getSettings().midiChannel, t.getSettings().program);
                playTrack(t, track);
                if (withMetamessages) {
                    if (count.intValue() == 0) {
                        setMidiMetaMessages(t, track);
                        count.incrementAndGet();
                    }
                }

            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        });
    }

    public static void setMidiMetaMessages(Track t, javax.sound.midi.Track track) throws InvalidMidiDataException {
        setTicksMetaMessages(t, track);
        setMeasureEndMetaMessage(t, track);
    }

    private static void setTicksMetaMessages(Track t, javax.sound.midi.Track track) throws InvalidMidiDataException {
        int lastTick = t.getFirstEmptyMeasureTick();

        for (int i = 0; i < lastTick; i++) {
            byte[] m = String.valueOf(i).getBytes();
            final MetaMessage metaMessageMeasureStart = new MetaMessage(TICK_START_MESSAGE_TYPE, m, m.length);
            final MidiEvent me = new MidiEvent(metaMessageMeasureStart,
                    i);
            track.add(me);

        }

    }

    public static void setMeasureEndMetaMessage(Track t, javax.sound.midi.Track track) throws InvalidMidiDataException {
        int lastTick = t.getFirstEmptyMeasureTick();
        byte[] m = String.valueOf(lastTick).getBytes();
        final MetaMessage metaMessageMeasureStart = new MetaMessage(MEASURE_START_MESSAGE_TYPE, m, m.length);
        final MidiEvent me = new MidiEvent(metaMessageMeasureStart,
                lastTick);
        track.add(me);
    }

    private static javax.sound.midi.Track getInstrumentTrack(Sequence seq, int channel, int program) throws InvalidMidiDataException {
        javax.sound.midi.Track track = seq.createTrack();
        ShortMessage instrumentChange = new ShortMessage();
        instrumentChange.setMessage(ShortMessage.PROGRAM_CHANGE, channel, program, 0);
        MidiEvent changeInstrument = new MidiEvent(instrumentChange, 0);
        track.add(changeInstrument);
        return track;
    }

    private static void playTrack(Track t, javax.sound.midi.Track track) {
        t.getTrackMap().forEach((tick, notes) -> {
            try {
                MidiEngine.addNotesToTrack(track, t.getSettings().midiChannel, notes, tick);

            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        });
    }

    private static void addNotesToTrack(javax.sound.midi.Track track, int channel, List<Note> notes, int tick) throws InvalidMidiDataException {
        for (Note n : notes) {
            addNoteToTrack(track, channel, n, tick);
        }
    }

    private static void addNoteToTrack(javax.sound.midi.Track track, int channel, Note note, int tick) throws InvalidMidiDataException {

        int startInTick = tick;
        int endInTick = startInTick + note.getLength().getErtek();

        ShortMessage a = new ShortMessage();
        a.setMessage(ShortMessage.NOTE_ON, channel, note.getPitch().getMidiCode(), note.getVelocity());
        MidiEvent noteOn = new MidiEvent(a, startInTick);
        track.add(noteOn);

//        track.add(new MidiEvent(
//                new ShortMessage(ShortMessage.CONTROL_CHANGE, channel, 7, vol),
//                startInTick));

        ShortMessage b = new ShortMessage();
        b.setMessage(ShortMessage.NOTE_OFF, channel, note.getPitch().getMidiCode(), note.getVelocity());
        MidiEvent noteOff = new MidiEvent(b, endInTick);
        track.add(noteOff);

    }

    public static void playNotes(int instrument, int tempo, Note... notes) {
        MidiChannel[] channels = MidiEngine.getSynth().getChannels();
        channels[0].programChange(instrument);

        for (int i = 0; i < notes.length; i++) {
            playNote(tempo, channels, notes[i]);
        }

    }

    public static void playNote(int tempo, MidiChannel[] channels, Note note) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    tryTurnOnOff(tempo, channels, note);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void tryTurnOnOff(int tempo, MidiChannel[] channels, Note note) throws InterruptedException {
                channels[0].noteOn(note.getPitch().getMidiCode(), 200);
                int length = (int) getNoteLenghtInMs(note.getLength(), tempo);

                Thread.sleep(length);
                channels[0].noteOff(note.getPitch().getMidiCode());

            }
        });
        thread.start();
    }

    public static Integer getTickIndexByMillis(long millis, int tempo) {
        int tickLength = (int) getTickLengthInMillis(tempo);
        return (int) (millis / tickLength);
    }

    public static void addMidiEventListener(MidiEventListener listenr) {
        LISTENERS.add(listenr);
    }

}
