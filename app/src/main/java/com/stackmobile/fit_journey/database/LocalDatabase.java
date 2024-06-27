package com.stackmobile.fitjourney.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.stackmobile.fitjourney.database.DAO.ActivityLogDAO;
import com.stackmobile.fitjourney.database.DAO.NotificationDAO;
import com.stackmobile.fitjourney.database.DAO.ReminderDAO;
import com.stackmobile.fitjourney.database.DAO.UserDAO;
import com.stackmobile.fitjourney.database.entities.ActivityLog;
import com.stackmobile.fitjourney.database.entities.Notification;
import com.stackmobile.fitjourney.database.entities.Reminder;
import com.stackmobile.fitjourney.database.entities.User;
import com.stackmobile.fitjourney.database.converters.DateConverter;

@Database(entities = {User.class, Reminder.class, Notification.class, ActivityLog.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class LocalDatabase extends RoomDatabase {
    private static LocalDatabase INSTANCE;

    public static LocalDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class, "DB_FITJourney")
                    .addMigrations()
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public abstract UserDAO userModel();
    public abstract NotificationDAO notificationModel();
    public abstract ReminderDAO reminderModel();
    public abstract ActivityLogDAO activityLogModel();
}
