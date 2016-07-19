package com.projecttango.DataStructure;

import org.rajawali3d.math.vector.Vector3;

/**
 * Created by Marcus Bätz on 14.07.2016.
 */
public class PosCalculator {

    public static Vector3 newPos(ADF adf, Point point){
        if(point.getAdf().equals(adf)){
           return new Vector3(point.getPosition());
        }else{
            Vector3 offset = Vector3.subtractAndCreate(point.getAdf().getPosition(),adf.getPosition());
           return Vector3.addAndCreate(point.getPosition(),offset);

        }
    }

}
