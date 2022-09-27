package hu.boga.music.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import hu.boga.music.enums.NoteLength;
import hu.boga.music.enums.NoteName;
import hu.boga.music.enums.Tone;
import hu.boga.music.model.Note;
import hu.boga.music.model.Track;
import hu.boga.music.model.TrackSettings;
import hu.boga.music.theory.Pitch;

class TrackTests {

    @Test
    void addNotes_trackSize_is1() {
        Track t = new Track();
        Note n = new Note();
        t.addNotes(Arrays.asList(n));
        assertEquals(1, t.getTrackMap().size());
    }

    @Test
    void addNotes_trackSize_is10() {
        Track t = new Track();
        Note n = new Note();
        t.addNotes(Arrays.asList(n));
        assertEquals(1, t.getTrackMap().size());
    }

//    @Test
//    void addNotes_noteAt10thTick_isNote() {
//        Track t = new Track();
//        Note n = new Note();
//        t.getSettings().setPointer(10);
//        t.addNotes(Arrays.asList(n));
//        assertEquals(n, t.getNotesAtTick(10).get(0));
//
//    }
//
//    @Test
//    void removeNote_trackSize_is0() {
//        Track t = new Track();
//        Note n = new Note();
//        t.getSettings().setPointer(10);
//        t.addNotes(Arrays.asList(n));
//        assertEquals(n, t.getNotesAtTick(10).get(0));
//        t.removeNote(n);
//        assertTrue(t.getNotesAtTick(10).isEmpty());
//    }
//
//    @Test
//    void copy_notes_size10() {
//        Track t = new Track();
//        for (int i = 0; i < 20; i++) {
//            Note n = new Note();
//            n.setSelected(i % 2 == 0);
//            t.getSettings().setPointer(i);
//            t.addNotes(Arrays.asList(n));
//        }
//        assertEquals(10, t.copy().entrySet().size());
//    }
//
//    @Test
//    void copy_notes_size0() {
//        Track t = new Track();
//        for (int i = 0; i < 20; i++) {
//            Note n = new Note();
//            t.getSettings().setPointer(i);
//            t.addNotes(Arrays.asList(n));
//        }
//        assertEquals(0, t.copy().entrySet().size());
//    }
//
//    @Test
//    void pasteNotes_note_pastedNoteTickIs30() {
//        Track t = new Track();
//        Note n = new Note();
//        n.setSelected(true);
//        t.getSettings().setPointer(10);
//        t.addNotes(Arrays.asList(n));
//
//        Map<Integer, List<Note>> c = t.copy();
//        t.paste(c, 20);
//        assertEquals(2, t.copy().entrySet().size());
//        assertTrue(!t.getNotesAtTick(20).isEmpty());
//
//    }

    @Test
    void pasteNotes_note_pastedNotePitchIs12() {
        Track t = new Track();

        Note n = new Note();
        n.setPitch(new Pitch(12));
        n.setSelected(true);

//        t.getSettings().setPointer(10);
        t.addNotes(Arrays.asList(n));

        n = new Note();
        n.setPitch(new Pitch(16));
        n.setSelected(true);

//        t.getSettings().setPointer(12);
        t.addNotes(Arrays.asList(n));

        Map<Integer, List<Note>> c = t.copy();

      //  t.paste(c, 20);

        assertEquals(4, t.copy().entrySet().size());
        assertTrue(!t.getNotesAtTick(20).isEmpty());
        assertEquals(12, t.getNotesAtTick(20).get(0).getPitch().getMidiCode());
        assertEquals(12, t.getNotesAtTick(20).get(0).getPitch().getMidiCode());

    }

    @Test
    void clone_tracksNotesAreEqual() {
        Track t = new Track();

        TrackSettings ts = new TrackSettings();
        ts.setProgram(23);
        ts.setLength(NoteLength.HARMICKETTED);
        ts.setOctave(3);
        ts.setScaleRoot(NoteName.Ab);
        ts.setScaleTone(Tone.LOCRIAN);
        ts.setGapLength(NoteLength.HARMICKETTED);
        t.setSettings(ts);

        Note n = new Note();
        n.setLength(NoteLength.DUPLA);
        n.setPitch(new Pitch(0));

        t.addNotes(Arrays.asList(n));

        Track cloned = t.clone();

        assertEquals(t.getNotesAtTick(0).get(0).getLength(), cloned.getNotesAtTick(0).get(0).getLength());
        assertEquals(t.getNotesAtTick(0).get(0).getMidiCode(), cloned.getNotesAtTick(0).get(0).getMidiCode());
        assertEquals(NoteLength.HARMICKETTED, cloned.getSettings().getLength());
        assertEquals(3, cloned.getSettings().getOctave());
        assertEquals(23, cloned.getSettings().getProgram());
        assertEquals(NoteName.Ab, cloned.getSettings().getScaleRoot());
        assertEquals(Tone.LOCRIAN, cloned.getSettings().getScaleTone());
    }

