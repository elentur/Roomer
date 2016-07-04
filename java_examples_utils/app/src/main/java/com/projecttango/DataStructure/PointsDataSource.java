package com.projecttango.DataStructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.json.JSONObject;
import org.rajawali3d.math.vector.Vector3;

import java.sql.SQLException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

/**
 * Created by roberto on 04.07.16.
 */
public class PointsDataSource extends DAO{

    private String[] allColumns = { 
            SQLiteHelper.POINTS_COLUMN_ID,
            SQLiteHelper.POINTS_COLUMN_X,
            SQLiteHelper.POINTS_COLUMN_Y,
            SQLiteHelper.POINTS_COLUMN_Z,
            SQLiteHelper.POINTS_COLUMN_TAG,
            SQLiteHelper.POINTS_COLUMN_PROPERTIES,
            SQLiteHelper.POINTS_COLUMN_ADF,
            SQLiteHelper.POINTS_COLUMN_BUILDING
    };

    public PointsDataSource(Context context) {
        super(context);
    }

    public Point createPoint(Vector3 position, HashMap properties,String tag, ADF adf, Building building) {

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.POINTS_COLUMN_X, position.x);
        values.put(SQLiteHelper.POINTS_COLUMN_Y, position.y);
        values.put(SQLiteHelper.POINTS_COLUMN_Z, position.z);
        values.put(SQLiteHelper.POINTS_COLUMN_TAG, tag);
        values.put(SQLiteHelper.POINTS_COLUMN_PROPERTIES, new JSONObject(properties).toString());
        values.put(SQLiteHelper.POINTS_COLUMN_ADF, adf.getId());
        values.put(SQLiteHelper.POINTS_COLUMN_BUILDING, building.getId());

        long insertId = database.insert(SQLiteHelper.TABLE_POINTS, null, values);

        Cursor cursor = database.query(SQLiteHelper.TABLE_POINTS, allColumns, SQLiteHelper.POINTS_COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();

        Point newPoint = cursorToPoint(cursor);

        cursor.close();

        return newPoint;
    }


    public void deletePoint(Point point) {
        long id = point.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_POINTS, SQLiteHelper.POINTS_COLUMN_ID + " = " + id, null);
    }

    public List<Point> getAllPoints() {
        List<Point> points = new ArrayList<Point>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_POINTS, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Point point = cursorToPoint(cursor);
            points.add(point);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return points;
    }

    private Point cursorToPoint(Cursor cursor) {

        Log.d("DEBUGGER", "" + cursor);
        Point point = new Point();
        point.setId(cursor.getLong(0));
        point.setPosition(new Vector3(cursor.getDouble(1),cursor.getDouble(2),cursor.getDouble(3)));
        point.setTag(cursor.getString(4));

        return point;
    }
}
