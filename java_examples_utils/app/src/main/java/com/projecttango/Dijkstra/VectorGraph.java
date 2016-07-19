package com.projecttango.Dijkstra;

import com.projecttango.DataStructure.NavigationPoint;
import com.projecttango.DataStructure.Point;
import org.rajawali3d.math.vector.Vector3;

import java.util.*;

/**
 * Created by
 * Robert Dziuba on 05.06.16.
 */
public class VectorGraph {

    private final Map<Point, Vertex> graph; // mapping of vertex names to Vertex objects, built from a set of Edges
    private final static ArrayList<Point> path = new ArrayList<Point>(); // final way with the shortest distance

    /**
     * A static function which get a start vector, end point and a list of all points for the graph
     * @param pos a position from where to go. can be everywhere.
     * @param end Point to go. Have to be a point of the graph!!!!!
     * @param points all points from which the graph is to be built.
     * @return a list of the points where to go.
     */
    public static ArrayList<Point> getPath(Vector3 pos, Point end, ArrayList<Point> points) {
        Edge[] edges = setNeighbours(points);

        VectorGraph g = new VectorGraph(edges);
        Point start = g.findNearestStartPoint(pos);
       // Point start = null;
        /*for(Point p: points){
            if(p.getTag().equals("D224"))start = p;
        }*/
        g.dijkstra(start);
        g.printPath(end);
        return path;
    }

    /**
     * Finds the nearest Point next to the start coordinate and makes a connection to it.
     * @param pos Vector3 coordinate of the start vector
     * @return Point Obj what is integrated in the graph
     */
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

        Point point = new Point(new Vector3(pos),new HashMap<Point, Double>(),"Start");

        for(Point candidate : pointList){
            point.addNeighbour(candidate);
        }
        point.setAdf(pointList.get(0).getAdf());
        if (!graph.containsKey(point)) graph.put(point, new Vertex(point));

        for(Point neighbour : point.getNeighbours().keySet()){

            graph.get(point).neighbours.put(graph.get(neighbour), point.getNeighbours().get(neighbour));
            graph.get(neighbour).neighbours.put(graph.get(point), Vector3.distanceTo(graph.get(neighbour).name.getPosition(),graph.get(point).name.getPosition()));
        }

        return point;
    }

    /**
     * Translate the point array list to an Edge array
     * @param points
     * @return Edge[] array of edges
     */
    private static Edge[] setNeighbours(ArrayList<Point> points){

        ArrayList<Edge> edgeList = new ArrayList<Edge>();

        for(Point p : points){
            for(Point key: p.getNeighbours().keySet()){
                edgeList.add(new Edge(p, key, p.getNeighbours().get(key)));
            }
        }

        Edge[] edges = new Edge[edgeList.size()];

        for(int i = 0; i< edgeList.size(); i++){
            edges[i] = edgeList.get(i);
        }

        return edges;
    }

    /**
     * One edge of the graph (only used by Graph constructor)
     */
    public static class Edge {
        public final Point v1, v2;
        public final double dist;

        public Edge(Point v1, Point v2, double dist) {
            this.v1 = v1;
            this.v2 = v2;
            this.dist = dist;
        }
    }

    /**
     * One vertex of the graph, complete with mappings to neighbouring vertices
     */
    public static class Vertex implements Comparable<Vertex> {
        public final Point name;
        public double dist = Double.MAX_VALUE; // MAX_VALUE assumed to be infinity
        public Vertex previous = null;
        public final Map<Vertex, Double> neighbours = new HashMap<Vertex, Double>();

        public Vertex(Point name) {
            this.name = name;
        }

        private void printPath() {
            if (this == this.previous) {
                System.out.printf("%s", this.name.getTag());
                path.add(this.name);
            } else if (this.previous == null) {
                System.out.printf("%s(unreached)", this.name.getTag());
            } else {
                this.previous.printPath();
                System.out.printf(" -> %s(%.2f)", this.name.getTag(), this.dist);
                path.add(this.name);
            }
        }

        public int compareTo(Vertex other) {
            return Double.compare(dist, other.dist);
        }

        @Override
        public String toString() {
            return "Vertex{" +
                    "dist=" + dist +
                    ", name=" + name +
                    '}';
        }
    }

    /**
     * Builds a graph from a set of edges
     */
    public VectorGraph(Edge[] edges) {
        graph = new HashMap<Point, Vertex>(edges.length);

        //one pass to find all vertices
        for (Edge e : edges) {
            if (!graph.containsKey(e.v1)) graph.put(e.v1, new Vertex(e.v1));
            if (!graph.containsKey(e.v2)) graph.put(e.v2, new Vertex(e.v2));
        }

        //another pass to set neighbouring vertices
        for (Edge e : edges) {
            graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
            //graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph
        }
    }

    /**
     * Runs dijkstra using a specified source vertex
     */
    public void dijkstra(Point startName) {
        if (!graph.containsKey(startName)) {
            System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
            return;
        }
        final Vertex source = graph.get(startName);
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

    /**
     * Prints a path from the source to the specified vertex
     */
    public void printPath(Point endName) {
        if (!graph.containsKey(endName)) {
            System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
            return;
        }

        graph.get(endName).printPath();
        System.out.println();
    }

    /**
     * Prints the path from the source to every vertex (output order is not guaranteed)
     */
    public void printAllPaths() {
        for (Vertex v : graph.values()) {
            v.printPath();
            System.out.println();
        }
    }
}
