package com.projecttango.DataStructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.projecttango.tangoutils.R;

import org.rajawali3d.math.vector.Vector3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Marcus Bätz on 24.05.2016.
 * This Class is for loading, saving, import and export of roomer databases including NavPoints
 */
public class RoomerDB extends SQLiteOpenHelper {

    private static final String TAG = RoomerDB.class.getSimpleName();

    private final String adf;
    public final String CREATE_TABLE;
    private boolean isCreating;

    public RoomerDB(Context context, String adf) {
        super(context, "roomer_" + adf + ".db", null, 1);
        this.adf = adf;
        CREATE_TABLE = "CREATE TABLE Points (ID INTEGER PRIMARY KEY, " +
                "ISNAV STRING, TAG TEXT, POSX REAL, POSY REAL, POSZ REAL, NEIGHBOURS TEXT);";
        isCreating = true;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        } catch (SQLiteException e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }

    /**
     * Adds a Point to the Database
     *
     * @param p The point that has to be added to added
     */
    public void insert(Point p) {

        try {

            SQLiteDatabase db = getWritableDatabase();
            if (isCreating) {
                db.execSQL("DROP TABLE IF EXISTS Points");
                onCreate(db);
                isCreating = false;
            }
            ContentValues ct = new ContentValues();
            ct.put("ID", p.getID());
            ct.put("ISNAV", (p instanceof NavigationPoint) + "");
            ct.put("TAG", p.getTag());
            ct.put("POSX", p.getPosition().x);
            ct.put("POSY", p.getPosition().y);
            ct.put("POSZ", p.getPosition().z);
            db.insert("Points", null, ct);

        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Updates the neighbour relationship for each  point in the database
     *
     * @param list A list of Points that have to be updated
     */
    public void update(ArrayList<Point> list) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            for (Point p : list) {
                int id = p.hashCode();
                String idN = "";
                for (Point n : p.getNeighbours().keySet()) {
                    idN = idN + n.getID() + ";";
                }
                ContentValues ct = new ContentValues();
                ct.put("NEIGHBOURS", idN);
                db.update("Points", ct, "ID=" + id, null);
            }
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Exports the Database to a shared space
     *
     * @param context
     */
    public void exportDB(Context context) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/Exam Creator");

        if (!direct.exists()) {
            if (direct.mkdir()) {
                Log.d("DEBUGGER", "Directory is created");
            }
        }
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/" + context.getPackageName() + "/databases/roomer_" + adf + ".db";
                String backupDBPath = "/roomerDBBackups/roomer_" + adf + ".db";

                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                Log.d("DEBUGGER", "BackupPath: " + backupDB.toString());
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context, backupDB.toString(),
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG)
                    .show();
            Log.e(TAG, e.toString());

        }
    }

    public void clearDB() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DROP TABLE Points");
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    /**
     * Imports the Database for given ADF from shared space
     *
     * @param context
     */
    public void importDB(Context context) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/Exam Creator");

        if (!direct.exists()) {
            if (direct.mkdir()) {
                Log.d("DEBUGGER", "Directory is created");
            }
        }

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/" + context.getPackageName() + "/databases/roomer_" + adf + ".db";
                String backupDBPath = "/roomerDBBackups/roomer_" + adf + ".db";
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context, backupDB.toString(),
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Returns an ArrayList with all points
     *
     * @return ArrayList of Points
     */
    public ArrayList<Point> loadPoints() throws Exception {
        ArrayList<Point> points = new ArrayList<Point>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("select * from Points", null);
            if (c.moveToFirst()) {

                while (!c.isAfterLast()) {
                    int ID = c.getInt(c.getColumnIndex("ID"));
                    String isNav = c.getString(c.getColumnIndex("ISNAV"));
                    String tag = c.getString(c.getColumnIndex("TAG"));
                    double posx = c.getDouble(c.getColumnIndex("POSX"));
                    double posy = c.getDouble(c.getColumnIndex("POSY"));
                    double posz = c.getDouble(c.getColumnIndex("POSZ"));
                    Point point;
                    if (isNav.equals("true")) {
                        point = new NavigationPoint(ID,new Vector3(posx, posy, posz), null, tag);
                    } else {
                        point = new DestinationPoint(ID,new Vector3(posx, posy, posz), null, tag);
                    }
                    points.add(point);
                    c.moveToNext();
                }
            }

            for (Point point : points) {
                int id = point.getID();
                // get all neighbours from specific point
                Cursor c1 = db.rawQuery("select NEIGHBOURS from Points where ID ='" + id + "' ", null);
                if(c1.moveToFirst()) {
                    String n = c1.getString(c1.getColumnIndex("NEIGHBOURS"));
                    // tranform str to array
                    String[] ids = n.split(";");
                    for (String s : ids) {
                        try {
                            int i = Integer.parseInt(s);
                            for (Point neighbour : points) {
                                // if the neighbour has the same id like one of the neighbours we are looking for....
                                if(neighbour.getID() == i) point.addNeighhbour(neighbour);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
            }
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage());
        }
        return points;
    }
}
