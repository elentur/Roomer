package com.projecttango.Dijkstra;
import com.projecttango.DataStructure.NavigationPoint;
import com.projecttango.DataStructure.Point;


import org.rajawali3d.math.vector.Vector3;

import java.util.*;

/**
 * Created by
 *
 * @authos roberto on 24.05.16.
 */
public class VectorGraph {

    private  final Map<Point, Vertex> graph; // mapping of vertex names to Vertex objects, built from a set of Edges

    private final static ArrayList<Point> path = new ArrayList<Point>();

    public static ArrayList<Point> getPath(Vector3 pos,Point end, ArrayList<Point> pointList) {
        VectorGraph g = new VectorGraph(pointList);
        Point start = g.findNearestStartPoint(pos);
        g.dijkstra(start);
        g.printPath(end);
        return path;
    }

    private Point findNearestStartPoint(Vector3 pos){

        double min_dist = Double.MAX_VALUE;

        Vector3 p = pos.clone();

        ArrayList<Point> pointList = new ArrayList<Point>();

        for(Point k :graph.keySet()){

            Vector3 a = k.getPosition().clone();

            for(Point n : k.getNeighbours().keySet()){

                Vector3 b = n.getPosition().clone();

                Vector3 q = Vector3.subtractAndCreate(b,a);

                double r = (q.x*(p.x-a.x) + q.y*(p.y-a.y) + q.z*(p.z-a.z)) / (q.x*q.x + q.y*q.y +q.z*q.z );

                double distance;

                HashMap<Point,Double> neighbours;

                if(r < 0 ){
                    distance = Vector3.distanceTo(a,p);
                    if(distance<min_dist) {
                        min_dist = distance;
                        pointList = new ArrayList<Point>();
                        pointList.add(k);
                    }

                }else if(r > 1){
                    distance = Vector3.distanceTo(b,p);
                    if(distance<min_dist) {
                        min_dist = distance;
                        pointList = new ArrayList<Point>();
                        pointList.add(n);
                    }
                }else{
                    Vector3 lotpunkt = Vector3.multiplyAndCreate(q,r).add(a);
                    distance = Vector3.distanceTo(lotpunkt,p);

                    if(distance<min_dist) {
                        min_dist = distance;
                        pointList = new ArrayList<Point>();
                        pointList.add(k);
                        pointList.add(n);
                    }

                }
            }
        }

        Point point = new NavigationPoint(new Vector3(pos),new HashMap<Point, Double>(),"Start");

        for(Point candidate : pointList){
            point.addNeighhbour(candidate);
        }

        if (!graph.containsKey(point)) graph.put(point, new Vertex(point));

        for(Point neighbour : point.getNeighbours().keySet()){
            graph.get(point).neighbours.put(graph.get(neighbour), point.getNeighbours().get(neighbour));
        }

        return point;
    }

    /**
     * One vertex of the graph, complete with mappings to neighbouring vertices
     */
    private class Vertex implements Comparable<Vertex> {
        public final Point p;
        public double dist = Double.MAX_VALUE; // MAX_VALUE assumed to be infinity
        public Vertex previous = null;
        public Map<Vertex, Double> neighbours = new HashMap<Vertex, Double>();

        public Vertex(Point p) {
            this.p = p;
        }

        private void printPath() {
            if (this == this.previous) {
                path.add(this.p);
                System.out.printf("%s", this.p.getTag());
            } else if (this.previous == null) {
                System.out.printf("%s(unreached)", this.p.getTag());
            } else {
                this.previous.printPath();
                path.add(this.p);
                System.out.printf(" -> %s(%f)", this.p.getTag(), this.dist);
            }
        }

        public int compareTo(Vertex other) {
            return Double.compare(dist, other.dist);
        }

        @Override
        public String toString() {
            return "Vertex{" +
                    "p=" + p +
                    ", dist=" + dist +
                    ", previous=" + previous +
                    '}';
        }
    }

    /**
     * Builds a graph from a set of points
     */
    private VectorGraph(ArrayList<Point> points) {
        graph = new HashMap<Point, Vertex>(points.size());

        //one pass to find all vertices
        for (Point p : points) {
            if (!graph.containsKey(p)) graph.put(p, new Vertex(p));
        }

        for (Point p : points) {
            for(Point neighbour : p.getNeighbours().keySet()){
                graph.get(p).neighbours.put(graph.get(neighbour), p.getNeighbours().get(neighbour));
            }
        }
    }

    /**
     * Runs dijkstra using a specified source vertex
     */
    private void dijkstra(Point startPoint) {
        if (!graph.containsKey(startPoint)) {
            System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startPoint);
            return;
        }
        final Vertex source = graph.get(startPoint);
        NavigableSet<Vertex> q = new TreeSet<Vertex>();

        // set-up vertices
        for (Vertex v : graph.values()) {
            v.previous = v == source ? source : null;
            v.dist = v == source ? 0 : Double.MAX_VALUE;
            q.add(v);
        }

        dijkstra(q);
    }

    /**
     * Implementation of dijkstra's algorithm using a binary heap.
     */
    private void dijkstra(final NavigableSet<Vertex> q) {
        Vertex u, v;
        while (!q.isEmpty()) {

            u = q.pollFirst(); // vertex with shortest distance (first iteration will return source)
            if (u.dist == Double.MAX_VALUE)
                break; // we can ignore u (and any other remaining vertices) since they are unreachable

            //look at distances to each neighbour
            for (Map.Entry<Vertex, Double> a : u.neighbours.entrySet()) {
                v = a.getKey(); //the neighbour in this iteration

                final double alternateDist = u.dist + a.getValue();
                if (alternateDist < v.dist) { // shorter path to neighbour found
                    q.remove(v);
                    v.dist = alternateDist;
                    v.previous = u;
                    q.add(v);
                }
            }
        }
    }

    /** Prints a path from the source to the specified vertex */
    private void printPath(Point endPoint) {
        if (!graph.containsKey(endPoint)) {
            System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endPoint);
            return;
        }

        graph.get(endPoint).printPath();
        System.out.println();
    }
    /** Prints the path from the source to every vertex (output order is not guaranteed) */
    private void printAllPaths() {
        for (Vertex v : graph.values()) {
            v.printPath();
            System.out.println();
        }
    }



}
