package com.projecttango.DataStructure;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by
 * roberto on 23.06.16.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "roomer.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Point, Integer> mPointDao = null;
    private Dao<Point2Point, Integer> mPoint2PointDao = null;

    private static DatabaseHelper sDatabaseHelper;

    public static DatabaseHelper getInstance() {
        if(sDatabaseHelper == null) throw new IllegalAccessError("DatabaseHelper has to be initialized as an object before makeing a static call!");
        return sDatabaseHelper;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        sDatabaseHelper = this;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        // creats a new table
        try {
            TableUtils.createTable(connectionSource, Point.class);
            TableUtils.createTable(connectionSource, Point2Point.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Point.class, true);
            TableUtils.dropTable(connectionSource, Point2Point.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Point */

    public Dao<Point, Integer> getPointDao() throws SQLException {
        if (mPointDao == null) {
            mPointDao = getDao(Point.class);
        }

        return mPointDao;
    }

     /* Point2Point */

    public Dao<Point2Point, Integer> getPoint2PointDao() throws SQLException {
        if (mPoint2PointDao == null) {
            mPoint2PointDao = getDao(Point2Point.class);
        }

        return mPoint2PointDao;
    }

    @Override
    public void close() {
        mPointDao = null;
        mPoint2PointDao = null;
        super.close();
    }
}
