package com.projecttango.DataStructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.rajawali3d.math.vector.Vector3;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.IllegalFormatException;
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

    public Point createPoint(Vector3 position, HashMap<String,Object> properties,String tag, ADF adf, Building building) {

        ContentValues values = setContentValues(position,properties,tag,adf,building);

        long insertId = database.insert(SQLiteHelper.TABLE_POINTS, null, values);

        Cursor cursor = database.query(SQLiteHelper.TABLE_POINTS, allColumns, SQLiteHelper.POINTS_COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();

        Point newPoint = cursorToPoint(cursor);

        cursor.close();

        return newPoint;
    }

    private ContentValues setContentValues(Vector3 position, HashMap<String,Object> properties,String tag, ADF adf, Building building) {

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.POINTS_COLUMN_X, position.x);
        values.put(SQLiteHelper.POINTS_COLUMN_Y, position.y);
        values.put(SQLiteHelper.POINTS_COLUMN_Z, position.z);
        values.put(SQLiteHelper.POINTS_COLUMN_TAG, tag);
        values.put(SQLiteHelper.POINTS_COLUMN_PROPERTIES, new JSONObject(properties).toString());
        values.put(SQLiteHelper.POINTS_COLUMN_ADF, adf.getId());
        values.put(SQLiteHelper.POINTS_COLUMN_BUILDING, building.getId());

        return values;
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

        HashMap result = new HashMap<String,Object>();

        try {
            result = new ObjectMapper().readValue(cursor.getString(5), HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        point.setProperties(result);

        return point;
    }

    public int updatePoint(Point point, ADF adf, Building building) {
        ContentValues values = setContentValues(point.getPosition(),point.getProperties(),point.getTag(),adf,building);
        return database.update(SQLiteHelper.TABLE_POINTS, values,SQLiteHelper.POINTS_COLUMN_ID + " = " + point.getId(),null );
    }

    public void setEdge(Point p, Point n){

        long p_id = p.getId();
        long n_id = n.getId();

        if(p_id <= 0 || n_id <= 0 ) throw new IllegalArgumentException("The point and/or the neighbour are not in the DB. Please inter them first!");

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.EDGES_COLUMN_POINT_ID, p_id);
        values.put(SQLiteHelper.EDGES_COLUMN_NEIGHBOUR_ID, n_id);

        long insertId = database.insert(SQLiteHelper.TABLE_EDGES, null, values);

        p.addNeighhbour(n);
    }

    public void deleteEdge(Point p, Point n){
        long p_id = p.getId();
        long n_id = n.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + p_id + " and " + n_id);
        database.delete(SQLiteHelper.TABLE_EDGES, SQLiteHelper.EDGES_COLUMN_POINT_ID + " = " + p_id + " AND " + SQLiteHelper.EDGES_COLUMN_NEIGHBOUR_ID + " = " + n_id, null);

        p.rmNeighbour(n);
    }

}
