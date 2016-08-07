package com.projecttango.DataStructure;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.projecttango.Visualisation.ToastHandler;

import org.rajawali3d.Object3D;
import org.rajawali3d.math.vector.Vector3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * Created by Marcus Bätz on 24.05.2016.
 * This Class is for loading, saving, import and export of roomer databases including NavPoints
 */
public class RoomerDB {
    public static RoomerDB db = null;
    private static final String TAG = RoomerDB.class.getSimpleName();
    /**
     * All dao objects for the database interaction
     */
    private BuildingsDataSource buildingDao;
    private ADFDataSource adfDao;
    private PointsDataSource pointDao;
    private EdgesDataSource edgeDao;

    /**
     * Comfort building and adf object for easier database management
     */
    private Building building;
    private ADF adf;

    /**
     * Creates a object of RoomerDB to access to the database methods
     *
     * @param context for the database
     */
    public RoomerDB(Context context) {
        importDB(context);
        this.buildingDao = new BuildingsDataSource(context);
        this.adfDao = new ADFDataSource(context);
        this.pointDao = new PointsDataSource(context);
        this.edgeDao = new EdgesDataSource(context);
        if (db == null) db = this;

    }

    /**
     * Creates a Building object and saves it in the database.
     * If building already exist database will be replaced with new value!
     *
     * @param name of the building
     * @return building object
     */
    public Building createBuilding(String name) {
        return buildingDao.createBuilding(name);
    }

    /**
     * Deletes a building from database.
     * Also will be delete all adfs and points from building in the database!
     *
     * @param building object to delete
     */
    public void deleteBuilding(Building building) {
        buildingDao.deleteBuilding(building);
    }

    /**
     * Returns a list of all buildings
     *
     * @return building list
     */
    public ArrayList<Building> getAllBuildings() {
        return buildingDao.getAllBuildings();
    }

    /**
     * Creates a ADF object and saves it in the database.
     * If ADF already exist with the same uuid in the database query will be fail and throw an exception!
     *
     * @param position of the ADF
     * @param name     of the ADF
     * @param uuid     from the ADF
     * @return ADF Object
     */
    public ADF createADF(Vector3 position, String name, String uuid) {
        if (building == null)
            throw new NullPointerException("Building is not set! Please set building or give a building name.");
        String buildingName = building.getName();
        return createADF(buildingName, position, name, uuid);
    }

    /**
     * Creates a ADF object and saves it in the database.
     *
     * @param buildingName of the Building
     * @param position     of the ADF
     * @param name         of the ADF
     * @param uuid         from the ADF.
     * @return ADF Object
     */
    public ADF createADF(String buildingName, Vector3 position, String name, String uuid) {

        Building b;

        if (buildingDao.rowNum(buildingName) > 0) {
            b = buildingDao.getBuilding(buildingName);
        } else {
            b = buildingDao.createBuilding(buildingName);
        }

        return adfDao.createADF(position, name, uuid, b);
    }

    /**
     * Deletes the adf from database.
     * Also will be delete all points from adf in the database
     *
     * @param adf to delete
     */
    public void deleteADF(ADF adf) {
        adfDao.deleteADF(adf);
    }

    /**
     * Returns a list of all adfs
     *
     * @return adf list
     */
    public ArrayList<ADF> getAllADFs() {
        return adfDao.getAllADFs();
    }

    /**
     * Returns a ADF object form the given uuid
     *
     * @param uuid of the ADF
     * @return ADF object
     */
    public ADF getAdf(String uuid) {
        return adfDao.getADF(uuid);
    }

    /**
     * Creates a Point object and saves it in the database.
     * If Point already exist with the same coordinates in the database query will be fail and throw an exception!
     *
     * @param position   of the point
     * @param properties of the point. All object have to be serializable!
     * @param tag        of the point
     * @return a point object
     */
    public Point createPoint(Vector3 position, HashMap<PointProperties, PointProperties> properties, String tag) {
        if (adf == null)
            throw new NullPointerException("ADF is not set! You have to set a ADF object by the set method or in the createPoint Method.");
        return pointDao.createPoint(position, properties, tag, adf);
    }

    /**
     * Creates a Point object and saves it in the database.
     *
     * @param position   of the point
     * @param properties of the point. All object have to be serializable!
     * @param tag        of the point
     * @param adf        of the point
     * @return a point object
     */
    public Point createPoint(Vector3 position, HashMap<PointProperties, PointProperties> properties, String tag, ADF adf) {
        return pointDao.createPoint(position, properties, tag, adf);
    }

