package hu.boga.music.midi;


import hu.boga.music.enums.NoteLength;
import hu.boga.music.model.Note;
import hu.boga.music.theory.Pitch;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MidiConverter {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    File file;

    public MidiConverter(File file) {
        this.file = file;
    }

    public List<hu.boga.music.model.Track> convertMidiFileToTracks() throws InvalidMidiDataException, IOException {
        List<hu.boga.music.model.Track> retVal = new ArrayList<>();
        Sequence sequence = MidiSystem.getSequence(file);

        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            hu.boga.music.model.Track myTrack = new hu.boga.music.model.Track();
            retVal.add(myTrack);

            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    System.out.print("Channel: " + sm.getChannel() + " ");
                    myTrack.getSettings().midiChannel = sm.getChannel();
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);

                        MidiEvent noteOff = findMatchingNoteOff(track, i, event);

                        long offTick = noteOff.getTick();
                        Note myNot = new Note();
                        myNot.setPitch(new Pitch(key));
                        NoteLength.ofErtek((int) (offTick - event.getTick()))
                                .ifPresentOrElse(nl -> myNot.setLength(nl), () -> myNot.setLength(NoteLength.HARMICKETTED));
                        myNot.setVelocity(velocity);
                        myTrack.getNotesAtTick((int) event.getTick()).add(myNot);

                    } else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else {
                        System.out.println("Command:" + sm.getCommand() + " :: " + sm.getData1() + " ::  " + sm.getData2());
                    }
                } else {
                    System.out.println("Other message: " + message.getClass() + " :: " + message.getMessage() + " :: " + message.getLength());
                }
            }
            System.out.println();
        }
        return retVal;
    }

    public static MidiEvent findMatchingNoteOff(Track track, int noteOnIndex, MidiEvent noteOn) {
        assert isNoteOnEvent(noteOn);

        for (int i = noteOnIndex; i < track.size(); i++) {
            MidiEvent event = track.get(i);
            if (isNoteOffEvent(event) && (getNoteValue(noteOn) == getNoteValue(event))) {
                return event;
            }
        }
        System.exit(1);
        return null;

    }

    /**
     * Return true if event is a Note On event.
     */
    public static boolean isNoteOnEvent(MidiEvent event) {
        return isNoteOnMessage(event.getMessage());
    }

//    private long findClosestOffEvent(Track track, long fromTick, int currentKey) {
//        long tick = -1;
//        for (long i = fromTick; i < track.size(); i++) {
//            MidiEvent event = track.get((int) i);
//            MidiMessage message = event.getMessage();
//            if (message instanceof ShortMessage) {
//                ShortMessage sm = (ShortMessage) message;
//                if (sm.getCommand() == NOTE_OFF) {
//                    int key = sm.getData1();
//                    if (key == currentKey) {
//                        return event.getTick();
//                    }
//                }
//            }
//        }
//        return tick;
//        //throw new RuntimeException("invalid midi data found: track: " + track + " at tick: " + fromTick + ", key: " + currentKey);
//    }

    /**
     * Return the note value for a note on or note off event.
     */
    public static int getNoteValue(MidiEvent noteOnOff) {
        assert isNoteOnEvent(noteOnOff) || isNoteOffEvent(noteOnOff);

        return getNoteValue(noteOnOff.getMessage());
    }

    /**
     * Return the note value for a note on or off message.
     */
    public static int getNoteValue(MidiMessage noteOnOff) {
        assert isNoteOnMessage(noteOnOff) || isNoteOffMessage(noteOnOff);

        return noteOnOff.getMessage()[1];
    }

    /**
     * Return the velocity for a note on or note off event.
     */
    public static int getVelocity(MidiEvent noteOnOff) {
        // I can't check for this because isNoteOnMessage calls getVelocity
        //assert isNoteOnEvent(noteOnOff) || isNoteOffEvent(noteOnOff);

        return getVelocity(noteOnOff.getMessage());
    }

    /**
     * Return the velocity for a note on or off message.
     */
    public static int getVelocity(MidiMessage noteOnOff) {
        // I can't check for this because isNoteOnMessage calls getVelocity
        //assert isNoteOnMessage(noteOnOff) || isNoteOffMessage(noteOnOff);

        return noteOnOff.getMessage()[2];
    }

    /**
     * Return true if event is a Note Off event.
     */
    public static boolean isNoteOffEvent(MidiEvent event) {
        return isNoteOffMessage(event.getMessage());
    }

    /**
     * Return true if message is a Note On message.
     */
    public static boolean isNoteOnMessage(MidiMessage message) {
        // The status can be a range of values, depending on what channel it is on.
        // Also, the velocity cannot be zero, otherwise it is a note off message.
        return message.getStatus() >= 144 && message.getStatus() < 160
                && getVelocity(message) > 0;
    }

    /**
     * Return true if message is a Note Off message.
     */
    public static boolean isNoteOffMessage(MidiMessage message) {
        // It is a note off event if the status indicates it is a
        // note off message.  Or, if it is a note on message and
        // the velocity is zero.
        return (message.getStatus() >= 128 && message.getStatus() < 144)
                || (message.getStatus() >= 144 && message.getStatus() < 160 && getVelocity(message) == 0);
    }
}
