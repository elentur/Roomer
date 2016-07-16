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
import java.util.Map;


/**
 * This class represents a Data Access Object.
 * It is a object-oriented interface between the Point and the databases.
 * Created by
 * Roberto on 04.07.16.
 */
public class PointsDataSource extends DAO{

    /**
     * array af all accessible table columns
     */
    private String[] allPointsColumns = {
            SQLiteHelper.POINTS_COLUMN_ID,
            SQLiteHelper.POINTS_COLUMN_X,
            SQLiteHelper.POINTS_COLUMN_Y,
            SQLiteHelper.POINTS_COLUMN_Z,
            SQLiteHelper.POINTS_COLUMN_TAG,
            SQLiteHelper.POINTS_COLUMN_PROPERTIES,
            SQLiteHelper.POINTS_COLUMN_ADF
    };

    /**
     * context of the activity for other inner database accesses.
     */
    Context context;

    /**
     * Instance of a BuildingsDataSource object.
     * @param context of the activity
     */
    public PointsDataSource(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Returns a Point object and makes a new entry in the database
     * @param position of the point
     * @param properties of the point
     * @param tag of the point (name)
     * @param adf of the point it belongs to
     * @return point object
     */
    public Point createPoint(Vector3 position, HashMap<PointProperties,PointProperties> properties,String tag, ADF adf) {

        ContentValues values = setContentValues(position,properties,tag,adf);

        Log.d("DEBUGGER", "values: "  + values);
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

    /**
     * Converts the the data in ContentValues.
     * Needed for the database column
     * @param position of the point
     * @param properties of the point
     * @param tag of the point (name)
     * @param adf of the point it belongs to
     * @return ContentValues
     */
    private ContentValues setContentValues(Vector3 position, HashMap<PointProperties,PointProperties> properties,String tag, ADF adf) {

        ContentValues values = new ContentValues();
        HashMap<String, String> propertiesToString = new HashMap<String, String>();
        for(Map.Entry<PointProperties,PointProperties> e : properties.entrySet()) {
            propertiesToString.put(e.getKey().name(),e.getValue().name());
        }
        values.put(SQLiteHelper.POINTS_COLUMN_X, position.x);
        values.put(SQLiteHelper.POINTS_COLUMN_Y, position.y);
        values.put(SQLiteHelper.POINTS_COLUMN_Z, position.z);
        values.put(SQLiteHelper.POINTS_COLUMN_TAG, tag);
        values.put(SQLiteHelper.POINTS_COLUMN_PROPERTIES, new JSONObject(propertiesToString).toString());
        values.put(SQLiteHelper.POINTS_COLUMN_ADF, adf.getId());

        return values;
    }

    /**
     * deletes a point from the database
     * @param point to delete
     */
    public void deletePoint(Point point) {
        long id = point.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_POINTS, SQLiteHelper.POINTS_COLUMN_ID + " = " + id, null);
    }

    /**
     * returns a list of all Points.
     * @return list of points
     */
    public ArrayList<Point> getAllPoints() {

        Cursor cursor = database.query(SQLiteHelper.TABLE_POINTS, allPointsColumns, null, null, null, null, null);

        return cursorLoop(cursor);
    }

    /**
     * returns a list of all Points from the given building.
     * @return list of points
     */
    public ArrayList<Point> getAllPoints(Building building) {

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

    /**
     * returns a list of all Points of the given ADF.
     * @return list of points
     */
    public ArrayList<Point> getAllPoints(ADF adf) {

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

    /**
     * converts the database query into a object
     * @param cursor of the database
     * @return list of points
     */
    private ArrayList<Point> cursorLoop(Cursor cursor){

        ArrayList<Point> points = new ArrayList<Point>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Point point = cursorToPoint(cursor);
            points.add(point);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        setAllEdgesToPoint(points);
        return points;
    }

    /**
     * converts the database query into a object and set all neighbour and properties
     * @param cursor of the database
     * @return point
     */
    private Point cursorToPoint(Cursor cursor) {
        Point point = null;
        //Log.d("DEBUGGER", "Cursercount: " +cursor.getCount());
        if(cursor.getCount() > 0) {
            point = new Point();
            point.setId(cursor.getLong(0));
            point.setPosition(new Vector3(cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3)));
            point.setTag(cursor.getString(4));
            HashMap<String, Object> result=null;

            try {
                result = new ObjectMapper().readValue(cursor.getString(5), HashMap.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HashMap<PointProperties,PointProperties> props = new HashMap<PointProperties, PointProperties>();
            if(result!= null)
                for(Map.Entry<String,Object> e: result.entrySet()){
                    props.put(PointProperties.valueOf(e.getKey()), PointProperties.valueOf((String)e.getValue()));
                }
            point.setProperties(props);

            ADFDataSource adfDao = new ADFDataSource(context);

            ADF adf = adfDao.getADF(cursor.getLong(6));
            point.setNeighbours(new HashMap<Point, Double>());
            point.setAdf(adf);
        }

        return point;
    }

    /**
     * updates the point in the database
     * @param point to update
     * @return int with the changed row id
     */
    public int updatePoint(Point point) {

        ContentValues values = setContentValues(point.getPosition(),point.getProperties(),point.getTag(),point.getAdf());
        return database.update(SQLiteHelper.TABLE_POINTS, values,SQLiteHelper.POINTS_COLUMN_ID + " = " + point.getId(),null );
    }

    /**
     * Creates a new neighbour between 2 points and saves it in the database
     * @param points list array
     */
    public void setAllEdgesToPoint(ArrayList<Point> points) {

        for(Point p : points) {
            HashMap<Point, Double> edges = new HashMap<Point, Double>();

            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

            qb.setTables(SQLiteHelper.TABLE_POINTS +
                    " LEFT OUTER JOIN " + SQLiteHelper.TABLE_EDGES + " ON " +
                    SQLiteHelper.POINTS_COLUMN_ID + " = " + SQLiteHelper.EDGES_COLUMN_NEIGHBOUR_ID);

            qb.appendWhere(SQLiteHelper.EDGES_COLUMN_POINT_ID + " = " + p.getId());

            Cursor cursor = qb.query(dbHelper.getReadableDatabase(), allPointsColumns, null, null, null, null, null);

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Point neighbour = cursorToPoint(cursor);
                int index = points.indexOf(neighbour);

                if(index > -1) {
                    p.addNeighbour(points.get(index));
                }
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }
    }

}
