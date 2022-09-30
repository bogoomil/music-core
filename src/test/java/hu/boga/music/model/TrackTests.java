package hu.boga.music.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.boga.music.enums.NoteLength;
import hu.boga.music.enums.NoteName;
import hu.boga.music.enums.Tone;
import hu.boga.music.model.Note;
import hu.boga.music.model.Track;
import hu.boga.music.model.TrackSettings;
import hu.boga.music.theory.Pitch;

import static org.junit.jupiter.api.Assertions.*;

class TrackTests {
    Track track;

    @BeforeEach
    void setUp(){
        track = new Track();
        Note note = new Note();
        note.setPitch(new Pitch(12));
        note.setLength(NoteLength.HARMICKETTED);
        track.getNotesAtTick(0).add(note);
    }

    @Test
    void clearAllNotestest(){
        track.clearAllNotes();
        assertTrue(track.getTrackMap().isEmpty());
    }

    @Test
    void cloneTest(){
        track.getSettings().volume = 123;
        Track newTrack = track.clone();
        assertTrue(newTrack.getTrackMap().size() == 1);
        assertTrue(newTrack.getSettings().volume == 123);
    }

    @Test
    void removeNoteTest(){
        Note n = track.getNotesAtTick(0).get(0);
        track.removeNote(n);
        assertTrue(track.getNotesAtTick(0).isEmpty());
    }

    @Test
    void moveNoteToTickTest(){
        Note n = track.getNotesAtTick(0).get(0);
        track.moveNoteToTick(n, 1);
        assertTrue(track.getNotesAtTick(0).isEmpty());
        assertTrue(track.getNotesAtTick(1).size() == 1);
    }

    @Test
    void copyTestSelected(){
        track.selectAll();
        track.copy();
        assertTrue(Track.copiedNotes.get(0).size() == 1);
    }

    @Test
    void copyTestUnSelected(){
        track.copy();
        assertNull(Track.copiedNotes.get(0) );
    }

    @Test
    void pasteTest(){
        track.selectAll();
        track.copy();
        track.paste();
        assertTrue(track.getTrackMap().size() == 2);
        assertTrue(track.getTrackMap().get(2).size() == 1);
    }

//    @Test
//    void unSelectAllTest(){
//        track.selectAll();
//        assertTrue(track.c.size() == 1);
//        track.unSelectAll();
//        assertTrue(track.copy().size() == 0);
//    }

    @Test
    void getFirstEmptyTickTest(){
        assertTrue(track.getFirstEmptyTick() == 1);
    }

    @Test
    void removeSelectedTest(){
        track.selectAll();
        track.removeSelected();
        assertTrue(track.getTrackMap().get(0).isEmpty());
    }

    @Test
    void shiftSelectedTest(){
        track.selectAll();
        track.shiftSelected(1);
        assertTrue(track.getTrackMap().get(0).get(0).getPitch().getMidiCode() == 24);
    }

    @Test
    void getFirstEmptyMeasureTickTest(){
        assertTrue(track.getFirstEmptyMeasureTick() == 32);
    }

}
