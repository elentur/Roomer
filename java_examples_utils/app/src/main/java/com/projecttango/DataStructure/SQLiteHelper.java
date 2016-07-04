package com.projecttango.DataStructure;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by
 * Roberto on 04.07.16.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    /**
     * DATABASE
     */
    private static final String DATABASE_NAME = "roomer.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * TABLES
     */

    public static final String TABLE_POINTS = "points";
    public static final String TABLE_EDGES = "edges";
    public static final String TABLE_ADFS = "adfs";
    public static final String TABLE_BUILDINGS = "buildings";

    /**
     * COLUMNS ADFS
     */
    public static final String BUILDINGS_COLUMN_ID = "_id";
    public static final String BUILDINGS_COLUMN_NAME = "name";

    // Database creation sql statement
    private static final String BUILDINGS_CREATE = "create table " + TABLE_BUILDINGS
            + "( " + BUILDINGS_COLUMN_ID + " integer primary key autoincrement,"
            + BUILDINGS_COLUMN_NAME + " TEXT not null"
            + ");";

    /**
     * COLUMNS ADFS
     */
    public static final String ADFS_COLUMN_ID = "_id";
    public static final String ADFS_COLUMN_X = "position_x";
    public static final String ADFS_COLUMN_Y = "position_y";
    public static final String ADFS_COLUMN_Z = "position_z";
    public static final String ADFS_COLUMN_NAME = "name";
    public static final String ADFS_COLUMN_BUILDING = "building_id";

    // Database creation sql statement
    private static final String ADFS_CREATE = "create table " + TABLE_ADFS
            + "( " + ADFS_COLUMN_ID + " integer primary key autoincrement,"
            + ADFS_COLUMN_X + " REAL,"
            + ADFS_COLUMN_Y + " REAL,"
            + ADFS_COLUMN_Z + " REAL,"
            + ADFS_COLUMN_NAME + " TEXT not null,"
            + ADFS_COLUMN_BUILDING + " integer not null,"
            + "FOREIGN KEY(" + ADFS_COLUMN_BUILDING + ") REFERENCES " + TABLE_BUILDINGS + "(" + BUILDINGS_COLUMN_ID + ")"
            + ");";

    /**
     * COLUMNS POINS
     */
    public static final String POINTS_COLUMN_ID = "_id";
    public static final String POINTS_COLUMN_X = "position_x";
    public static final String POINTS_COLUMN_Y = "position_y";
    public static final String POINTS_COLUMN_Z ="position_z";
    public static final String POINTS_COLUMN_TAG ="tag";
    public static final String POINTS_COLUMN_PROPERTIES ="properties";
    public static final String POINTS_COLUMN_ADF = "adf_id";
    public static final String POINTS_COLUMN_BUILDING = "building_id";

    // Database creation sql statement
    private static final String POINTS_CREATE = "create table " + TABLE_POINTS
            + "( " + POINTS_COLUMN_ID + " integer primary key autoincrement, "
            + POINTS_COLUMN_X + " REAL not null,"
            + POINTS_COLUMN_Y + " REAL not null,"
            + POINTS_COLUMN_Z + " REAL not null,"
            + POINTS_COLUMN_TAG + " TEXT not null,"
            + POINTS_COLUMN_PROPERTIES + " TEXT,"
            + POINTS_COLUMN_ADF + " integer not null,"
            + POINTS_COLUMN_BUILDING + " integer not null,"
            + "FOREIGN KEY(" + POINTS_COLUMN_ADF + ") REFERENCES " + TABLE_ADFS + "(" + BUILDINGS_COLUMN_ID + "),"
            + "FOREIGN KEY(" + POINTS_COLUMN_BUILDING + ") REFERENCES " + TABLE_BUILDINGS + "(" + ADFS_COLUMN_ID + ")"
            + ");";

    /**
     * COLUMNS EDGES
     */
    public static final String EDGES_COLUMN_POINT_ID = "point_id";
    public static final String EDGES_COLUMN_NEIGHBOUR_ID = "neighbour_id";

    // Database creation sql statement
    private static final String EDGES_CREATE = "create table " + TABLE_EDGES
            + "( " + EDGES_COLUMN_POINT_ID + " integer not null,"
            + EDGES_COLUMN_NEIGHBOUR_ID + " integer not null,"
            + "PRIMARY KEY("+ EDGES_COLUMN_POINT_ID +", "+ EDGES_COLUMN_NEIGHBOUR_ID +"),"
            + "FOREIGN KEY(" + EDGES_COLUMN_POINT_ID + ") REFERENCES " + TABLE_POINTS + "(" + POINTS_COLUMN_ID + "),"
            + "FOREIGN KEY(" + EDGES_COLUMN_NEIGHBOUR_ID + ") REFERENCES " + TABLE_POINTS + "(" + POINTS_COLUMN_ID + ")"
            + ");";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(POINTS_CREATE);
        db.execSQL(EDGES_CREATE);
        db.execSQL(ADFS_CREATE);
        db.execSQL(BUILDINGS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADFS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDINGS);

        onCreate(db);
    }
}
