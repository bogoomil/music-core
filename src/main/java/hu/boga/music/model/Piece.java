package hu.boga.music.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Piece {

    private String name = "";
    private int tempo = 120;

    List<Track> tracks = new ArrayList<>();

    public void addTrack(Track t) {
        this.tracks.add(t);
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void removeTrack(Track t) {
        this.tracks.remove(t);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    @JsonIgnore
    public int getMaxTick() {
        return this.tracks.stream().map(t -> t.getFirstEmptyTick()).max(Integer::compareTo).orElse(0);
    }

}
