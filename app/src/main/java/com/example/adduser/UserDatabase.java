package com.example.adduser;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class},version = 2)
public abstract class UserDatabase extends RoomDatabase {

    static Migration migrationFrom1to2 = new Migration(1,2) {//1 là version cũ,2 là version mới
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            // thêm 1 trường dữ liệu "year" vào trong database "user"
            database.execSQL("ALTER TABLE user ADD COLUMN year TEXT");
        }
    };

    public static final String DATABASE_NAME = "user.db";
    public static UserDatabase instance;
    public static synchronized UserDatabase getInstance(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext()
                    ,UserDatabase.class
                    ,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .addMigrations(migrationFrom1to2)
                    .build();
        }
        return instance;
    }

    public abstract UserDAO userDAO();
}
