package com.projecttango.DijkstraMarcus;

import com.projecttango.DataStructure.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * Created by roberto on 05.06.16.
 */
class Graph {
    private final Map<Point, Vertex> graph; // mapping of vertex names to Vertex objects, built from a set of Edges

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
            } else if (this.previous == null) {
                System.out.printf("%s(unreached)", this.name.getTag());
            } else {
                this.previous.printPath();
                System.out.printf(" -> %s(%.2f)", this.name.getTag(), this.dist);
            }
        }

        public int compareTo(Vertex other) {
            return Double.compare(dist, other.dist);
        }
    }

    /**
     * Builds a graph from a set of edges
     */
    public Graph(Edge[] edges) {
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
