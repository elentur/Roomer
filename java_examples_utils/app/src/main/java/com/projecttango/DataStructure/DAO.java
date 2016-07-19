package com.projecttango.DataStructure;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;


/**
 * The DAO class initiate the DB connection.
 * It also Starts the SQLHelper for accessing to the table names and columns.
 * Created by
 * Roberto on 04.07.16.
 */
public abstract class DAO {

    /**
     * methods to manage a SQLite database
     */
    protected SQLiteDatabase database;
    /**
     * Helper for the table and column name access
     */
    protected SQLiteHelper dbHelper;

    /**
     * initialize the SQLiteHelper and opens the the database
     * @param context from activity
     */
    public DAO(Context context) {
        dbHelper = new SQLiteHelper(context);
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the database
     *
     * @throws SQLException if there is no Writable Database
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * close the database
     */
    public void close() {
        dbHelper.close();
    }
}
