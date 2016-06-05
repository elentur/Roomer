package com.projecttango.Dijkstra;

import com.projecttango.DataStructure.NavigationPoint;
import com.projecttango.DataStructure.Point;

import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by roberto on 01.06.16.
 */
public class Main {

    private static Point pZ;

    public static void main(String[] args) {

        System.out.println("Creating graphs");

        Point[] list = marcusGraph();

        ArrayList<Point> pointList = new ArrayList<Point>(Arrays.asList(list));

        ArrayList<Point> plist = VectorGraph.getPath(new Vector3(15, 35, 0), pZ, pointList);

        for (Point p : plist) {
            System.out.println(p);
        }

    }

    private static Point[] hoersaal() {
        // Point A
        Point pA = new NavigationPoint(new Vector3(0, 1, 0), new HashMap<Point, Double>(), "A");

        // Point B
        Point pB = new NavigationPoint(new Vector3(50, 1, 0), new HashMap<Point, Double>(), "B");

        // Point C
        Point pC = new NavigationPoint(new Vector3(50, 10, 0), new HashMap<Point, Double>(), "C");

        // Point D
        Point pD = new NavigationPoint(new Vector3(0, 10, 0), new HashMap<Point, Double>(), "D");

        // Point E
        Point pE = new NavigationPoint(new Vector3(0, 20, 0), new HashMap<Point, Double>(), "E");

        // Point F
        Point pF = new NavigationPoint(new Vector3(50, 20, 0), new HashMap<Point, Double>(), "F");

        // Point Z
        pZ = new NavigationPoint(new Vector3(0, 30, 0), new HashMap<Point, Double>(), "Z");

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

        pZ = pF;

        return new Point[]{pA, pB, pC, pD, pE, pF};
    }

    private static Point[] gangMitUebergaegenen() {

        // Point 1
        Point p1 = new NavigationPoint(new Vector3(10 + getRandom(), 30, 0), new HashMap<Point, Double>(), "1");

        // Point 2
        Point p2 = new NavigationPoint(new Vector3(20 + getRandom(), 30, 0), new HashMap<Point, Double>(), "2");

        // Point 3
        Point p3 = new NavigationPoint(new Vector3(30 + getRandom(), 30, 0), new HashMap<Point, Double>(), "3");

        // Point 4
        Point p4 = new NavigationPoint(new Vector3(40 + getRandom(), 30, 0), new HashMap<Point, Double>(), "4");

        // Point 5
        Point p5 = new NavigationPoint(new Vector3(60 + getRandom(), 30, 0), new HashMap<Point, Double>(), "5");

        // Point 6
        Point p6 = new NavigationPoint(new Vector3(70 + getRandom(), 30, 0), new HashMap<Point, Double>(), "6");

        // Point 7
        Point p7 = new NavigationPoint(new Vector3(10 + getRandom(), 20, 0), new HashMap<Point, Double>(), "7");

        // Point 8
        Point p8 = new NavigationPoint(new Vector3(20 + getRandom(), 20, 0), new HashMap<Point, Double>(), "8");

        // Point 9
        Point p9 = new NavigationPoint(new Vector3(30 + getRandom(), 20, 0), new HashMap<Point, Double>(), "9");

        // Point 10
        Point p10 = new NavigationPoint(new Vector3(40 + getRandom(), 20, 0), new HashMap<Point, Double>(), "10");

        // Point 11
        Point p11 = new NavigationPoint(new Vector3(50 + getRandom(), 20, 0), new HashMap<Point, Double>(), "11");

        // Point 12
        Point p12 = new NavigationPoint(new Vector3(60 + getRandom(), 20, 0), new HashMap<Point, Double>(), "12");

        // Point 13
        Point p13 = new NavigationPoint(new Vector3(70 + getRandom(), 20, 0), new HashMap<Point, Double>(), "13");

        // Point 14
        Point p14 = new NavigationPoint(new Vector3(10 + getRandom(), 10, 0), new HashMap<Point, Double>(), "14");

        // Point 15
        Point p15 = new NavigationPoint(new Vector3(20 + getRandom(), 10, 0), new HashMap<Point, Double>(), "15");

        // Point 16
        Point p16 = new NavigationPoint(new Vector3(30 + getRandom(), 10, 0), new HashMap<Point, Double>(), "16");

        // Point 17
        Point p17 = new NavigationPoint(new Vector3(40 + getRandom(), 10, 0), new HashMap<Point, Double>(), "17");

        // Point 18
        Point p18 = new NavigationPoint(new Vector3(50 + getRandom(), 10, 0), new HashMap<Point, Double>(), "18");

        // Point 19
        Point p19 = new NavigationPoint(new Vector3(60 + getRandom(), 10, 0), new HashMap<Point, Double>(), "19");

        // Point 20
        Point p20 = new NavigationPoint(new Vector3(70 + getRandom(), 10, 0), new HashMap<Point, Double>(), "20");

        // Point Z
        pZ = p18;

        // Neighbours 1
        p1.addNeighhbour(p2);

        // Neighbours 2
        p2.addNeighhbour(p1);
        p2.addNeighhbour(p3);
        //p2.addNeighhbour(p8);

        // Neighbours 3
        p3.addNeighhbour(p2);
        p3.addNeighhbour(p4);

        // Neighbours 4
        p4.addNeighhbour(p3);
        p4.addNeighhbour(p5);
        p4.addNeighhbour(p10);

        // Neighbours 5
        p5.addNeighhbour(p4);
        p5.addNeighhbour(p6);
        p5.addNeighhbour(p12);

        // Neighbours 6
        p6.addNeighhbour(p5);

        // Neighbours 7
        p7.addNeighhbour(p8);

        // Neighbours 8
        p8.addNeighhbour(p2);
        p8.addNeighhbour(p7);
        p8.addNeighhbour(p9);
        p8.addNeighhbour(p15);

        // Neighbours 9
        p9.addNeighhbour(p8);
        p9.addNeighhbour(p10);

        // Neighbours 10
        p10.addNeighhbour(p4);
        p10.addNeighhbour(p9);
        p10.addNeighhbour(p11);
        p10.addNeighhbour(p17);

        // Neighbours 11
        p11.addNeighhbour(p10);
        p11.addNeighhbour(p11);

        // Neighbours 12
        p12.addNeighhbour(p5);
        p12.addNeighhbour(p11);
        p12.addNeighhbour(p13);
        p12.addNeighhbour(p19);

        // Neighbours 13
        p13.addNeighhbour(p12);

        // Neighbours 14
        p14.addNeighhbour(p15);

        // Neighbours 15
        //p15.addNeighhbour(p8);
        p15.addNeighhbour(p14);
        p15.addNeighhbour(p16);

        // Neighbours 16
        p16.addNeighhbour(p15);
        p16.addNeighhbour(p17);

        // Neighbours 17
        p17.addNeighhbour(p10);
        p17.addNeighhbour(p16);
        p17.addNeighhbour(p18);

        // Neighbours 18
        p18.addNeighhbour(p17);
        p18.addNeighhbour(p19);

        // Neighbours 19
        p19.addNeighhbour(p12);
        p19.addNeighhbour(p18);
        p19.addNeighhbour(p20);

        // Neighbours 20
        p20.addNeighhbour(p19);

        return new Point[]{p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20};
    }

