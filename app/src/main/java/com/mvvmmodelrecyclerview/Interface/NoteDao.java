package com.mvvmmodelrecyclerview.Interface;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mvvmmodelrecyclerview.model.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);
    @Query("SELECT * FROM note_table")
    LiveData<List<Note>> getAllNotes();

}
