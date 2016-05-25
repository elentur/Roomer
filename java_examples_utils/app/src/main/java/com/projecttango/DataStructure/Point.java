package com.projecttango.DataStructure;

/**
 * Created by
 * @author roberto on 23.05.16.
 */

import org.rajawali3d.math.vector.Vector3;

import java.util.HashMap;

public abstract class Point {

    private final Vector3 position;
    private final HashMap<Point,Double> neighbours;
    private String tag;

    public Point(Vector3 position, HashMap<Point, Double> neighbours,  String tag) {
        this.position = position;
        this.neighbours = neighbours;
        this.tag = tag;
    }

    public Vector3 getPosition() {
        return position;
    }

    public HashMap<Point, Double> getNeighbours() {
        return neighbours;
    }

    public void addNeighhbour(Point p){
        neighbours.put(p,distance(this,p));
    }

    public double distance(Point p1, Point p2){
        return Vector3.distanceTo(p1.getPosition(),p2.getPosition());
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "Point{" +
                "position=" + position +
                //", neighbours=" + neighbours +
                ", tag='" + tag + '\'' +
                '}';
    }
}
