package hu.boga.music.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import hu.boga.music.controller.AppController;
import hu.boga.music.enums.NoteLength;
import hu.boga.music.model.Note;
import hu.boga.music.model.Track;
import hu.boga.music.theory.Pitch;

public class AppControllerTests {
    @Test
    void addNotesToCurrentTrack_notesSize_1() {
        AppController c = new AppController();
        Note n = new Note();
        n.setLength(NoteLength.NEGYED);
        n.setPitch(new Pitch(0));
        List<Note> notes = Arrays.asList(n);
   //     c.addNotesToCurrentTrack(notes);
        assertEquals(1, c.getPiece().getTracks().get(c.getCurrentTrackIndex()).getTrackMap().entrySet().size());
    }

    @Test
    void addNotesToCurrentTrack_trackSettingsIsPattern_notesSizeIs16() {
        AppController c = new AppController();
        Note n = new Note();
        n.setLength(NoteLength.HARMICKETTED);
        n.setPitch(new Pitch(0));
        List<Note> notes = Arrays.asList(n);

        c.getCurrentTrack().getSettings().setPattern(true);
        c.getCurrentTrack().getSettings().setGapLength(NoteLength.TIZENHATOD);

  //      c.addNotesToCurrentTrack(notes);
        assertEquals(16, c.getPiece().getTracks().get(c.getCurrentTrackIndex()).getTrackMap().entrySet().size());
    }

    @Test
    void getCurrentTrack_trackIsTrack_true() {
        AppController c = new AppController();
        c.getPiece().getTracks().clear();
        Track t = new Track();
        c.getPiece().addTrack(t);

        assertEquals(t, c.getCurrentTrack());
    }

    @Test
    void setCurrentTrack_trackChanged() {
        AppController c = new AppController();
        c.getPiece().getTracks().clear();
        Track t = new Track();
        c.getPiece().addTrack(t);

        Track t2 = new Track();
        c.getPiece().addTrack(t2);

        Track t3 = new Track();
        c.getPiece().addTrack(t3);

        c.setCurrentTrack(t2);
        assertEquals(t2, c.getCurrentTrack());

    }

}
