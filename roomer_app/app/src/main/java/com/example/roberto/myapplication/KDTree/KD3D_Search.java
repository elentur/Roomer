package com.example.roberto.myapplication.KDTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * This Klass ist only for debugging of the KD-Tree
 */
public class KD3D_Search {


    private static KD3DTree kdt;

    public static void main(String args[]) throws IOException {

        int numpoints = 20;

        kdt = new KD3DTree(numpoints + 1);

        ArrayList<double[]> list;

        /* different lists to choose */

        //list = randomList(numpoints);
        //list = zChainList(2,2,numpoints);
        list = ZweiDList(numpoints);

        System.out.println("Setting points");

        double[] zero = {0,0,0};
        kdt.add(zero);

        for(double[] d : list){
            kdt.add(d);
        }

        // getting two points of the list
        ArrayList<KD3DNode> inOrderList = kdt.inorder();

        KD3DNode start = inOrderList.get(2);
        KD3DNode end = inOrderList.get(9);

        ArrayList<KD3DNode> way = findShortestWay(start,end);
        System.out.println("sortest Way");
        for(KD3DNode node : way){
            System.out.println(node);
        }
    }

    private static ArrayList<KD3DNode> findShortestWay(KD3DNode start , KD3DNode end){

        ArrayList<KD3DNode> inOrderList = kdt.inorder();
        ArrayList<KD3DNode> way = new ArrayList<KD3DNode>();

        KD3DNode next = start;
        KD3DNode bestNode = end;

        next.checked = true;
        way.add(next);

        double d;
        double min_d;


        System.out.println("Start Node: " + start);
        System.out.println("End Node: " + end);
        System.out.println("++++++++++++++++++++");

        do {

            min_d = start.distance2(start.x, end.x, 3);

            for (KD3DNode node : inOrderList) {

                d = next.distance2(node.x, next.x, 3);

                //System.out.println("distance: " + d + " node: " + node);

                if (d <= min_d && !node.checked) {
                    min_d = d;
                    if(isCloserToEnd(node,next,end)) {
                        bestNode = node;
                    }
                }
            }

            bestNode.checked = true;
            way.add(bestNode);
            next = bestNode;
            /*System.out.println("min dist: " + min_d);
            System.out.println("Next Node: " + next);
            System.out.println("is equal to end: " + next.equal(next.x,end.x,3));
            System.out.println("*******************");*/


        }while(!next.equal(next.x,end.x,3));

        return way;
    }

    private static boolean isCloserToEnd(KD3DNode node, KD3DNode next, KD3DNode end) {

        double distNode = end.distance2(node.x,end.x,3);
        double distNext = end.distance2(next.x,end.x,3);

        return distNode < distNext;
    }

    private static ArrayList<double[]> zChainList(int x, int y, int num) {
        ArrayList<double[]> list = new ArrayList<double[]>();

        for (int i = 0; i < num; i++) {

            double[] d = { x, y, i  - num/2};

            System.out.println(d[0] + ", " + d[1] + ", " + d[2]);

            list.add(d);
        }

        return list;
    }

    private static ArrayList<double[]> ZweiDList(int num) {
        ArrayList<double[]> list = new ArrayList<double[]>();

        Random r = new Random();

        for (int i = 0; i < num; i++) {

            double[] d = { -10.0 + r.nextDouble() * 20.0, -10.0 + r.nextDouble() * 20.0, 2};

            System.out.println(d[0] + ", " + d[1] + ", " + d[2]);

            list.add(d);
        }

        return list;
    }

    private static ArrayList<double[]> randomList(int num){
        ArrayList<double[]> list = new ArrayList<double[]>();

        Random r = new Random();

        for (int i = 0; i < num; i++) {

            double[] d = {
                    -10.0 + r.nextDouble() * 20.0,
                    -10.0 + r.nextDouble() * 20.0,
                    -10.0 + r.nextDouble() * 20.0
            };

            System.out.println(d[0] + ", " + d[1] + ", " + d[2]);

            list.add(d);
        }

        return list;
    }

}
