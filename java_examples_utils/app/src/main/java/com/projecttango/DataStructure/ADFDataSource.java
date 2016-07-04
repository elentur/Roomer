package com.projecttango.DataStructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 
 * Roberto on 04.07.16.
 */
public class ADFDataSource extends DAO{

    public ADFDataSource(Context context) {
        super(context);
    }

    private String[] allColumns = {
            SQLiteHelper.ADFS_COLUMN_ID,
            SQLiteHelper.ADFS_COLUMN_X,
            SQLiteHelper.ADFS_COLUMN_Y,
            SQLiteHelper.ADFS_COLUMN_Z,
            SQLiteHelper.ADFS_COLUMN_NAME,
            SQLiteHelper.ADFS_COLUMN_BUILDING
    };

    public ADF createADF(String name, Building building) {
        return createADF(new Vector3(0,0,0), name, building);
    }

    public ADF createADF(Vector3 position, String name, Building building) {

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.ADFS_COLUMN_X, position.x);
        values.put(SQLiteHelper.ADFS_COLUMN_Y, position.y);
        values.put(SQLiteHelper.ADFS_COLUMN_Z, position.z);
        values.put(SQLiteHelper.ADFS_COLUMN_NAME, name);
        values.put(SQLiteHelper.ADFS_COLUMN_BUILDING, building.getId());

        long insertId = database.insert(SQLiteHelper.TABLE_ADFS, null, values);

        Cursor cursor = database.query(SQLiteHelper.TABLE_ADFS, allColumns, SQLiteHelper.ADFS_COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();

        ADF newADF = cursorToADF(cursor);

        cursor.close();

        return newADF;
    }


    public void deleteADF(ADF ADF) {
        long id = ADF.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_ADFS, SQLiteHelper.ADFS_COLUMN_ID + " = " + id, null);
    }

    public List<ADF> getAllPoints() {
        List<ADF> ADFs = new ArrayList<ADF>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_ADFS, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ADF ADF = cursorToADF(cursor);
            ADFs.add(ADF);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return ADFs;
    }

    private ADF cursorToADF(Cursor cursor) {

        Log.d("DEBUGGER", "" + cursor);
        ADF ADF = new ADF();
        ADF.setId(cursor.getLong(0));
        ADF.setPosition(new Vector3(cursor.getDouble(1),cursor.getDouble(2),cursor.getDouble(3)));
        ADF.setName(cursor.getString(4));
        return ADF;
    }
}
