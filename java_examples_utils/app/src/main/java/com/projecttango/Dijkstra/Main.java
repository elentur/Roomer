package com.projecttango.Dijkstra;

import com.projecttango.DataStructure.NavigationPoint;
import com.projecttango.DataStructure.Point;

import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by roberto on 01.06.16.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Creating graphs");


        // Point A
        Point pA = new NavigationPoint(new Vector3(0,1,0),new HashMap<Point, Double>(),"A");

        // Point B
        Point pB = new NavigationPoint(new Vector3(50,1,0),new HashMap<Point, Double>(),"B");

        // Point C
        Point pC = new NavigationPoint(new Vector3(50,10,0),new HashMap<Point, Double>(),"C");

        // Point D
        Point pD = new NavigationPoint(new Vector3(0,10,0),new HashMap<Point, Double>(),"D");

        // Point E
        Point pE = new NavigationPoint(new Vector3(0,20,0),new HashMap<Point, Double>(),"E");

        // Point F
        Point pF = new NavigationPoint(new Vector3(50,20,0),new HashMap<Point, Double>(),"F");

        // Point Z
        Point pZ = new NavigationPoint(new Vector3(0,30,0),new HashMap<Point, Double>(),"Z");

        // Neighbours A
        pA.addNeighhbour(pB);
        pA.addNeighhbour(pD);

        // Neighbours B
        pB.addNeighhbour(pA);
        pB.addNeighhbour(pC);

        // Neighbours C
        pC.addNeighhbour(pB);
        pC.addNeighhbour(pD);
        pC.addNeighhbour(pF);

        // Neighbours D
        pD.addNeighhbour(pA);
        pD.addNeighhbour(pC);
        pD.addNeighhbour(pE);

        // Neighbours E
        pE.addNeighhbour(pD);
        pE.addNeighhbour(pF);

        // Neighbours F
        pF.addNeighhbour(pC);
        pF.addNeighhbour(pE);
        pF.addNeighhbour(pZ);

        // Neighbours Z
        pZ.addNeighhbour(pF);

        ArrayList<Point> pointList = new ArrayList<Point>(Arrays.asList(new Point[] {pA,pB,pC,pD,pE,pF,pZ}));

       /* for(Point p : pointList){
            for(Point neighbour : p.getNeighbours().keySet()){
                System.out.println(p.getTag() + " -> " + neighbour.getTag() + "(" + p.getNeighbours().get(neighbour) + ")");
            }
        }*/

        System.out.println(
                VectorGraph.getPath(new Vector3(80,5,0),pZ,pointList)
        );
    }
}
