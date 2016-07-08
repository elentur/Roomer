package com.projecttango.DataStructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;

/**
 * Created by
 * Roberto on 04.07.16.
 */
public class BuildingsDataSource extends DAO{

    /**
     *
     */
    private String[] allColumns = {
            SQLiteHelper.BUILDINGS_COLUMN_ID,
            SQLiteHelper.BUILDINGS_COLUMN_NAME
    };

    /**
     *
     * @param context
     */
    public BuildingsDataSource(Context context) {
        super(context);
    }

    /**
     *
     * @param name
     * @return
     */
    public Building createBuilding(String name) {

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.BUILDINGS_COLUMN_NAME, name);

        long insertId = database.insert(SQLiteHelper.TABLE_BUILDINGS, null, values);

        Cursor cursor = database.query(SQLiteHelper.TABLE_BUILDINGS, allColumns, SQLiteHelper.BUILDINGS_COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();

        Building newBuilding = cursorToBuilding(cursor);

        cursor.close();

        return newBuilding;
    }


    /**
     *
     * @param building
     */
    public void deleteBuilding(Building building) {
        long id = building.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_BUILDINGS, SQLiteHelper.BUILDINGS_COLUMN_ID + " = " + id, null);
    }

    /**
     *
     * @return
     */
    public ArrayList<Building> getAllBuildings() {
        ArrayList<Building> buildings = new ArrayList<Building>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_BUILDINGS, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Building building = cursorToBuilding(cursor);
            buildings.add(building);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return buildings;
    }

    /**
     *
     * @param id
     * @return
     */
    public Building getBuilding(long id){
        Building b;

        Cursor cursor = database.query(
                SQLiteHelper.TABLE_BUILDINGS,
                allColumns,
                SQLiteHelper.BUILDINGS_COLUMN_ID + "=?",
                new String[] { "" + id },
                null,
                null,
                null
        );

        cursor.moveToFirst();
        b = cursorToBuilding(cursor);
        cursor.close();
        return b;
    }

    /**
     *
     * @param name
     * @return
     */
    public Building getBuilding(String name){
        Building b;

        Cursor cursor = database.query(
                SQLiteHelper.TABLE_BUILDINGS,
                allColumns,
                SQLiteHelper.BUILDINGS_COLUMN_NAME + "=?",
                new String[] { name },
                null,
                null,
                null
        );

        cursor.moveToFirst();
        b = cursorToBuilding(cursor);
        cursor.close();
        return b;
    }

    /**
     *
     * @param cursor
     * @return
     */
    private Building cursorToBuilding(Cursor cursor) {
        Building building = null;
        if(cursor.getCount() > 0) {
            building = new Building();
            building.setId(cursor.getLong(0));
            building.setName(cursor.getString(1));
        }
        return building;
    }

    /**
     * Returns the number of rows with the given name
     * @param name of the building
     * @return int number of rows
     */
    public int rowNum(String name){

        Cursor cursor = database.query(
                SQLiteHelper.TABLE_BUILDINGS,
                new String[] { SQLiteHelper.BUILDINGS_COLUMN_ID },
                SQLiteHelper.BUILDINGS_COLUMN_NAME + "=?",
                new String[] { name },
                null,
                null,
                null
        );

        return cursor.getCount();
    }
}
