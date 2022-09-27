package hu.boga.music.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import hu.boga.music.enums.NoteLength;

class NoteLengthTests {

    @Test
    void getErtek_Negyszeres_128() {
        assertEquals(128, NoteLength.NEGYSZERES.getErtek());
    }

    @Test
    void ofErtek_128_Negyszeres() {
        assertEquals(NoteLength.NEGYSZERES, NoteLength.ofErtek(128));
    }

    @Test
    void ofErtek_234_IsNull() {
        assertNull(NoteLength.ofErtek(234));
    }

    @Test
    void next_noteLength_isTIZENHATOD() {
        assertEquals(NoteLength.TIZENHATOD, NoteLength.HARMICKETTED.next());
    }

    @Test
    void next_noteLength_isHARMINCKETTED() {
        assertEquals(NoteLength.HARMICKETTED, NoteLength.NEGYSZERES.next());
    }

    @Test
    void prev_noteLength_isTIZENHATOD() {
        assertEquals(NoteLength.HARMICKETTED, NoteLength.TIZENHATOD.prev());
    }

    @Test
    void prev_noteLength_isNEGYSZERES() {
        assertEquals(NoteLength.NEGYSZERES, NoteLength.HARMICKETTED.prev());
    }

}
