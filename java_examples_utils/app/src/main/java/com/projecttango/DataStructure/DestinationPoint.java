package com.projecttango.DataStructure;

import com.j256.ormlite.table.DatabaseTable;
import org.rajawali3d.math.vector.Vector3;

import java.util.HashMap;

/**
 * Created by marcu_000 on 23.05.2016.
 */
@DatabaseTable(tableName = DestinationPoint.TABLE_NAME_POINTS)
public class DestinationPoint extends Point {

    /********************** table *********************************/

    public static final String TABLE_NAME_POINTS = "points";

    /********************* table *******************************/

    public DestinationPoint(){}

    public DestinationPoint(Vector3 position, HashMap<Point, Double> neighbours, String tag) {
        super(position, neighbours, tag);
    }

    public DestinationPoint(int id, Vector3 position, HashMap<Point, Double> neighbours, String tag) {
        super(id, position, neighbours, tag);
    }
}
