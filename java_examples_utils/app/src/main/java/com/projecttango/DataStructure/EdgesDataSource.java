package com.projecttango.DataStructure;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * This class represents a Data Access Object.
 * It is a object-oriented interface between the Edges and the databases.
 * Created by
 * Roberto on 06.07.16.
 */
public class EdgesDataSource extends DAO{

    /**
     * context of the activity for other inner database accesses.
     */
    private Context context;

    /**
     * array af all accessible table columns
     */
    private String[] allEdgesColumns = {
            SQLiteHelper.EDGES_COLUMN_POINT_ID,
            SQLiteHelper.EDGES_COLUMN_NEIGHBOUR_ID
    };

    /**
     * Instance of a BuildingsDataSource object.
     * @param context of the activity
     */
    public EdgesDataSource(Context context) {
        super(context);
        this.context = context;
    }


    /**
     * Makes a new entry in the database.
     * @param p source point
     * @param n destination point
     */
    public void createEdge(Point p, Point n){

        long p_id = p.getId();
        long n_id = n.getId();

        if(p_id <= 0 || n_id <= 0 ) throw new IllegalArgumentException("The point and/or the neighbour are not in the DB. Please inter them first!");

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.EDGES_COLUMN_POINT_ID, p_id);
        values.put(SQLiteHelper.EDGES_COLUMN_NEIGHBOUR_ID, n_id);

        long insertId = database.insert(SQLiteHelper.TABLE_EDGES, null, values);

        p.addNeighbour(n);
    }

    /**
     * Deletes a edge from the database
     * @param p source point
     * @param n destination point
     */
    public void deleteEdge(Point p, Point n){
        long p_id = p.getId();
        long n_id = n.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + p_id + " and " + n_id);
        database.delete(SQLiteHelper.TABLE_EDGES, SQLiteHelper.EDGES_COLUMN_POINT_ID + " = " + p_id + " AND " + SQLiteHelper.EDGES_COLUMN_NEIGHBOUR_ID + " = " + n_id, null);

        p.rmNeighbour(n);
    }

    /**
     * Deletes all edges from the database which belongs to the given point
     * @param p source point
     */
    public void deleteEdges(Point p){
        long p_id = p.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + p_id);
        database.delete(SQLiteHelper.TABLE_EDGES, SQLiteHelper.EDGES_COLUMN_POINT_ID + " = " + p_id, null);

        p.setNeighbours(new HashMap<Point,Double>());
    }
}
