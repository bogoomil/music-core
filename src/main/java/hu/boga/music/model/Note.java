package hu.boga.music.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.google.common.base.Objects;
import hu.boga.music.enums.NoteLength;
import hu.boga.music.theory.Pitch;

import java.util.UUID;

public class Note implements Cloneable {

    Pitch pitch;
    NoteLength length;
    int velocity;

    boolean selected;

    private UUID id = UUID.randomUUID();

    public Note(){

    }

    public Note(Note noteToCopy) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equal(id, note.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @JsonIgnore
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
