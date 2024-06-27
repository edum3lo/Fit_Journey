package com.stackmobile.fit_journey.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.stackmobile.fit_journey.database.entities.ActivityLog;

import java.util.List;

@Dao
public interface ActivityLogDAO {
    @Insert
    void insert(ActivityLog activityLog);
    @Update
    void update(ActivityLog activityLog);
    @Delete
    void delete(ActivityLog activityLog);
    @Query("SELECT * FROM activity_log WHERE id = :id")
    ActivityLog getActivityLogById(int id);
    @Query("SELECT * FROM activity_log WHERE userId = :userId")
    List<ActivityLog> getActivityLogsByUserId(int userId);
    @Query("SELECT * FROM activity_log")
    List<ActivityLog> getAllActivityLogs();
}
