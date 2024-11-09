package com.vunlph30245.lab2.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vunlph30245.lab2.Database.DbHelper;
import com.vunlph30245.lab2.Model.SanPhamModel;

import java.util.ArrayList;

public class ToDoDAO {
    private DbHelper dbHelper;
    private SQLiteDatabase database;

    public ToDoDAO(Context context) {
        dbHelper = new DbHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    public long addTodo(SanPhamModel sanPhamModel) {
        ContentValues values = new ContentValues();
        values.put("TITLE", sanPhamModel.getTitle());
        values.put("CONTENT", sanPhamModel.getContent());
        values.put("DATE", sanPhamModel.getDate());
        values.put("TYPE", sanPhamModel.getType());
        values.put("STATUS", sanPhamModel.getStatus());

        return database.insert("TASK", null, values);
    }


    public long updateTodo(SanPhamModel sanPhamModel) {
        ContentValues values = new ContentValues();
        values.put("TITLE", sanPhamModel.getTitle());
        values.put("CONTENT", sanPhamModel.getContent());
        values.put("DATE", sanPhamModel.getDate());
        values.put("TYPE", sanPhamModel.getType());
        values.put("STATUS", sanPhamModel.getStatus());

        String whereClause = "ID = ?";
        String[] whereArgs = {String.valueOf(sanPhamModel.getId())};

        return database.update("TASK", values, whereClause, whereArgs);
    }


    public ArrayList<SanPhamModel> getListSanPham() {
        ArrayList<SanPhamModel> list = new ArrayList<>();
        database = dbHelper.getReadableDatabase();

        try {
            Cursor cursor = database.rawQuery("SELECT * FROM TASK", null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));
                    String content = cursor.getString(cursor.getColumnIndexOrThrow("CONTENT"));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
                    String type = cursor.getString(cursor.getColumnIndexOrThrow("TYPE"));
                    int status = cursor.getInt(cursor.getColumnIndexOrThrow("STATUS"));

                    SanPhamModel sanPhamModel = new SanPhamModel(id, title, content, date, type, status);
                    list.add(sanPhamModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public boolean removeTodo(int id) {
        int row = database.delete("TASK", "ID=?", new String[]{String.valueOf(id)});
        return row > 0;
    }


    public boolean updateStatus(int id, boolean isChecked) {
        int status = isChecked ? 1 : 0;
        ContentValues values = new ContentValues();
        values.put("STATUS", status);
        int row = database.update("TASK", values, "ID=?", new String[]{String.valueOf(id)});
        return row > 0;
    }

}
