package com.projecttango.DataStructure;

import com.j256.ormlite.table.DatabaseTable;
import org.rajawali3d.math.vector.Vector3;

import java.util.HashMap;

/**
 * Created by marcu_000 on 23.05.2016.
 */
@DatabaseTable(tableName = NavigationPoint.TABLE_NAME_POINTS)
public class NavigationPoint extends Point {

    /********************** table *********************************/

    public static final String TABLE_NAME_POINTS = "points";

    /********************* table *******************************/

    public NavigationPoint(){}

    public NavigationPoint(Vector3 position, HashMap<Point, Double> neighbours, String tag) {
        super(position, neighbours, tag);
    }

    public NavigationPoint(int id, Vector3 position, HashMap<Point, Double> neighbours, String tag) {
        super(id,position, neighbours, tag);
    }
}
