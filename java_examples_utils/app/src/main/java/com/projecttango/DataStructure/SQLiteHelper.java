package com.projecttango.DataStructure;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Creates the database, set tables and columns. Manage the update and delete of the database
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
    public static final String BUILDINGS_COLUMN_ID =  "_id";
    public static final String BUILDINGS_COLUMN_NAME = "name";

    // Database creation sql statement
    private static final String BUILDINGS_CREATE = "create table " + TABLE_BUILDINGS
            + "( " + BUILDINGS_COLUMN_ID + " integer primary key autoincrement,"
            + BUILDINGS_COLUMN_NAME + " TEXT not null UNIQUE ON CONFLICT REPLACE"
            + ");";

    /**
     * COLUMNS ADFS
     */
    public static final String ADFS_COLUMN_ID = "_id";
    public static final String ADFS_COLUMN_X = "position_x";
    public static final String ADFS_COLUMN_Y = "position_y";
    public static final String ADFS_COLUMN_Z = "position_z";
    public static final String ADFS_COLUMN_NAME = "name";
    public static final String ADFS_COLUMN_UUID = "uuid";
    public static final String ADFS_COLUMN_BUILDING = "building_id";

    // Database creation sql statement
    private static final String ADFS_CREATE = "create table " + TABLE_ADFS
            + "( " + ADFS_COLUMN_ID + " integer primary key autoincrement,"
            + ADFS_COLUMN_X + " REAL,"
            + ADFS_COLUMN_Y + " REAL,"
            + ADFS_COLUMN_Z + " REAL,"
            + ADFS_COLUMN_NAME + " TEXT not null,"
            + ADFS_COLUMN_UUID + " TEXT not null UNIQUE ON CONFLICT FAIL,"
            + ADFS_COLUMN_BUILDING + " integer not null,"
            + "FOREIGN KEY(" + ADFS_COLUMN_BUILDING + ") REFERENCES " + TABLE_BUILDINGS + "(" + BUILDINGS_COLUMN_ID + ") ON DELETE CASCADE"
            + ");";

    /**
     * COLUMNS POINTS
     */
    public static final String POINTS_COLUMN_ID = "_id";
    public static final String POINTS_COLUMN_X = "position_x";
    public static final String POINTS_COLUMN_Y = "position_y";
    public static final String POINTS_COLUMN_Z ="position_z";
    public static final String POINTS_COLUMN_TAG ="tag";
    public static final String POINTS_COLUMN_PROPERTIES ="properties";
    public static final String POINTS_COLUMN_ADF = "adf_id";

    // Database creation sql statement
    private static final String POINTS_CREATE = "create table " + TABLE_POINTS
            + "( " + POINTS_COLUMN_ID + " integer primary key autoincrement, "
            + POINTS_COLUMN_X + " REAL not null,"
            + POINTS_COLUMN_Y + " REAL not null,"
            + POINTS_COLUMN_Z + " REAL not null,"
            + POINTS_COLUMN_TAG + " TEXT not null,"
            + POINTS_COLUMN_PROPERTIES + " TEXT,"
            + POINTS_COLUMN_ADF + " integer not null,"
            + "UNIQUE ("+POINTS_COLUMN_X+", "+POINTS_COLUMN_Y+","+POINTS_COLUMN_Z+") ON CONFLICT FAIL,"
            + "FOREIGN KEY(" + POINTS_COLUMN_ADF + ") REFERENCES " + TABLE_ADFS + "(" + BUILDINGS_COLUMN_ID + ") ON DELETE CASCADE"
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
            + "FOREIGN KEY(" + EDGES_COLUMN_POINT_ID + ") REFERENCES " + TABLE_POINTS + "(" + POINTS_COLUMN_ID + ") ON DELETE CASCADE,"
            + "FOREIGN KEY(" + EDGES_COLUMN_NEIGHBOUR_ID + ") REFERENCES " + TABLE_POINTS + "(" + POINTS_COLUMN_ID + ") ON DELETE CASCADE"
            + ");";


    /**
     * Generates a SQLiteHelper Object and creates the database roomer.db with all tables.
     * On app uninstall removes the roomer.db with all tables
     * @param context
     */
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
    public void onOpen(SQLiteDatabase db) {
        // enables the foreign key cascading
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
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


    /**
     * Shows all fields of a database table
     * @param database of the table
     * @param table name
     * @param allColumns Array of all columns you are looking for.
     */
    public static void showTableContent(SQLiteDatabase database, String table, String[] allColumns ){

        Cursor cursor = database.query(table, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            for(int i=0; i< allColumns.length; i++) {
                Log.d("TABLE_"+ table, "" + cursor.getString(i));
            }

            cursor.moveToNext();
        }

        cursor.close();
    }


}
