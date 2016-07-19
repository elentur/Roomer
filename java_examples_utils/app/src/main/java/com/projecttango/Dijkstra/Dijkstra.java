package com.projecttango.Dijkstra;

import java.io.*;
import java.util.*;

public class Dijkstra {
    private static final Graph.Edge[] GRAPH = {
            new Graph.Edge("1", "2", 11),
            new Graph.Edge("2", "1", 12),
            new Graph.Edge("2", "3", 13),
            new Graph.Edge("2", "8", 14),
            new Graph.Edge("3", "4", 15),
            new Graph.Edge("4", "5", 16),
            new Graph.Edge("4", "10", 17),
            new Graph.Edge("5", "6", 18),
            new Graph.Edge("5", "12", 19),
            new Graph.Edge("6", "5", 9),
            new Graph.Edge("7", "8", 8),
            new Graph.Edge("8", "7", 7),
            new Graph.Edge("8", "2", 6),
            new Graph.Edge("8", "15", 5),
            new Graph.Edge("9", "10", 4),
            new Graph.Edge("10", "4", 3),
            new Graph.Edge("10", "17", 2),
            new Graph.Edge("10", "11", 1),
            new Graph.Edge("11", "12", 10),
            new Graph.Edge("12", "5", 21),
            new Graph.Edge("12", "13", 22),
            new Graph.Edge("12", "19", 23),
            new Graph.Edge("13", "12", 24),
            new Graph.Edge("14", "15", 25),
            new Graph.Edge("15", "8", 26),
            new Graph.Edge("15", "16", 27),
            new Graph.Edge("16", "17", 28),
            new Graph.Edge("17", "10", 29),
            new Graph.Edge("17", "18", 30),
            new Graph.Edge("18", "19", 31),
            new Graph.Edge("19", "12", 32),
            new Graph.Edge("19", "20", 33),
            new Graph.Edge("20", "19", 34),


    };
    private static final String START = "1";
    private static final String END = "20";

    /**
     * Main Class for testing algorithm
     * @param args parameter empty
     */
    public static void main(String[] args) {
        Graph g = new Graph(GRAPH);
        g.dijkstra(START);
        g.printPath(END);
        g.printAllPaths();
    }
}