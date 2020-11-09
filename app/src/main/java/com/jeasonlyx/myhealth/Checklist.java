package com.jeasonlyx.myhealth;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "checklist_table", indices = {@Index(value = "name", unique = true)})
public class Checklist {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private int times;

    private String frequency;

    private String category;

    private String note;

    public Checklist(String name, int times, String frequency, String category, String note) {
        this.name = name;
        this.times = times;
        this.frequency = frequency;
        this.category = category;
        this.note = note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTimes() {
        return times;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getCategory() {
        return category;
    }

    public String getNote() {
        return note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Checklist checklist = (Checklist) o;
        return times == checklist.times &&
                name.equals(checklist.name) &&
                frequency.equals(checklist.frequency) &&
                category.equals(checklist.category) &&
                Objects.equals(note, checklist.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, times, frequency, category, note);
    }
}
