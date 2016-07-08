package com.projecttango.DataStructure;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by
 * Roberto on 06.07.16.
 */
public class EdgesDataSource extends DAO{

    /**
     *
     */
    private Context context;

    /**
     *
     */
    private String[] allEdgesColumns = {
            SQLiteHelper.EDGES_COLUMN_POINT_ID,
            SQLiteHelper.EDGES_COLUMN_NEIGHBOUR_ID
    };

    /**
     *
     * @param context
     */
    public EdgesDataSource(Context context) {
        super(context);
        this.context = context;
    }


    /**
     *
     * @param p
     * @param n
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
     *
     * @param p
     * @param n
     */
    public void deleteEdge(Point p, Point n){
        long p_id = p.getId();
        long n_id = n.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + p_id + " and " + n_id);
        database.delete(SQLiteHelper.TABLE_EDGES, SQLiteHelper.EDGES_COLUMN_POINT_ID + " = " + p_id + " AND " + SQLiteHelper.EDGES_COLUMN_NEIGHBOUR_ID + " = " + n_id, null);

        p.rmNeighbour(n);
    }

    /**
     *
     * @param p
     */
    public void deleteEdges(Point p){
        long p_id = p.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + p_id);
        database.delete(SQLiteHelper.TABLE_EDGES, SQLiteHelper.EDGES_COLUMN_POINT_ID + " = " + p_id, null);

        p.setNeighbours(new HashMap<Point,Double>());
    }
}
