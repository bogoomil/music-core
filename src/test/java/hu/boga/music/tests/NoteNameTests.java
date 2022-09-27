package hu.boga.music.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hu.boga.music.enums.NoteName;

class NoteNameTests {

    @Test
    void byCode_0_C() {
        assertEquals(NoteName.C, NoteName.byCode(0));
        assertEquals(NoteName.C, NoteName.byCode(12));
        assertEquals(NoteName.B, NoteName.byCode(11));
    }

}
