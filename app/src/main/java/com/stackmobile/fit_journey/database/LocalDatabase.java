package com.stackmobile.fit_journey.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.stackmobile.fit_journey.database.DAO.ActivityLogDAO;
import com.stackmobile.fit_journey.database.DAO.ImageDAO;
import com.stackmobile.fit_journey.database.DAO.NotificationDAO;
import com.stackmobile.fit_journey.database.DAO.ReminderDAO;
import com.stackmobile.fit_journey.database.DAO.UserDAO;
import com.stackmobile.fit_journey.database.entities.ActivityLog;
import com.stackmobile.fit_journey.database.entities.Image;
import com.stackmobile.fit_journey.database.entities.Notification;
import com.stackmobile.fit_journey.database.entities.Reminder;
import com.stackmobile.fit_journey.database.entities.User;
import com.stackmobile.fit_journey.database.converters.DateConverter;

@Database(entities = {User.class, Reminder.class, Notification.class, ActivityLog.class, Image.class}, version = 2)
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
    public abstract ImageDAO imageModel();
}
