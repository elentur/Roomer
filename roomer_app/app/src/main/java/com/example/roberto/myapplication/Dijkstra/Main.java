package com.example.roberto.myapplication.Dijkstra;

import com.example.roberto.myapplication.DataStructure.NavigationPoint;
import com.example.roberto.myapplication.DataStructure.Point;
import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by
 * @aouthor roberto on 23.05.16.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("Creating graphs");


        // Point A
        Point pA = new NavigationPoint(new Vector3(9,2,1),new HashMap<Point, Double>(),"A");

        // Point B
        Point pB = new NavigationPoint(new Vector3(3,4,5),new HashMap<Point, Double>(),"B");

        // Point C
        Point pC = new NavigationPoint(new Vector3(9,9,9),new HashMap<Point, Double>(),"C");

        // Point D
        Point pD = new NavigationPoint(new Vector3(3,9,0),new HashMap<Point, Double>(),"D");

        // Point E
        Point pE = new NavigationPoint(new Vector3(7,6,2),new HashMap<Point, Double>(),"E");

        // Point F
        Point pF = new NavigationPoint(new Vector3(6,7,8),new HashMap<Point, Double>(),"F");

        // Neighbours A
        pA.addNeighhbour(pB);
        pA.addNeighhbour(pC);
        pA.addNeighhbour(pE);

        // Neighbours B
        pB.addNeighhbour(pA);
        pB.addNeighhbour(pC);

        // Neighbours C
        pC.addNeighhbour(pA);
        pC.addNeighhbour(pB);
        pC.addNeighhbour(pD);
        pC.addNeighhbour(pE);

        // Neighbours D
        pD.addNeighhbour(pC);
        pD.addNeighhbour(pF);

        // Neighbours E
        pE.addNeighhbour(pA);
        pE.addNeighhbour(pC);
        pE.addNeighhbour(pF);

        // Neighbours F
        pF.addNeighhbour(pD);
        pF.addNeighhbour(pE);


        Point[] pointList = {pA,pB,pC,pD,pE,pF};

        VectorGraph g = new VectorGraph(pointList);
        g.dijkstra(pA);
        g.printPath(pD);
        //g.printAllPaths();


    }
}