    /**
     * Updates the point values in the database.
     *
     * @param p the point
     */
    public void updatePoint(Point p) {

        pointDao.updatePoint(p);

        //Also updates all edges from point. ATTENTION: only updates the edges from point side, not from neighbour side
        /*edgeDao.deleteEdges(p);

        for(Point n : p.getNeighbours().keySet()){
            edgeDao.createEdge(p,n);
        }*/
    }

    /**
     * Deletes the point from database.
     * Also will be delete all edges from the point in the database!
     * Not the edges from neighbour to the point!
     *
     * @param p the point
     */
    public void deletePoint(Point p) {
        pointDao.deletePoint(p);
    }

    /**
     * Returns a list of all points
     *
     * @return point list
     */
    public ArrayList<Point> getAllPoints() {
        return pointDao.getAllPoints();
    }

    /**
     * Returns a list of points of the building
     *
     * @param building where the points belongs
     * @return point list
     */
    public ArrayList<Point> getAllPoints(Building building) {
        return pointDao.getAllPoints(building);
    }

    /**
     * Returns a list of points of the adf file
     *
     * @param adf where the points belongs
     * @return point list
     */
    public ArrayList<Point> getAllPoints(ADF adf) {
        return pointDao.getAllPoints(adf);
    }

    /**
     * Creates the edge between point and neighbour in the table and in the object
     *
     * @param p the point
     * @param n the point neighbour
     */
    public void createEdge(Point p, Point n) {
        edgeDao.createEdge(p, n);
    }

    /**
     * Deletes the edge between point and neighbour in the table and in the object
     *
     * @param p the point
     * @param n the point neighbour
     */
    public void deleteEdge(Point p, Point n) {
        edgeDao.deleteEdge(p, n);
    }

    /**
     * Returns a list of all ADFs from a special building
     *
     * @param building where the adfs belongs
     * @return list of adfs
     */
    public ArrayList<ADF> getAllADF(Building building) {
        return adfDao.getAllADFs(building);
    }

    /**
     * Returns a list of all ADFs
     *
     * @return list of adfs
     */
    public ArrayList<ADF> getAllADF() {
        return adfDao.getAllADFs();
    }

    /**
     * Removes a Collection of points from database.
     *
     * @param points Collection
     */
    public void deletePoints(Collection<Point> points) {
        for (Point p : points) {
            pointDao.deletePoint(p);
        }
    }

    /**
     * Exports the Database to a shared space
     *
     * @param context of the activity
     */
    public void exportDB(final Context context) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/Exam Creator");

        if (!direct.exists()) {
            if (direct.mkdir()) {
                Log.d("DEBUGGER", "Directory is created");
            }
        }
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/" + context.getPackageName() + "/databases/roomer.db";
                String backupDBPath = "/roomerDBBackups/roomer.db";

                final File currentDB = new File(data, currentDBPath);
                final File backupDB = new File(sd, backupDBPath);
                Log.d("DEBUGGER", "BackupPath: " + backupDB.toString());
                final FileChannel src = new FileInputStream(currentDB).getChannel();
                final FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                new ToastHandler(context, backupDB.toString(),
                        Toast.LENGTH_LONG);


            }
        } catch (Exception e) {

            new ToastHandler(context, e.toString(), Toast.LENGTH_LONG);

        }
    }

    /**
     * Imports the Database for given ADF from shared space
     *
     * @param context form the activity
     */
    public void importDB(final Context context) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/Exam Creator");

        if (!direct.exists()) {
            if (direct.mkdir()) {
                Log.d("DEBUGGER", "Directory is created");
            }
        }

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/" + context.getPackageName() + "/databases/roomer.db";
                String backupDBPath = "/roomerDBBackups/roomer.db";
                final File backupDB = new File(data, currentDBPath);
                final File currentDB = new File(sd, backupDBPath);
                final FileChannel src = new FileInputStream(currentDB).getChannel();
                final FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                new ToastHandler(context, backupDB.toString(),
                        Toast.LENGTH_SHORT);

            }
        } catch (final Exception e) {

            new ToastHandler(context, e.toString(), Toast.LENGTH_SHORT);


        }
    }

    /*
    *   GETTER and SETTER
    * */

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public ADF getAdf() {
        return adf;
    }

    public void setAdf(ADF adf) {
        this.adf = adf;
    }


}
