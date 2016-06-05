package com.projecttango.DijkstraMarcus;

/**
 * Created by roberto on 05.06.16.
 */

import com.projecttango.DataStructure.NavigationPoint;
import com.projecttango.DataStructure.Point;
import org.rajawali3d.math.vector.Vector3;

import java.util.*;

public class Dijkstra {
    static Point a = new NavigationPoint(new Vector3(1, -1, 3), null, "a");
    static Point b = new NavigationPoint(new Vector3(2, -1, 3), null, "b");
    static Point c = new NavigationPoint(new Vector3(3, -1, 3), null, "c");
    static Point d = new NavigationPoint(new Vector3(3.5, -1, 3), null, "d");
    static Point e = new NavigationPoint(new Vector3(4, -1, 3), null, "e");
    static Point f = new NavigationPoint(new Vector3(5, -1, 3), null, "f");
    static Point g = new NavigationPoint(new Vector3(1, -1, 2), null, "g");
    static Point h = new NavigationPoint(new Vector3(2, -1, 2), null, "h");
    static Point i = new NavigationPoint(new Vector3(2.5, -1, 2), null, "i");
    static Point j = new NavigationPoint(new Vector3(4, -1, 2), null, "j");
    static Point k = new NavigationPoint(new Vector3(5, -1, 2), null, "k");
    static Point l = new NavigationPoint(new Vector3(1, -1, 1), null, "l");
    static Point m = new NavigationPoint(new Vector3(2, -1, 1), null, "m");
    static Point n = new NavigationPoint(new Vector3(3, -1, 1), null, "n");
    static Point o = new NavigationPoint(new Vector3(4, -1, 1), null, "o");
    static Point p = new NavigationPoint(new Vector3(5, -1, 1), null, "p");
    private static Graph.Edge[] GRAPH;

    static void setNeighbours() {
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
        ArrayList<Point> points = new ArrayList<Point>();
        points.add(a);
        points.add(b);
        points.add(c);
        points.add(d);
        points.add(e);
        points.add(f);
        points.add(g);
        points.add(h);
        points.add(i);
        points.add(j);
        points.add(k);
        points.add(l);
        points.add(m);
        points.add(n);
        points.add(o);
        points.add(p);
        ArrayList<Graph.Edge> edges = new ArrayList<Graph.Edge>();

        for(Point p : points){
            for(Point key: p.getNeighbours().keySet()){
                //System.out.println(p.getTag() + " " + key.getTag() + " dist: " + p.getNeighbours().get(key));
                edges.add(new Graph.Edge(p, key, p.getNeighbours().get(key)));
            }
        }

        GRAPH =new Graph.Edge[edges.size()];

        for(int i = 0; i<GRAPH.length; i++){
            GRAPH[i] = edges.get(i);
        }
    }

    private static final Point START = o;
    private static final Point END = a;

    public static void main(String[] args) {
        setNeighbours();
        Graph g = new Graph(GRAPH);
        g.dijkstra(START);
        //g.printPath(END);
        g.printAllPaths();
    }
}
