package com.stackmobile.fit_journey.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity_log",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class ActivityLog implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "userId")
    private int userId;
    @ColumnInfo(name = "minutes_of_activity")
    private String minutesOfActivity;
    @ColumnInfo(name = "water_consumption")
    private String waterConsumption;
    @ColumnInfo(name = "sleep_time_interval")
    private String sleepTimeInterval;
    @ColumnInfo(name = "meal_description")
    private String mealDescription;

    public ActivityLog() {}

    protected ActivityLog(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        minutesOfActivity = in.readString();
        waterConsumption = in.readString();
        sleepTimeInterval = in.readString();
        mealDescription = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(minutesOfActivity);
        dest.writeString(waterConsumption);
        dest.writeString(sleepTimeInterval);
        dest.writeString(mealDescription);
    }

    public static final Parcelable.Creator<ActivityLog> CREATOR = new Parcelable.Creator<ActivityLog>() {
        @Override
        public ActivityLog createFromParcel(Parcel in) {
            return new ActivityLog(in);
        }

        @Override
        public ActivityLog[] newArray(int size) {
            return new ActivityLog[size];
        }
    };

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getMinutesOfActivity() {
        return minutesOfActivity;
    }
    public void setMinutesOfActivity(String minutesOfActivity) {
        this.minutesOfActivity = minutesOfActivity;
    }
    public String getWaterConsumption() {
        return waterConsumption;
    }
    public void setWaterConsumption(String waterConsumption) {
        this.waterConsumption = waterConsumption;
    }
    public String getSleepTimeInterval() {
        return sleepTimeInterval;
    }
    public void setSleepTimeInterval(String sleepTimeInterval) {
        this.sleepTimeInterval = sleepTimeInterval;
    }
    public String getMealDescription() {
        return mealDescription;
    }
    public void setMealDescription(String mealDescription) {
        this.mealDescription = mealDescription;
    }
}
