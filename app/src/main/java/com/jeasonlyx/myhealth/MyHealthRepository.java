package com.jeasonlyx.myhealth;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyHealthRepository {

    private MyHealthDao myHealthDao;
    private LiveData<List<Checklist>> allChecklist;
    

    public MyHealthRepository(Application application){
        MyHealthDatabase database = MyHealthDatabase.getInstance(application);
        myHealthDao = database.getMyHealthDao(); // abstract method in Database class
        allChecklist = myHealthDao.getAllChecklist();
    }

    // need to run in background
    public void insert(Checklist checklist){
        new InsertChecklistAsyncTask(myHealthDao).execute(checklist);
    }

    public void update(Checklist checklist){
        new UpdateChecklistAsyncTask(myHealthDao).execute(checklist);
    }

    public void delete(Checklist checklist){
        new DeleteChecklistAsyncTask(myHealthDao).execute(checklist);
    }

    public void deleteAllChecklist(){
        new DeleteAllChecklistAsyncTask(myHealthDao).execute();
    }

    public LiveData<List<Checklist>> getAllChecklist(){
        return allChecklist;
    }

    public List<Checklist> getCategorizedChecklist(String category){
        List<Checklist> categorizedChecklist = new ArrayList<>();
        
        try {
            categorizedChecklist = new GetCategorizedChecklistAsyncTask(myHealthDao).execute(category).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return categorizedChecklist;
    }

    // return 0 for unique, 1 for duplication
    public  int checkNameUniqueness(String name){
        int unique = 0;
        try {
            unique = new checkNameUniqueness(myHealthDao).execute(name).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return unique;
    }


    // static to prevent memory leak
    // Void is capital not same as normal return type
    private static class InsertChecklistAsyncTask extends AsyncTask<Checklist, Void, Void> {

        private MyHealthDao myHealthDao;

        public InsertChecklistAsyncTask(MyHealthDao myHealthDao) {
            this.myHealthDao = myHealthDao;
        }

        @Override
        protected Void doInBackground(Checklist... checklists) {
            myHealthDao.insert(checklists[0]);
            return null; // have to return null in response to Void capital
        }
    }

    private static class UpdateChecklistAsyncTask extends AsyncTask<Checklist, Void, Void> {

        private MyHealthDao myHealthDao;

        public UpdateChecklistAsyncTask(MyHealthDao myHealthDao) {
            this.myHealthDao = myHealthDao;
        }

        @Override
        protected Void doInBackground(Checklist... checklists) {
            myHealthDao.update(checklists[0]);
            return null; // have to return null in response to Void capital
        }
    }

    private static class DeleteChecklistAsyncTask extends AsyncTask<Checklist, Void, Void> {

        private MyHealthDao myHealthDao;

        public DeleteChecklistAsyncTask(MyHealthDao myHealthDao) {
            this.myHealthDao = myHealthDao;
        }

        @Override
        protected Void doInBackground(Checklist... checklists) {
            myHealthDao.delete(checklists[0]);
            return null; // have to return null in response to Void capital
        }
    }

    private static class DeleteAllChecklistAsyncTask extends AsyncTask<Void, Void, Void> {

        private MyHealthDao myHealthDao;

        public DeleteAllChecklistAsyncTask(MyHealthDao myHealthDao) {
            this.myHealthDao = myHealthDao;
        }

        @Override
        protected Void doInBackground(Void... Voids) {
            myHealthDao.deleteAllChecklist();
            return null; // have to return null in response to Void capital
        }
    }

    private static class GetCategorizedChecklistAsyncTask extends
            AsyncTask<String, Void, List<Checklist>> {

        private MyHealthDao myHealthDao;

        public GetCategorizedChecklistAsyncTask(MyHealthDao myHealthDao) {
            this.myHealthDao = myHealthDao;
        }

        @Override
        protected List<Checklist> doInBackground(String... strings) {
            return myHealthDao.getCategorizedChecklist(strings[0]);
        }
    }

    private static class checkNameUniqueness extends AsyncTask<String, Void, Integer> {

        private MyHealthDao myHealthDao;

        public checkNameUniqueness(MyHealthDao myHealthDao) {
            this.myHealthDao = myHealthDao;
        }


        @Override
        protected Integer doInBackground(String... strings) {
            return myHealthDao.checkNameUniqueness(strings[0]);
        }
    }



}
