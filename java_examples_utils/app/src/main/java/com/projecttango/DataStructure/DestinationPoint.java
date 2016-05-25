package com.projecttango.DataStructure;

/**
 * Created by roberto on 23.05.16.
 */
import org.rajawali3d.math.vector.Vector3;

import java.util.HashMap;

public class DestinationPoint extends Point {
    public DestinationPoint(Vector3 position, HashMap<Point, Double> neighbours, String tag) {
        super(position, neighbours, tag);
    }
}