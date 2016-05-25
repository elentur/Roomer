package com.projecttango.DataStructure;

/**
 * Created by
 * @author roberto on 23.05.16.
 */
import org.rajawali3d.math.vector.Vector3;

import java.util.HashMap;

public class NavigationPoint extends Point {
    public NavigationPoint(Vector3 position, HashMap<Point, Double> neighbours, String tag) {
        super(position, neighbours, tag);
    }
}
