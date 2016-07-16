package com.projecttango.DataStructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;

/**
 * This class represents a Data Access Object.
 * It is a object-oriented interface between the Building and the databases.
 * Created by
 * Roberto on 04.07.16.
 */
public class BuildingsDataSource extends DAO{

    /**
     * array af all accessible table columns
     */
    private String[] allColumns = {
            SQLiteHelper.BUILDINGS_COLUMN_ID,
            SQLiteHelper.BUILDINGS_COLUMN_NAME
    };

    /**
     * Instance of a BuildingsDataSource object.
     * @param context from the activity
     */
    public BuildingsDataSource(Context context) {
        super(context);
    }

    /**
     * Returns a Building object and makes a new entry in the database
     * @param name of the building
     * @return Building object
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
     * deletes a Building from the database
     * @param building to delete
     */
    public void deleteBuilding(Building building) {
        long id = building.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_BUILDINGS, SQLiteHelper.BUILDINGS_COLUMN_ID + " = " + id, null);
    }

    /**
     * returns a list of all Buildings.
     * @return list of buildings
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
     * returns a single building according of the given id
     * returns null if not exist
     * @param id of the building
     * @return building object
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
     * returns a single building according of the given name
     * returns null if not exist
     * @param name of the building
     * @return building object
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
     * converts the database query into a object
     * @param cursor of the database
     * @return Building
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
