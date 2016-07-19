package com.projecttango.DataStructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import org.rajawali3d.math.vector.Vector3;
import java.util.ArrayList;

/**
 * This class represents a Data Access Object.
 * It is a object-oriented interface between the ADF and the databases.
 * Created by 
 * Roberto on 04.07.16.
 */
public class ADFDataSource extends DAO{

    /**
     * the activity context
     */
    private Context context;

    /**
     * Instance of a ADFDataSource object.
     * @param context from the activity
     */
    public ADFDataSource(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * array af all accessible table columns
     */
    private String[] allColumns = {
            SQLiteHelper.ADFS_COLUMN_ID,
            SQLiteHelper.ADFS_COLUMN_X,
            SQLiteHelper.ADFS_COLUMN_Y,
            SQLiteHelper.ADFS_COLUMN_Z,
            SQLiteHelper.ADFS_COLUMN_NAME,
            SQLiteHelper.ADFS_COLUMN_UUID,
            SQLiteHelper.ADFS_COLUMN_BUILDING
    };

    /**
     * Retruns a ADF object and makes a new entry in the database
     * @param position a Vector3 coordinate
     * @param name of the ADF
     * @param uuid of the ADF
     * @param building in where the ADF is situated
     * @return ADF object
     */
    public ADF createADF(Vector3 position, String name, String uuid, Building building) {

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.ADFS_COLUMN_X, position.x);
        values.put(SQLiteHelper.ADFS_COLUMN_Y, position.y);
        values.put(SQLiteHelper.ADFS_COLUMN_Z, position.z);
        values.put(SQLiteHelper.ADFS_COLUMN_NAME, name);
        values.put(SQLiteHelper.ADFS_COLUMN_UUID, uuid);
        values.put(SQLiteHelper.ADFS_COLUMN_BUILDING, building.getId());

        long insertId = database.insert(SQLiteHelper.TABLE_ADFS, null, values);

        Cursor cursor = database.query(SQLiteHelper.TABLE_ADFS, allColumns, SQLiteHelper.ADFS_COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();

        ADF newADF = cursorToADF(cursor);

        cursor.close();

        return newADF;
    }

    /**
     * Deletes a ADF from the database.
     *
     * @param ADF which have to remove.
     */
    public void deleteADF(ADF ADF) {
        long id = ADF.getId();
        Log.d("DEBUGGER", "Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_ADFS, SQLiteHelper.ADFS_COLUMN_ID + " = " + id, null);
    }

    /**
     * gets all ADF form the database
     * @return a list o ADFs
     */
    public ArrayList<ADF> getAllADFs() {
        ArrayList<ADF> ADFs = new ArrayList<ADF>();

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
     * Returns all ADF form the database according of the given building it belongs to
     *
     * @param building where the ADFs belongs to.
     * @return a list o ADFs
     */
    public ArrayList<ADF> getAllADFs(Building building) {
        ArrayList<ADF> ADFs = new ArrayList<ADF>();

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
     * returns a singel ADF from database according to the given id.
     * returns null if not exist.
     * @param id from the ADF
     * @return ADF
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
     * Returns ADF object containing the uuid.
     * Returns null if not exist.
     * @param uuid is the uuid of the ADF
     * @return ADF
     */
    public ADF getADF(String uuid){
        ADF adf;
        Cursor cursor = database.query(
                SQLiteHelper.TABLE_ADFS,
                allColumns,
                SQLiteHelper.ADFS_COLUMN_UUID + "='" + uuid +"'",
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
     * converts the database query into a object
     * @param cursor of the database
     * @return ADF
     */
    private ADF cursorToADF(Cursor cursor) {
        ADF adf = null;

        if(cursor.getCount() > 0) {
            adf = new ADF();
            adf.setId(cursor.getLong(0));
            adf.setPosition(new Vector3(cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3)));
            adf.setName(cursor.getString(4));
            adf.setUuid(cursor.getString(5));

            BuildingsDataSource buildingDao = new BuildingsDataSource(context);
            Building b = buildingDao.getBuilding(cursor.getLong(6));
            adf.setBuilding(b);
        }

        return adf;
    }
}
