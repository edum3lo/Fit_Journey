package com.stackmobile.fit_journey.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.stackmobile.fit_journey.database.entities.Image;

import java.util.List;

@Dao
public interface ImageDAO {
    @Insert
    void insert(Image image);
    @Update
    void update(Image image);
    @Delete
    void delete(Image image);
    @Query("SELECT * FROM image WHERE id = :id")
    Image getImageById(int id);
    @Query("SELECT * FROM image WHERE userId = :userId")
    List<Image> getImagesByUserId(int userId);
}
