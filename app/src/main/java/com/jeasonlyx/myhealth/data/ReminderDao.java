package com.jeasonlyx.myhealth.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert
    void insert(Reminder reminder);

    @Update
    void update(Reminder reminder);

    @Delete
    void delete(Reminder reminder);

    //@Query("DELETE FROM reminder_table")
    //void deleteAllReminder();

    @Query("SELECT * FROM reminder_table ORDER BY name ASC")
    LiveData<List<Reminder>> getAllReminder();  // LiveData allows observe

    @Query("SELECT * FROM reminder_table WHERE name = :name")
    List<Reminder> getReminderOfName(String name);  // LiveData allows observe

    @Query("SELECT COUNT(*) FROM reminder_table WHERE name = :name AND time = :time")
    int checkReminderUniqueness(String name, int time);
}
