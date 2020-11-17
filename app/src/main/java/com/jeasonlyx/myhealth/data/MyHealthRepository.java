package com.jeasonlyx.myhealth.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Calendar;
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

    /*
    * This method takes a current calendar object and decide to reset checklists on database
    * based on their frequency:
    * Day: Always reset(daily)
    * Week: Reset on Monday(Weekly)
    * Month: Reset on first day of each month(Monthly)
    * */
    public void resetCompletedOnDate(Calendar calendar){
        new resetCompletedOnDate(myHealthDao).execute(calendar);
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
            String selected = strings[0];
            /* // For menu use, add frequency and unfinished
            HashSet<String> set_category = new HashSet<>();
            HashMap<String, String> map_frequency = new HashMap<>();
            //HashSet<String> set_completion = new HashSet<>();

            set_category.add("Default");
            set_category.add("Medicine");
            set_category.add("Diet");
            set_category.add("Exercise");

            map_frequency.put("Daily", "Day");
            map_frequency.put("Weekly", "Week");
            map_frequency.put("Monthly", "Month");

            if(set_category.contains(selected))
                return myHealthDao.getCategorizedChecklist(selected);
            else if(map_frequency.containsKey(selected))
                return myHealthDao.getFrequencyChecklist(map_frequency.get(selected));

            return myHealthDao.getToBeCompletedChecklist();
            */
            return myHealthDao.getCategorizedChecklist(selected);
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

    private static class resetCompletedOnDate extends AsyncTask<Calendar, Void, Void>{

        private MyHealthDao myHealthDao;

        public resetCompletedOnDate(MyHealthDao myHealthDao) {
            this.myHealthDao = myHealthDao;
        }

        @Override
        protected Void doInBackground(Calendar... calendars) {

            Calendar calendar = calendars[0];
            // first day of each month, clear the monthly completed
            if(calendar.get(Calendar.DAY_OF_MONTH) == 1){
                myHealthDao.resetMonthCompleted();
            }

            // Monday set the weekly completed to 0
            if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
                myHealthDao.resetWeekCompleted();
            }

            // EveryDay set Day completed to 0
            myHealthDao.resetDayCompleted();
            return null;
        }
    }

}
