package hu.boga.music.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import hu.boga.music.enums.NoteLength;
import hu.boga.music.midi.MidiEngine;

class MidiEngineTests {

    @Test
    void getTickIndexByMillis_millis0_tickIs0() {
        assertEquals(0, MidiEngine.getTickIndexByMillis(0L, 120));
    }

    @Test
    void getTickIndexByMillis_millis500_tickIs8() {
        assertEquals(8, MidiEngine.getTickIndexByMillis(500L, 120));
    }

    @Test
    void getTickLengthInMillis_length() {
        assertEquals(500 / 8d, MidiEngine.getTickLengthInMillis(120));
    }

    @Test
    void getShynth_Synth_isNotNull() {
        assertNotNull(MidiEngine.getSynth());
    }

    @Test
    void getSequencer_Sequencer_isNotNull() {
        assertNotNull(MidiEngine.getSequencer());
    }

    @Test
    void getNoteLengthInMs_length_is500() {
        assertEquals(500, MidiEngine.getNoteLenghtInMs(NoteLength.NEGYED, 120));
    }
}
