package com.jeasonlyx.myhealth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyHealthViewModel extends AndroidViewModel {

    private MyHealthRepository repository;
    private LiveData<List<Checklist>> allChecklist;


    public MyHealthViewModel(@NonNull Application application) {
        super(application);

        repository = new MyHealthRepository(application);
        allChecklist = repository.getAllChecklist();
    }

    // need to run in background
    public void insert(Checklist checklist){
        repository.insert(checklist);
    }

    public void update(Checklist checklist){
        repository.update(checklist);
    }

    public void delete(Checklist checklist){
        repository.delete(checklist);
    }

    public void deleteAllChecklist(){
        repository.deleteAllChecklist();
    }

    public LiveData<List<Checklist>> getAllChecklist(){
        return allChecklist;
    }

    public List<Checklist> getCategorizedChecklist(String category){
        return repository.getCategorizedChecklist(category);
    }

    public int checkNameUniqueness(String name){
        return repository.checkNameUniqueness(name);
    }
}
