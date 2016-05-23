package com.example.roberto.myapplication.KDTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * This Klass ist only for debugging of the KD-Tree
 */
public class KD3D_Search {


    private static KD3DTree kdt;

    private static Random r = new Random();


    public static void main(String args[]) throws IOException {

        int numpoints = 10;

        kdt = new KD3DTree(numpoints + 1);

        ArrayList<double[]> list;

        /* different lists to choose */

        //list = randomList(numpoints);
        //list = zChainList(2,2,numpoints);
        list = zweiDList(numpoints);
        //list = handMadeList();


        System.out.println("Setting points");

        for(double[] d : list){
            kdt.add(d);
        }

        // getting two points of the list
        ArrayList<KD3DNode> inOrderList = kdt.inorder();



        /*double[] d0 = {
                Math.floor((numpoints * -1) + r.nextDouble() * (numpoints * 2)) / 1,
                Math.floor((numpoints * -1) + r.nextDouble() * (numpoints * 2)) / 1,
                Math.floor((numpoints * -1) + r.nextDouble() * (numpoints * 2)) / 1
        };*/
        double[] d0 = {
                numpoints * -1,
                numpoints * -1,
                numpoints * -1
        };
        KD3DNode start = kdt.find_nearest(d0);
        /*double[] d1 = {
                Math.floor((numpoints * -1) + r.nextDouble() * (numpoints * 2)) / 1,
                Math.floor((numpoints * -1) + r.nextDouble() * (numpoints * 2)) / 1,
                Math.floor((numpoints * -1) + r.nextDouble() * (numpoints * 2)) / 1
        };*/
        double[] d1 = {
                numpoints,
                numpoints,
                numpoints
        };
        KD3DNode end = kdt.find_nearest(d1);

        ArrayList<KD3DNode> way = findShortestWay(start,end);
        System.out.println("sortest Way in " + way.size() + " steps.");
        for(KD3DNode node : way){
            System.out.println(node);
        }
    }

    private static void drawCoordinatenSys(ArrayList<double[]> list) {
        for(double y =10;y >= -10; y--){
            for(double x=-10;x <= 10; x++){
                String str = "| ";

                if(x==0 && y==0)
                    str = "|o";

                for(double[] d : list){
                    if(d[0] == x && d[1] == y)
                        str = "|x";
                }

                System.out.print(str);
            }
            System.out.println("|");
        }
    }

    private static ArrayList<double[]> handMadeList() {
        ArrayList<double[]> list = new ArrayList<double[]>();

        double[] d1 = {0,0,0};
        list.add(d1);

        double[] d2 = {1,1,0};
        list.add(d2);

        double[] d3 = {4,2,0};
        list.add(d3);

        double[] d4 = {3,5,0};
        list.add(d4);

        double[] d5 = {5,3,0};
        list.add(d5);

        double[] d6 = {2,3,0};
        list.add(d6);

        double[] d7 = {4,1,0};
        list.add(d7);

        double[] d8 = {2,5,0};
        list.add(d8);

        double[] d9 = {1,5,0};
        list.add(d9);

        double[] d10 = {2,2,0};
        list.add(d10);

        drawCoordinatenSys(list);

        return list;
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

                if(node.checked) continue;

                d = next.distance2(node.x, next.x, 3);

                if (d <= min_d && isCloserToEnd(node,next,end)) {
                        min_d = d;
                        bestNode = node;
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

        drawCoordinatenSys(list);

        return list;
    }

    private static ArrayList<double[]> zweiDList(int num) {
        ArrayList<double[]> list = new ArrayList<double[]>();

        for (int i = 0; i < num; i++) {

            double[] d = { Math.floor(-10.0 + r.nextDouble() * 20.0 ) / 1, Math.floor(-10.0 + r.nextDouble() * 20.0 ) / 1 , 0};

            System.out.println(d[0] + ", " + d[1] + ", " + d[2]);

            list.add(d);
        }

        drawCoordinatenSys(list);

        return list;
    }

    private static ArrayList<double[]> randomList(int num){
        ArrayList<double[]> list = new ArrayList<double[]>();

        for (int i = 0; i < num; i++) {

            /*double[] d = {
                    Math.floor((num * -1) + r.nextDouble() * (num * 2)) / 1,
                    Math.floor((num * -1) + r.nextDouble() * (num * 2)) / 1,
                    Math.floor((num * -1) + r.nextDouble() * (num * 2)) / 1
            };
*/
            double[] d = {
                    (num * -1) + r.nextDouble() * (num * 2),
                    (num * -1) + r.nextDouble() * (num * 2),
                    (num * -1) + r.nextDouble() * (num * 2)
            };

            System.out.println(i +": "+d[0] + ", " + d[1] + ", " + d[2]);

            list.add(d);
        }

        return list;
    }

}
