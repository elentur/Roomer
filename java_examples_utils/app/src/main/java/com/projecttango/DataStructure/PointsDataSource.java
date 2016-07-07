package com.projecttango.DataStructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.rajawali3d.math.vector.Vector3;

import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

/**
 * Created by roberto on 04.07.16.
 */
public class PointsDataSource extends DAO{

    private String[] allPointsColumns = {
            SQLiteHelper.POINTS_COLUMN_X,
            SQLiteHelper.POINTS_COLUMN_Y,
            SQLiteHelper.POINTS_COLUMN_Z,
            SQLiteHelper.POINTS_COLUMN_TAG,
            SQLiteHelper.POINTS_COLUMN_PROPERTIES,
            SQLiteHelper.POINTS_COLUMN_ADF
    };

    Context context;

    public PointsDataSource(Context context) {
        super(context);
        this.context = context;
    }

    public Point createPoint(Vector3 position, HashMap<String,Object> properties,String tag, ADF adf) {

        ContentValues values = setContentValues(position,properties,tag,adf);

        long insertId = database.insert(SQLiteHelper.TABLE_POINTS, null, values);

        Cursor cursor = database.query(
                SQLiteHelper.TABLE_POINTS,
                allPointsColumns,
                SQLiteHelper.POINTS_COLUMN_ID + "=?",
                new String[] { "" + insertId },
                null,
                null,
                null);

        cursor.moveToFirst();

        Point newPoint = cursorToPoint(cursor);

        cursor.close();

        return newPoint;
    }

    private ContentValues setContentValues(Vector3 position, HashMap<String,Object> properties,String tag, ADF adf) {

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.POINTS_COLUMN_X, position.x);
        values.put(SQLiteHelper.POINTS_COLUMN_Y, position.y);
        values.put(SQLiteHelper.POINTS_COLUMN_Z, position.z);
        values.put(SQLiteHelper.POINTS_COLUMN_TAG, tag);
        values.put(SQLiteHelper.POINTS_COLUMN_PROPERTIES, new JSONObject(properties).toString());
        values.put(SQLiteHelper.POINTS_COLUMN_ADF, adf.getId());

        return values;
    }


    public void deletePoint(Point point) {
        long id = point.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_POINTS, SQLiteHelper.POINTS_COLUMN_ID + " = " + id, null);
    }

    public List<Point> getAllPoints() {

        Cursor cursor = database.query(SQLiteHelper.TABLE_POINTS, allPointsColumns, null, null, null, null, null);

        return cursorLoop(cursor);
    }

    public List<Point> getAllPoints(Building building) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(SQLiteHelper.TABLE_POINTS +
                " LEFT OUTER JOIN " + SQLiteHelper.TABLE_ADFS + " ON " +
                SQLiteHelper.POINTS_COLUMN_ADF + " = " + SQLiteHelper.ADFS_COLUMN_ID);

        qb.appendWhere(SQLiteHelper.ADFS_COLUMN_BUILDING + " = " + building.getId());

        Cursor cursor = qb.query(
                dbHelper.getReadableDatabase(),
                allPointsColumns,
                null,
                null,
                null,
                null,
                null
        );

        return cursorLoop(cursor);
    }

    public List<Point> getAllPoints(ADF adf) {
        Cursor cursor = database.query(
                SQLiteHelper.TABLE_POINTS,
                allPointsColumns,
                SQLiteHelper.POINTS_COLUMN_ADF + "=?",
                new String[] { "" + adf.getId() },
                null,
                null,
                null
        );

        return cursorLoop(cursor);
    }

    private List<Point> cursorLoop(Cursor cursor){
        List<Point> points = new ArrayList<Point>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Point point = cursorToPoint(cursor);
            points.add(point);
            setAllEdgesToPoint(point);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return points;
    }

    private Point cursorToPoint(Cursor cursor) {
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

        ADFDataSource adfDao = new ADFDataSource(context);

        ADF adf = adfDao.getADF(cursor.getLong(6));

        point.setAdf(adf);

        return point;
    }

    public int updatePoint(Point point) {
        ContentValues values = setContentValues(point.getPosition(),point.getProperties(),point.getTag(),point.getAdf());
        return database.update(SQLiteHelper.TABLE_POINTS, values,SQLiteHelper.POINTS_COLUMN_ID + " = " + point.getId(),null );
    }

    public void setAllEdgesToPoint(Point p) {

        HashMap<Point, Double> edges = new HashMap<Point, Double>();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(SQLiteHelper.TABLE_POINTS +
                " LEFT OUTER JOIN " + SQLiteHelper.TABLE_EDGES + " ON " +
                SQLiteHelper.POINTS_COLUMN_ID + " = " + SQLiteHelper.EDGES_COLUMN_NEIGHBOUR_ID);

        qb.appendWhere(SQLiteHelper.EDGES_COLUMN_POINT_ID + " = " + p.getId());

        Cursor cursor = qb.query(dbHelper.getReadableDatabase(), allPointsColumns, null, null, null, null, null );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Point neighbour = cursorToPoint(cursor);
            p.addNeighhbour(neighbour);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
    }

}
