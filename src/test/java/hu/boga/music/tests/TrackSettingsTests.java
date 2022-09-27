package hu.boga.music.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

import hu.boga.music.enums.NoteLength;
import hu.boga.music.model.TrackSettings;

public class TrackSettingsTests {

    @Test
    void setters_values_ok() {
        TrackSettings s = new TrackSettings();
        s.setMidiChannel(1);
        assertEquals(1, s.getMidiChannel());

        s.setVolume(100);
        assertEquals(100, s.getVolume());


        s.setRgb(1000);
        assertEquals(1000, s.getRgb());

        int rgb = Color.BLACK.getRGB();
        s.setRgb(rgb);
        assertEquals(rgb, s.getColor().getRGB());

        s.setGapLength(NoteLength.DUPLA);
        assertEquals(NoteLength.DUPLA, s.getGapLength());

        s.setPattern(true);
        assertTrue(s.isPattern());

    }
}
