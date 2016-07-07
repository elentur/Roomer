package com.projecttango.DataStructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import org.rajawali3d.math.vector.Vector3;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 
 * Roberto on 04.07.16.
 */
public class ADFDataSource extends DAO{

    /**
     *
     */
    private Context context;

    /**
     *
     * @param context
     */
    public ADFDataSource(Context context) {
        super(context);
        this.context = context;
    }

    /**
     *
     */
    private String[] allColumns = {
            SQLiteHelper.ADFS_COLUMN_ID,
            SQLiteHelper.ADFS_COLUMN_X,
            SQLiteHelper.ADFS_COLUMN_Y,
            SQLiteHelper.ADFS_COLUMN_Z,
            SQLiteHelper.ADFS_COLUMN_UUID,
            SQLiteHelper.ADFS_COLUMN_BUILDING
    };

    /**
     *
     * @param position a Vector3 coordinate
     * @param name of the ADF
     * @param building in where the ADF is situated
     * @return ADF object
     */
    public ADF createADF(Vector3 position, String name, Building building) {

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.ADFS_COLUMN_X, position.x);
        values.put(SQLiteHelper.ADFS_COLUMN_Y, position.y);
        values.put(SQLiteHelper.ADFS_COLUMN_Z, position.z);
        values.put(SQLiteHelper.ADFS_COLUMN_UUID, name);
        values.put(SQLiteHelper.ADFS_COLUMN_BUILDING, building.getId());

        long insertId = database.insert(SQLiteHelper.TABLE_ADFS, null, values);

        Cursor cursor = database.query(SQLiteHelper.TABLE_ADFS, allColumns, SQLiteHelper.ADFS_COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();

        ADF newADF = cursorToADF(cursor);

        cursor.close();

        return newADF;
    }

    /**
     *
     * @param ADF
     */
    public void deleteADF(ADF ADF) {
        long id = ADF.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_ADFS, SQLiteHelper.ADFS_COLUMN_ID + " = " + id, null);
    }

    /**
     *
     * @return
     */
    public List<ADF> getAllADFs() {
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

    /**
     *
     * @return
     */
    public List<ADF> getAllADFs(Building building) {
        List<ADF> ADFs = new ArrayList<ADF>();

        Cursor cursor = database.query(
                SQLiteHelper.TABLE_ADFS,
                allColumns,
                SQLiteHelper.ADFS_COLUMN_BUILDING + "=?",
                new String[] { "" + building.getId() },
                null,
                null,
                null
        );

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

    /**
     *
     * @param id
     * @return
     */
    public ADF getADF(long id){
        ADF adf;
        Cursor cursor = database.query(
                SQLiteHelper.TABLE_ADFS,
                allColumns,
                SQLiteHelper.ADFS_COLUMN_ID + "=" + id,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        adf = cursorToADF(cursor);
        cursor.close();
        return adf;
    }

    /**
     *
     * @param cursor
     * @return
     */
    private ADF cursorToADF(Cursor cursor) {
        ADF adf = new ADF();
        adf.setId(cursor.getLong(0));
        adf.setPosition(new Vector3(cursor.getDouble(1),cursor.getDouble(2),cursor.getDouble(3)));
        adf.setName(cursor.getString(4));

        BuildingsDataSource buildingDao = new BuildingsDataSource(context);
        Building b = buildingDao.getBuilding(cursor.getLong(5));
        adf.setBuilding(b);

        return adf;
    }
}
