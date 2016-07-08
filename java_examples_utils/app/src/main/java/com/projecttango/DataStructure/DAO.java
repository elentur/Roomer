package com.projecttango.DataStructure;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;


/**
 * Created by
 * Roberto on 04.07.16.
 */
public abstract class DAO {

    protected SQLiteDatabase database;
    protected SQLiteHelper dbHelper;

    public DAO(Context context) {
        dbHelper = new SQLiteHelper(context);
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}
