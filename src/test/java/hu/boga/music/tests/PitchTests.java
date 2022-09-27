package hu.boga.music.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import hu.boga.music.enums.NoteName;
import hu.boga.music.theory.Pitch;

class PitchTests {

    @Test
    void getName_name_isC() {
        Pitch p = new Pitch(12);
        assertEquals(NoteName.C, p.getName());
        assertEquals(1, p.getOctave());
        assertEquals(12, p.getMidiCode());
        p.setMidiCode(24);
        assertEquals(24, p.getMidiCode());
    }
}