    private static Point[] marcusGraph() {
        // setting Points
        Point a = new NavigationPoint(new Vector3(1.1, -1, 3), null, "a");
        Point b = new NavigationPoint(new Vector3(2.2, -1, 3), null, "b");
        Point c = new NavigationPoint(new Vector3(3.1, -1, 3), null, "c");
        Point d = new NavigationPoint(new Vector3(3.5, -1, 3), null, "d");
        Point e = new NavigationPoint(new Vector3(4.1, -1, 3), null, "e");
        Point f = new NavigationPoint(new Vector3(5.2, -1, 3), null, "f");
        Point g = new NavigationPoint(new Vector3(1.3, -1, 2), null, "g");
        Point h = new NavigationPoint(new Vector3(2.4, -1, 2), null, "h");
        Point i = new NavigationPoint(new Vector3(2.5, -1, 2), null, "i");
        Point j = new NavigationPoint(new Vector3(4.2, -1, 2), null, "j");
        Point k = new NavigationPoint(new Vector3(5.3, -1, 2), null, "k");
        Point l = new NavigationPoint(new Vector3(1.6, -1, 1), null, "l");
        Point m = new NavigationPoint(new Vector3(2.5, -1, 1), null, "m");
        Point n = new NavigationPoint(new Vector3(3.2, -1, 1), null, "n");
        Point o = new NavigationPoint(new Vector3(4.8, -1, 1), null, "o");
        Point p = new NavigationPoint(new Vector3(5.1, -1, 1), null, "p");

        // setting Neighbours
        a.addNeighhbour(b);
        a.addNeighhbour(g);
        b.addNeighhbour(a);
        b.addNeighhbour(c);
        c.addNeighhbour(b);
        c.addNeighhbour(d);
        c.addNeighhbour(i);
        i.addNeighhbour(c);
        d.addNeighhbour(c);
        d.addNeighhbour(e);
        e.addNeighhbour(d);
        e.addNeighhbour(f);
        e.addNeighhbour(j);
        j.addNeighhbour(e);
        f.addNeighhbour(k);
        g.addNeighhbour(a);
        h.addNeighhbour(i);
        i.addNeighhbour(h);
        i.addNeighhbour(j);
        j.addNeighhbour(k);
        j.addNeighhbour(i);
        k.addNeighhbour(f);
        k.addNeighhbour(j);
        l.addNeighhbour(m);
        m.addNeighhbour(l);
        m.addNeighhbour(n);
        n.addNeighhbour(m);
        n.addNeighhbour(i);
        i.addNeighhbour(n);
        o.addNeighhbour(j);
        j.addNeighhbour(o);
        o.addNeighhbour(p);
        p.addNeighhbour(o);

        pZ = a;

        return new Point[]{a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p};
    }

    private static double getRandom() {
        Random rand = new Random();
        return rand.nextDouble();
    }
}
