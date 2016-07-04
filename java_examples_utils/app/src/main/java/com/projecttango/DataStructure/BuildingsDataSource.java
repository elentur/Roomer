package com.projecttango.DataStructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 * Roberto on 04.07.16.
 */
public class BuildingsDataSource extends DAO{

    private String[] allColumns = {
            SQLiteHelper.BUILDINGS_COLUMN_ID,
            SQLiteHelper.BUILDINGS_COLUMN_NAME
    };


    public BuildingsDataSource(Context context) {
        super(context);
    }

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


    public void deleteBuilding(Building building) {
        long id = building.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_BUILDINGS, SQLiteHelper.BUILDINGS_COLUMN_ID + " = " + id, null);
    }

    public List<Building> getAllPoints() {
        List<Building> buildings = new ArrayList<Building>();

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

    private Building cursorToBuilding(Cursor cursor) {

        Log.d("DEBUGGER", "" + cursor);
        Building building = new Building();
        building.setId(cursor.getLong(0));
        building.setName(cursor.getString(1));
        return building;
    }
}
