package com.jeasonlyx.myhealth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jeasonlyx.myhealth.data.Checklist;
import com.jeasonlyx.myhealth.data.MyHealthRepository;
import com.jeasonlyx.myhealth.data.Reminder;
import com.jeasonlyx.myhealth.data.ReminderRepository;

import java.util.Calendar;
import java.util.List;

public class MyHealthViewModel extends AndroidViewModel {

    private MyHealthRepository repository;
    private LiveData<List<Checklist>> allChecklist;

    private ReminderRepository reminderRepository;
    private LiveData<List<Reminder>> allReminders;


    public MyHealthViewModel(@NonNull Application application) {
        super(application);

        repository = new MyHealthRepository(application);
        allChecklist = repository.getAllChecklist();

        reminderRepository = new ReminderRepository(application);
        allReminders = reminderRepository.getAllReminder();
    }

    /*          Checklist Section                     */
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

    public void resetCompletedOnDate(Calendar calendar) { repository.resetCompletedOnDate(calendar);}


    /*           Reminder Section           */

    public void insert(Reminder reminder){
        reminderRepository.insert(reminder);
    }

    public void update(Reminder reminder){
        reminderRepository.update(reminder);
    }

    public void delete(Reminder reminder){
        reminderRepository.delete(reminder);
    }

    public LiveData<List<Reminder>> getAllReminder(){
        return allReminders;
    }

    public List<Reminder> getReminderOfName(String name){
        return reminderRepository.getReminderOfName(name);
    }

    // return 0 for unique, 1 for duplication
    public  int checkReminderUniqueness(Reminder reminder){
        return reminderRepository.checkReminderUniqueness(reminder);
    }
}
