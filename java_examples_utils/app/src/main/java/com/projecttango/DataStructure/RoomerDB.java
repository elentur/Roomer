package com.projecttango.DataStructure;

import android.content.Context;
import org.rajawali3d.math.vector.Vector3;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Marcus Bätz on 24.05.2016.
 * This Class is for loading, saving, import and export of roomer databases including NavPoints
 */
public class RoomerDB {

    private BuildingsDataSource buildingDao;
    private ADFDataSource adfDao;
    private PointsDataSource pointDao;
    private EdgesDataSource edgeDao;

    /**
     * Creates a object of RoomerDB to access to the database methods
     * @param context for the database
     */
    public RoomerDB(Context context) {
        this.buildingDao = new BuildingsDataSource(context);
        this.adfDao = new ADFDataSource(context);
        this.pointDao = new PointsDataSource(context);
        this.edgeDao = new EdgesDataSource(context);
    }

    /**
     * Creates a ADF object and saves it in the database.
     * @param buildingName of the Building
     * @param position of the ADF
     * @param uuid from the ADF.
     * @return ADF Object
     */
    public ADF createADF(String buildingName, Vector3 position, String uuid ){

        Building b;

        if(buildingDao.exist(buildingName)){
           b = buildingDao.getBuilding(buildingName);
        }else{
            b = buildingDao.createBuilding(buildingName);
        }

        return adfDao.createADF(position, uuid, b);
    }

    /**
     * Deletes the adf from database.
     * @param adf to delete
     */
    public void deleteADF(ADF adf){
        adfDao.deleteADF(adf);
    }

    /**
     * Creates a Point object and saves it in the database.
     * @param position of the point
     * @param properties of the point. All object have to be serializable!
     * @param tag of the point
     * @param adf of the point
     * @return a point object
     */
    public Point createPoint(Vector3 position, HashMap<String,Object> properties,String tag, ADF adf){
        Point point = pointDao.createPoint(position,properties,tag,adf);
        return point;
    }

    /**
     * Updates the point values in the database
     * @param p the point
     */
    public void updatePoint(Point p){
        pointDao.updatePoint(p);
    }

    /**
     * Deletes the point from database.
     * @param p the point
     */
    public void deletePoint(Point p){
        pointDao.deletePoint(p);
    }

    /**
     * Returns a list of all points
     * @return point list
     */
    public List<Point> getAllPoints(){
        return pointDao.getAllPoints();
    }

    /**
     * Returns a list of points of the building
     * @param building where the points belongs
     * @return point list
     */
    public List<Point> getAllPoints(Building building){
        return pointDao.getAllPoints(building);
    }

    /**
     * Returns a list of points of the adf file
     * @param adf where the points belongs
     * @return point list
     */
    public List<Point> getAllPoints(ADF adf){
        return pointDao.getAllPoints(adf);
    }

    /**
     * Creates the edge between point and neighbour in the table and in the object
     * @param p the point
     * @param n the point neighbour
     */
    public void createEdge(Point p, Point n){
        edgeDao.createEdge(p,n);
    }

    /**
     * Deletes the edge between point and neighbour in the table and in the object
     * @param p the point
     * @param n the point neighbour
     */
    public void deleteEdge(Point p, Point n){
        edgeDao.deleteEdge(p,n);
    }

    /**
     * Returns a list of all ADFs from a special building
     * @param building where the adfs belongs
     * @return list of adfs
     */
    public List<ADF> getAllADF(Building building){
        return adfDao.getAllADFs(building);
    }

    /**
     * Returns a list of all ADFs
     * @return list of adfs
     */
    public List<ADF> getAllADF(){
        return adfDao.getAllADFs();
    }

}
