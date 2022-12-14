package hu.boga.music.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.google.common.base.Objects;
import hu.boga.music.midi.MidiEngine;

public class Track implements Cloneable {
    private int counter;
    private int id = counter++;
    private String name;

    private Map<Integer, List<Note>> trackMap = new HashMap<>();
    private TrackSettings settings = TrackSettings.defaultSettings();

    static Map<Integer, List<Note>> copiedNotes;

    public Map<Integer, List<Note>> getTrackMap() {
        return trackMap;
    }

    public List<Note> getNotesAtTick(int tick) {
        List<Note> retVal = trackMap.get(tick);
        if (retVal == null) {
            retVal = new ArrayList<>();
            trackMap.put(tick, retVal);
        }
        return retVal;
    }

    public void removeNote(Note n) {
        trackMap.values().forEach(l -> l.remove(n));

    }

    public void moveNoteToTick(Note n, int tick) {
        this.removeNote(n);
        this.addNote(tick, n);
    }

    public void copy() {
        Map<Integer, List<Note>> map = new HashMap<>();
        for (Entry<Integer, List<Note>> entry : trackMap.entrySet()) {
            List<Note> sn = this.getCopyOfSelectedNotes(entry.getValue());
            if (!sn.isEmpty()) {
                map.put(entry.getKey(), sn);
            }
        }
        this.copiedNotes = map;
    }

    public void paste() {
        copiedNotes.forEach((t, list) -> {
            list.forEach(note -> {
                this.addNote(t + 32, note);
            });
        });
    }

    public TrackSettings getSettings() {
        return settings;
    }

    public void setSettings(TrackSettings settings) {
        this.settings = settings;
    }

    public void setTrackMap(Map<Integer, List<Note>> trackMap) {
        this.trackMap = trackMap;
    }

    @Override
    public Track clone() {
        Track t = new Track();
        t.setSettings(new TrackSettings(this.settings));
        t.setTrackMap(this.cloneNotes());
        return t;
    }

    @JsonIgnore
    public Integer getFirstEmptyTick() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        trackMap.forEach((tick, list) -> {
            Optional<Note> longest = getLongestNoteAtTick(tick);
            if (longest.isPresent()) {
                int t = tick + longest.get().getLength().getErtek();
                if (t >= atomicInteger.get()) {
                    atomicInteger.set(t);
                }
            }

        });
        return atomicInteger.get();
    }

    public void selectAll() {
        this.trackMap.forEach((tick, notes) -> {
            notes.forEach(n -> n.setSelected(true));
        });
    }

    // refaktor a settingsbe
    public void unSelectAll() {
        this.trackMap.forEach((tick, notes) -> {
            notes.forEach(n -> n.setSelected(false));
        });
    }

    public void clearAllNotes() {
        this.trackMap = new HashMap<>();
    }

    public void removeSelected() {
        this.trackMap.forEach((tick, notes) -> notes.removeIf(Note::isSelected));

    }

    public void shiftSelected(int octavesToShift) {
        this.trackMap.forEach((tick, notes) -> notes.stream().filter(Note::isSelected).forEach(n -> n.setPitch(n.getPitch().shift(octavesToShift))));

    }

    @JsonIgnore
    public Integer getFirstEmptyMeasureTick() {
        int tick = getFirstEmptyTick();
        int measureNum = (int) Math.ceil(tick / 32d);
        return measureNum * 32;
    }


//    public int getNotesTick(Note note) {
//        AtomicInteger atomicInteger = new AtomicInteger();
//        this.trackMap.forEach((tick, list) -> {
//            if (list.contains(note)) atomicInteger.set(tick);
//        });
//        return atomicInteger.get();
//    }

    private void addNote(int tick, Note n) {
        List<Note> notes = trackMap.get(tick);
        if (notes == null) {
            notes = new ArrayList<>();
            trackMap.put(tick, notes);
        }
        if (!notes.contains(n)) {
            notes.add(n);
        }
    }

    private Map<Integer, List<Note>> cloneNotes() {
        Map<Integer, List<Note>> ret = new HashMap<>();
        this.trackMap.entrySet().forEach(entry -> {
            List<Note> l = entry.getValue().stream().map(Note::new).collect(Collectors.toList());
            ret.put(entry.getKey(), l);
        });
        return ret;
    }

    private List<Note> getCopyOfSelectedNotes(List<Note> notes) {
        return notes.stream().filter(Note::isSelected).map(Note::new).collect(Collectors.toList());
    }

    private Optional<Note> getLongestNoteAtTick(int tick) {
        if (this.getNotesAtTick(tick) != null) {
            return this.getNotesAtTick(tick).stream().max((n1, n2) -> n2.length.compareTo(n1.length));
        }
        return Optional.empty();
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return id == track.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
