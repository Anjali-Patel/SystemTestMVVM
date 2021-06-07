package com.mvvmmodelrecyclerview.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.mvvmmodelrecyclerview.Repository.NoteRepository;
import com.mvvmmodelrecyclerview.model.Employee;
import com.mvvmmodelrecyclerview.model.Note;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private final NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }
    public void insert(Note note) {
        repository.insert(note);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
    public LiveData<List<Employee>> getAllEmployee() {
        return repository.getMutableLiveData();
    }
}
