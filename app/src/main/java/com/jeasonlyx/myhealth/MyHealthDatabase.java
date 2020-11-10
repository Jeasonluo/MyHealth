package com.jeasonlyx.myhealth;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Checklist.class, Reminder.class}, version = 1, exportSchema = false)
public abstract class MyHealthDatabase extends RoomDatabase {

    private static MyHealthDatabase instance;

    // Room subclasses this abstract class, because we instances this class
    public abstract MyHealthDao getMyHealthDao();
    public abstract ReminderDao getReminderDao();


    public static synchronized MyHealthDatabase getInstance(Context context){
        if(instance == null){ // make a instance of this class if not existed
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyHealthDatabase.class, "my_health_database")
                    // delete old database and build new from scratch
                    .fallbackToDestructiveMigration()
                    // add callback to do something (add initial data)
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private MyHealthDao myHealthDao;
        private ReminderDao reminderDao;

        public PopulateDbAsyncTask(MyHealthDatabase db) {
            this.myHealthDao = db.getMyHealthDao();
            this.reminderDao = db.getReminderDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            myHealthDao.insert(new Checklist("Medicine 1", 1, "Day", "Medicine", "Notes 1"));
            myHealthDao.insert(new Checklist("Medicine 2", 2, "Week", "Diet", "Notes 2"));
            myHealthDao.insert(new Checklist("Medicine 3", 3, "Month", "Exercise", "Notes 3"));

            reminderDao.insert(new Reminder("Medicine 1", 930, 11302020, 1));
            reminderDao.insert(new Reminder("Medicine 2", 1135, 12012020, 2));
            reminderDao.insert(new Reminder("Medicine 3", 2359, 01152021, 3));

            return null;
        }
    }
}
