package com.jeasonlyx.myhealth.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReminderRepository {

    private ReminderDao reminderDao;
    private LiveData<List<Reminder>> allReminders;


    public ReminderRepository(Application application){
        MyHealthDatabase database = MyHealthDatabase.getInstance(application);
        reminderDao = database.getReminderDao(); // abstract method in Database class
        allReminders = reminderDao.getAllReminder();
    }

    // need to run in background
    public void insert(Reminder reminder){
        new InsertReminderAsyncTask(reminderDao).execute(reminder);
    }

    public void update(Reminder reminder){
        new UpdateReminderAsyncTask(reminderDao).execute(reminder);
    }

    public void delete(Reminder reminder){
        new DeleteReminderAsyncTask(reminderDao).execute(reminder);
    }

    //public void deleteAllReminder(){
    //    new DeleteAllReminderAsyncTask(reminderDao).execute();
    //}

    public LiveData<List<Reminder>> getAllReminder(){
        return allReminders;
    }

    public List<Reminder> getReminderOfName(String name){
        List<Reminder> reminderOfName = new ArrayList<>();
        
        try {
            reminderOfName = new GetReminderOfNameAsyncTask(reminderDao).execute(name).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return reminderOfName;
    }

    // return 0 for unique, 1 for duplication
    public  int checkReminderUniqueness(Reminder reminder){
        int unique = 0;
        try {
            unique = new checkReminderUniqueness(reminderDao).execute(reminder).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return unique;
    }


    // static to prevent memory leak
    // Void is capital not same as normal return type
    private static class InsertReminderAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private ReminderDao reminderDao;

        public InsertReminderAsyncTask(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }

        @Override
        protected Void doInBackground(Reminder... reminders) {
            reminderDao.insert(reminders[0]);
            return null; // have to return null in response to Void capital
        }
    }

    private static class UpdateReminderAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private ReminderDao reminderDao;

        public UpdateReminderAsyncTask(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }

        @Override
        protected Void doInBackground(Reminder... reminders) {
            reminderDao.update(reminders[0]);
            return null; // have to return null in response to Void capital
        }
    }

    private static class DeleteReminderAsyncTask extends AsyncTask<Reminder, Void, Void> {

        private ReminderDao reminderDao;

        public DeleteReminderAsyncTask(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }

        @Override
        protected Void doInBackground(Reminder... reminders) {
            reminderDao.delete(reminders[0]);
            return null; // have to return null in response to Void capital
        }
    }

    /*private static class DeleteAllReminderAsyncTask extends AsyncTask<Void, Void, Void> {

        private ReminderDao reminderDao;

        public DeleteAllReminderAsyncTask(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }

        @Override
        protected Void doInBackground(Void... Voids) {
            reminderDao.deleteAllReminder();
            return null; // have to return null in response to Void capital
        }
    }*/

    // need change later
    private static class GetReminderOfNameAsyncTask extends
            AsyncTask<String, Void, List<Reminder>> {

        private ReminderDao reminderDao;

        public GetReminderOfNameAsyncTask(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }


        @Override
        protected List<Reminder> doInBackground(String... strings) {
            return reminderDao.getReminderOfName(strings[0]);
        }
    }

    private static class checkReminderUniqueness extends AsyncTask<Reminder, Void, Integer> {

        private ReminderDao reminderDao;

        public checkReminderUniqueness(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }


        @Override
        protected Integer doInBackground(Reminder... reminders) {
            return reminderDao.checkReminderUniqueness(reminders[0].getName(), reminders[0].getTime());
        }
    }



}
