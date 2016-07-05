package com.projecttango.DataStructure;

import android.util.Log;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.rajawali3d.math.vector.Vector3;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by
 * marcu_000 on 23.05.2016.
 */
public class Point implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * unique id of the point. Will be automaticly set by its hashCode()
     */
    private long id;

    /**
     * position of the point
     */
    private Vector3 position;

    /**
     * all related neighbours, the key is the neighbour point and the value is the distance between it and this point.
     */
    private HashMap<Point,Double> neighbours;

    /**
     * all related property. !IMPORTANT: all values have to be serializable!
     */
    private HashMap<String,Object> properties;

    /**
     * designation of the point
     */
    private String tag;


    /**
     * Empty Constructor for DB integration
     */
    public Point(){
        // Don't forget the empty constructor, needed by ORMLite.
    }

    /**
     * Instance of a point object.
     */
    public Point(Vector3 position, HashMap<Point, Double> neighbours, String tag) {
        this(0,position,neighbours,tag,new HashMap<String,Object>());
    }

    /**
     * Instance of a point object.
     */
    public Point(int id, Vector3 position, HashMap<Point, Double> neighbours, String tag) {
        this(id,position,neighbours,tag,new HashMap<String,Object>());
    }

    /**
     * Instance of a point object.
     */
    public Point(Vector3 position, HashMap<Point, Double> neighbours, String tag, HashMap<String,Object> properties) {
        this(0,position,neighbours,tag,properties);
    }

    /**
     * Instance of a point object.
     * @param id
     * @param position of the point represented by a vector3 object
     * @param neighbours HashMap of all other points which are in relation with this point
     * @param tag designation of the point
     * @param properties HashMap with all properties of the point, dynamically extensible. !IMPORTANT: all values have to be serializable!
     */
    public Point(int id, Vector3 position, HashMap<Point, Double> neighbours, String tag, HashMap<String,Object> properties) {
        if(id == 0){
            this.id = this.hashCode();
        }

        this.position = position;

        this.neighbours = neighbours != null ? neighbours: new  HashMap<Point,Double>();

        this.tag = tag;

        this.properties = properties;
    }

    /**
     * calculates the distance between 2 points
     * @param p1 first point
     * @param p2 first point
     * @return the distance as a double
     */
    public double distance(Point p1, Point p2){

       return Vector3.distanceTo(p1.getPosition(),p2.getPosition());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public HashMap<Point, Double> getNeighbours() {
        return neighbours;
    }

    /**
     * adds a new neighbour to this point. Ignores neighbours which are the equals!
     * @param p neighbour point
     */
    public void addNeighhbour(Point p){
        if(p == null) throw new IllegalArgumentException("The Point can not be null!");
        if(!this.equals(p)) {

            if(neighbours == null) neighbours = new HashMap<Point, Double>();

            Double x = distance(this, p);
            neighbours.put(p, x);
        }
    }

    /**
     * removes a Point from the neighbours map.
     * @param p neighbour point
     */
    public void rmNeighbour(Point p){
        if(p == null) throw new IllegalArgumentException("The Point can not be null!");
        neighbours.remove(p);
    }

    /**
     * Returns the property to which the specified key is mapped, or null if this map contains no mapping for the key.
     * @param key name of the property
     * @return Returns the value or null if the map no contains the key.
     */
    public Object getProperty(String key) {
        if(key.isEmpty()) throw new IllegalArgumentException("To get a property the key can not be empty!");
        return this.properties.get(key);
    }

    /**
     * Associates the specified property with the specified key in this map.
     * @param key name of the property
     * @param value the object to save
     */
    public void setProperty(String key, Object value) {
        if(key.isEmpty()) throw new IllegalArgumentException("To set a property the key can not be empty!");
        if(value == null) throw new IllegalArgumentException("To set a property the value can not be null!");
        this.properties.put(key,value);
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * @param key name of the property
     */
    public void removeProperty(String key) {
        if(key.isEmpty()) throw new IllegalArgumentException("To get a property the key can not be empty!");
        this.properties.remove(key);
    }

    public HashMap<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, Object> properties) {
        if(properties == null) throw new IllegalArgumentException("The properties can not be null!");
        this.properties = properties;
    }

    public String getTag() {
        return tag;
    }

    public void setTag( String tag) {
        if(tag.isEmpty()) throw new IllegalArgumentException("The tag can not be empty!");
        this.tag = tag;
    }

    @Override
    public String toString(){
        return tag + ": " + String.format("x: %.2f y: %.2f z: %.2f",this.position.x,this.position.y,this.position.z);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Point))return false;
        return ((Point)o).getTag().equals(this.getTag());
    }

    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }
}
