package hu.boga.music.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hu.boga.music.enums.NoteLength;
import hu.boga.music.theory.Pitch;

public class Note implements Cloneable {

    Pitch pitch;
    NoteLength length;

    boolean selected;

    private static int counter;

    private int id;

    public Note() {
        this.id = counter;
        counter++;
    }

    public Note(Note noteToCopy) {
        this();
        this.length = noteToCopy.getLength();
        this.pitch = noteToCopy.getPitch();
        this.selected = noteToCopy.isSelected();
    }

    public Pitch getPitch() {
        return pitch;
    }

    public void setPitch(Pitch pitch) {
        this.pitch = pitch;
    }

    public NoteLength getLength() {
        return length;
    }

    public void setLength(NoteLength length) {
        this.length = length;
    }

    @JsonIgnore
    public int getMidiCode() {
        return this.pitch.getMidiCode();
    }

//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + id;
//        return result;
//    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Note other = (Note) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    @JsonIgnore
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void setCounter(int counter) {
        Note.counter = counter;
    }

    @JsonIgnore
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void toggleSelection() {
        this.selected = !selected;

    }

    public void incrementLength() {
        this.length = this.length.next();
    }

    public void decrementLength() {
        this.length = this.length.prev();

    }

}
