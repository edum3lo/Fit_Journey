package com.stackmobile.fitjourney.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.stackmobile.fitjourney.database.entities.User;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User findById(int id);
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User findByEmail(String email);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);
    @Update
    void update(User user);
    @Delete
    void delete(User user);
}
