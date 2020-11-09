package com.jeasonlyx.myhealth;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyHealthDao {

    @Insert
    void insert(Checklist checklist);

    @Update
    void update(Checklist checklist);

    @Delete
    void delete(Checklist checklist);

    @Query("DELETE FROM checklist_table")
    void deleteAllChecklist();

    @Query("SELECT * FROM checklist_table ORDER BY name ASC")
    LiveData<List<Checklist>> getAllChecklist();  // LiveData allows observe

    @Query("SELECT * FROM checklist_table WHERE category = :category ORDER BY name ASC")
    List<Checklist> getCategorizedChecklist(String category);  // LiveData allows observe

    @Query("SELECT COUNT(*) FROM checklist_TABLE WHERE name = :name")
    int checkNameUniqueness(String name);


}
