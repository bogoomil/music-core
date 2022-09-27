package hu.boga.music.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.boga.music.enums.NoteLength;
import hu.boga.music.model.Note;
import hu.boga.music.theory.Pitch;

class NoteTests {

    // Static mezőket explicit initializálni kell!!!
    @BeforeEach
    void initCounter() {
        Note.setCounter(0);
    }

    @Test
    void note_id_is0() {
        Note n = new Note();
        assertEquals(0, n.getId());
        n = new Note();
        assertEquals(1, n.getId());

        Note n2 = new Note(n);
        assertEquals(2, n2.getId());

        Pitch p = new Pitch(3);
        n2.setPitch(p);

        assertEquals(p, n2.getPitch());

        NoteLength nl = NoteLength.EGESZ;
        n2.setLength(nl);

        assertEquals(nl, n2.getLength());
        assertTrue(n2.equals(n2));
        assertNotEquals(n2, n);

        // n.setPitch(n2.getPitch());
        // n.setLength(n2.getLength());
        // assertEquals(n2, n);

        assertEquals(p.getMidiCode(), n2.getMidiCode());
        assertFalse(5 == n2.getMidiCode());
        assertFalse(n2.equals(null));
        assertFalse(n2.equals(p));

    }

    @Test
    void setSelected_selected_isTrue() {
        Note n = new Note();
        n.setSelected(true);
        assertEquals(true, n.isSelected());
    }

    @Test
    void setSelected_selected_isFalse() {
        Note n = new Note();
        assertEquals(false, n.isSelected());
    }

    @Test
    void toggleSelection_selected_true() {
        Note n = new Note();
        n.toggleSelection();
        assertTrue(n.isSelected());
        n.toggleSelection();
        assertFalse(n.isSelected());
    }

    @Test
    void incrementLength_noteLength_32() {
        Note n = new Note();
        n.setLength(NoteLength.HARMICKETTED);
        n.incrementLength();
        assertEquals(NoteLength.TIZENHATOD, n.getLength());
    }

    @Test
    void decrementLength_noteLength_32() {
        Note n = new Note();
        n.setLength(NoteLength.FEL);
        n.decrementLength();
        assertEquals(NoteLength.NEGYED, n.getLength());
        n.setLength(NoteLength.HARMICKETTED);
        n.decrementLength();
        assertEquals(NoteLength.NEGYSZERES, n.getLength());
    }

    @Test
    void equalsTest(){
        Note n1 = new Note();
        Note n2 = new Note();
        n2.setId(0);

        assertEquals(n2, n1);
    }
}
