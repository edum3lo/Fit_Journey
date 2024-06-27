package com.stackmobile.fitjourney.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.stackmobile.fitjourney.database.entities.Notification;

import java.util.List;

@Dao
public interface NotificationDAO {
    @Insert
    void insert(Notification notification);
    @Update
    void update(Notification notification);
    @Delete
    void delete(Notification notification);
    @Query("SELECT * FROM notification WHERE id = :id")
    Notification getNotificationById(int id);
    @Query("SELECT * FROM notification")
    List<Notification> getAllNotifications();
}
