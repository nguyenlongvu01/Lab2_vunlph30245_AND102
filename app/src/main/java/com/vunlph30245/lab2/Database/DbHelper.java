package com.vunlph30245.lab2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(@Nullable Context context) {
        super(context, "TodoDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE TASK(ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, CONTENT TEXT, DATE TEXT, TYPE TEXT, STATUS INTEGER)";
        db.execSQL(sql);

        String sql_Insert = "INSERT INTO TASK (TITLE, CONTENT, DATE, TYPE, STATUS) VALUES " +
                "('Hoc Java', 'Hoc lap trinh android', '10/01/2024', 'De', 1), " +
                "('Hoc Python', 'Hoc lap trinh python', '10/01/2024', 'De', 1), " +
                "('Hoc kotlin', 'Hoc lap trinh android kotlin', '10/01/2024', 'De', 1), " +
                "('Hoc php', 'Hoc lap trinh php', '10/01/2024', 'De', 1);";
        db.execSQL(sql_Insert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TASK");
        onCreate(db);
    }
}