    @Test
    void setTempo_tempoIs100_true() {
        Track t = new Track();
        t.setTempo(100);
        assertTrue(t.getTempo() == 100);
    }

    @Test
    void addNotes_trackCurrentTick_is8() {
        Track t = new Track();
        Note n = new Note();
        n.setLength(NoteLength.NEGYED);
        n.setPitch(new Pitch(0));
        t.addNotes(Arrays.asList(n));
        assertEquals(8, t.getFirstEmptyTick());
    }

    @Test
    void clearNotes_trackMap_isEmpty() {
        Track t = new Track();
        Note n = new Note();
        n.setLength(NoteLength.DUPLA);
        n.setPitch(new Pitch(0));
        t.addNotes(Arrays.asList(n));

        t.clearAllNotes();

        assertTrue(t.getTrackMap().isEmpty());

    }

    @Test
    void removeSelected_notesRemoved() {
        Track t = new Track();
        Note n = new Note();
        n.setLength(NoteLength.DUPLA);
        n.setPitch(new Pitch(0));
        t.addNotes(Arrays.asList(n));
        n.setSelected(true);
        t.removeSelected();
        assertTrue(t.getNotesAtTick(0).isEmpty());

    }

    @Test
    void shiftOctavePlus_noteShifted12() {
        Track t = new Track();
        Note n = new Note();
        n.setPitch(new Pitch(0));
        t.addNotes(Arrays.asList(n));
        n.setSelected(true);
        t.shiftSelected(1);
        assertEquals(12, n.getPitch().getMidiCode());

    }

//    @Test
//    void getLastTickOfLastMeasure_tick_31() {
//        Track t = new Track();
//        Note n = new Note();
//        n.setLength(NoteLength.HARMICKETTED);
//        t.getSettings().setPointer(1);
//        t.addNotes(Arrays.asList(n));
//        assertEquals(32, t.getFirstEmptyMeasureTick());
//
//        n.setLength(NoteLength.EGESZ);
//        assertEquals(64, t.getFirstEmptyMeasureTick());
//
//    }
//
    @Test
    void getLastTickOfLastMeasure_tick_0() {
        Track t = new Track();
        assertEquals(0, t.getFirstEmptyMeasureTick());

    }

    @Test
    void selectAll_note_selected() {
        Track t = new Track();
        Note n = new Note();
        n.setLength(NoteLength.HARMICKETTED);
        t.addNotes(Arrays.asList(n));

        t.selectAll();
        assertTrue(n.isSelected());

    }

    @Test
    void unSelectAll_note_notSelected() {
        Track t = new Track();
        Note n = new Note();
        n.setLength(NoteLength.HARMICKETTED);
        t.addNotes(Arrays.asList(n));

        t.selectAll();
        t.unSelectAll();
        assertFalse(n.isSelected());

    }

    @Test
    void getFirstEmptyTick_tick_is2() {
        Track t = new Track();
        Note n = new Note();
        n.setLength(NoteLength.TIZENHATOD);
        t.addNotes(Arrays.asList(n));

        t.selectAll();
        t.unSelectAll();
        assertEquals(2, t.getFirstEmptyTick());

    }

//    @Test
//    void generateMeasurePattern_patternSize_is16() {
//        Track t = new Track();
//        Note note = new Note();
//        note.setLength(NoteLength.HARMICKETTED);
//
//        t.getSettings().setPointer(0);
//        t.getSettings().setGapLength(NoteLength.TIZENHATOD);
//        t.generatePattern(note);
//        assertEquals(16, t.getTrackMap().keySet().size());
//
//        t.clearAllNotes();
//        t.getSettings().setPointer(16);
//        t.getSettings().setGapLength(NoteLength.EGESZ);
//        note.setLength(NoteLength.FEL);
//        t.generatePattern(note);
//
//        assertEquals(1, t.getTrackMap().keySet().size());
//    }

}
