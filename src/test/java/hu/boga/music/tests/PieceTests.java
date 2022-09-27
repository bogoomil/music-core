package hu.boga.music.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import hu.boga.music.enums.NoteLength;
import hu.boga.music.model.Note;
import hu.boga.music.model.Piece;
import hu.boga.music.model.Track;

class PieceTests {
    @Test
    void addTrack_tracksSize_greaterThan0() {
        Piece p = new Piece();
        Track t = new Track();
        p.addTrack(t);
        assertEquals(1, p.getTracks().size());
    }

    @Test
    void setName_name_isA() {
        Piece p = new Piece();
        p.setName("A");
        assertEquals("A", p.getName());
    }

    @Test
    void setTempo_tempo_is120() {
        Piece p = new Piece();
        p.setTempo(120);
        assertEquals(120, p.getTempo());
    }

    @Test
    void removeTrack_trackSize_is0() {
        Piece p = new Piece();
        Track t = new Track();
        p.addTrack(t);
        p.removeTrack(t);
        assertEquals(0, p.getTracks().size());

    }

    @Test
    void getMaxTick_tick_is1() {
        Piece p = new Piece();
        Track t = new Track();
        p.addTrack(t);
        Note n = new Note();
        n.setLength(NoteLength.HARMICKETTED);
        t.addNotes(Arrays.asList(n));

        assertEquals(1, p.getMaxTick());

    }

    @Test
    void setTracks_track_isList() {
        Piece p = new Piece();
        List<Track> tracks = new ArrayList<>();
        p.setTracks(tracks);
        assertEquals(tracks, p.getTracks());
    }

}
