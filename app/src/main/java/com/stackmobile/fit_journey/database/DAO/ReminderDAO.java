package com.stackmobile.fit_journey.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.stackmobile.fit_journey.database.entities.Reminder;

import java.util.List;

@Dao
public interface ReminderDAO {
    @Insert
    void insert(Reminder reminder);
    @Update
    void update(Reminder reminder);
    @Delete
    void delete(Reminder reminder);
    @Query("SELECT * FROM reminder WHERE id = :id")
    Reminder getReminderById(int id);
    @Query("SELECT * FROM reminder WHERE userId = :userId")
    List<Reminder> getRemindersByUserId(int userId);
}
