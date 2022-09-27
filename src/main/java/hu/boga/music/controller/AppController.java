package hu.boga.music.controller;

import java.util.List;

import hu.boga.music.midi.MidiEngine;
import hu.boga.music.model.Note;
import hu.boga.music.model.Piece;
import hu.boga.music.model.Track;

public class AppController {

    Piece piece;
    int currentTrackIndex;

    public AppController() {
        piece = new Piece();
        piece.addTrack(new Track());
    }

    public void playNotes(List<Note> notes, int program, int tempo) {
        MidiEngine.playNotes(program,
                tempo,
                notes.toArray(new Note[notes.size()]));
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getCurrentTrackIndex() {
        return currentTrackIndex;
    }

    public void setCurrentTrackIndex(int currentTrack) {
        this.currentTrackIndex = currentTrack;
    }

//    public void addNotesToCurrentTrack(List<Note> notes) {
//        Track t = this.piece.getTracks().get(currentTrackIndex);
//
//        if (t.getSettings().isPattern()) {
//            notes.forEach(n -> {
//                t.generatePattern(n);
//            });
//        } else {
//            t.addNotes(notes);
//        }
//
//    }

    public Track getCurrentTrack() {
        return this.piece.getTracks().get(currentTrackIndex);
    }

    public void setCurrentTrack(Track t2) {
        this.currentTrackIndex = this.piece.getTracks().indexOf(t2);

    }

    private int getMaxLength(List<Note> notes) {
        return notes.stream().map(n -> n.getLength().getErtek()).max(Integer::compareTo).orElse(0);
    }

}
