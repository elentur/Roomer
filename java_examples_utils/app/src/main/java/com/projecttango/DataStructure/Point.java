package com.projecttango.DataStructure;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.rajawali3d.math.vector.Vector3;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by marcu_000 on 23.05.2016.
 */
@DatabaseTable(tableName = Point.TABLE_NAME_POINTS)
public abstract class Point implements Serializable{

    /********************** table *********************************/

    public static final String TABLE_NAME_POINTS = "points";
    public static final String FIELD_NAME_ID     = "id";
    public static final String FIELD_NAME_TAG   = "tag";

    /********************* table *******************************/

    private static final long serialVersionUID = 1L;

    private Vector3 position;

    private HashMap<Point,Double> neighbours;

    @DatabaseField(columnName = FIELD_NAME_TAG)
    private String tag;
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int ID = 0;

    public Point(){
        // Don't forget the empty constructor, needed by ORMLite.
    }

    public Point(Vector3 position, HashMap<Point, Double> neighbours,  String tag) {

        this.position = position;
        this.neighbours = neighbours!= null? neighbours: new  HashMap<Point,Double>();
        this.tag = tag;

        if(ID == 0){
            this.ID = this.hashCode();
        }
    }

    public Point(int id, Vector3 position, HashMap<Point, Double> neighbours,  String tag) {
        this.position = position;
        this.neighbours = neighbours != null ? neighbours : new HashMap<Point, Double>();
        this.tag = tag;
        this.ID = id;
    }

    public Vector3 getPosition() {
        return position;
    }

    public HashMap<Point, Double> getNeighbours() {
        return neighbours;
    }

    public void addNeighhbour(Point p){
        Double x = distance(this, p);
        neighbours.put(p,x);
    }

    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }

    public double distance(Point p1, Point p2){

       return Vector3.distanceTo(p1.getPosition(),p2.getPosition());
    }

    public int getID() {
        return ID;
    }

    public String getTag() {
        return tag;
    }
    public void setTag( String tag) {
        this.tag = tag;
    }

    public String toString(){
        return tag + ": " + String.format("x: %.2f y: %.2f z: %.2f",position.x,position.y,position.z);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Point))return false;
        return ((Point)o).getTag().equals(this.getTag());
    }
}
