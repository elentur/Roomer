package com.example.roberto.myapplication.model;

import com.example.roberto.myapplication.KDTree.KD3DNode;
import com.example.roberto.myapplication.KDTree.KD3DTree;
import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;

/**
 * Created by
 * @author roberto on 21.05.16.
 */
public class PointsRoutModel {

    private ArrayList<Vector3> pointList;

    private KD3DTree kdt;

    public PointsRoutModel(int id) {
        searchPointListById(id);
    }

    public PointsRoutModel(String name) {
        searchPointListByName(name);
    }

    public PointsRoutModel(ArrayList<Vector3> pointList) {
        if(pointList == null || pointList.size() < 1) throw new IllegalArgumentException("The PointList cannot be empty!");

        this.pointList = pointList;
        buildKDTree();
    }

    private void searchPointListById(int id) {
        //TODO creating a Database query to the pointList by Id
    }

    private void searchPointListByName(String name) {
        //TODO creating a Database query to the pointList by Name
    }

    private void buildKDTree(){

        int numpoints = pointList.size();

        kdt = new KD3DTree(numpoints + 1);

        /*double zero[] = {0,0,0};

        kdt.add(zero);*/

        for(Vector3 point : pointList){

            double p[] = {point.x,point.y,point.z};

            kdt.add(p);
        }
    }

    public ArrayList<Vector3> listInOrder(){

        ArrayList<Vector3> inOrderList = new ArrayList<Vector3>();

        ArrayList<KD3DNode> list = kdt.inorder();

        for(KD3DNode d : list){
            Vector3 vec = new Vector3(d.x[0],d.x[1],d.x[2]);
            inOrderList.add(vec);
        }

        return inOrderList;
    }

    public ArrayList<Vector3> listPreOrder(){
        ArrayList<Vector3> preOrderList = new ArrayList<Vector3>();

        ArrayList<KD3DNode> list = kdt.preorder();

        for(KD3DNode d : list){
            Vector3 vec = new Vector3(d.x[0],d.x[1],d.x[2]);
            preOrderList.add(vec);
        }

        return preOrderList;
    }

    public ArrayList<Vector3> listPostOrder(){
        ArrayList<Vector3> postOrderList = new ArrayList<Vector3>();

        ArrayList<KD3DNode> list = kdt.postorder();

        for(KD3DNode d : list){
            Vector3 vec = new Vector3(d.x[0],d.x[1],d.x[2]);
            postOrderList.add(vec);
        }

        return postOrderList;
    }

    public Vector3 searchInList(Vector3 vec){

        kdt.search(vec.x,vec.y,vec.z);

        return new Vector3();
    }

    public ArrayList<Vector3> getShortestWay(Vector3 start, Vector3 end){

        ArrayList<Vector3> way = new ArrayList<Vector3>();
        ArrayList<KD3DNode> list = kdt.inorder();

        // find the nearest Nodes - start and end do not have to be in the list
        double[] startPoint = {start.x,start.y,start.z};
        KD3DNode startNode = kdt.find_nearest(startPoint);
        double[] endPoint = {end.x,end.y,end.z};
        KD3DNode endNode = kdt.find_nearest(endPoint);


        KD3DNode nextNode = startNode;
        KD3DNode bestNode = endNode;

        do {

            double dist = startNode.distance2(startNode.x, endNode.x, 3);

            for (KD3DNode node : list) {

                double distance = nextNode.distance2(nextNode.x, node.x, 3);
                if (distance < dist && !node.checked) {
                    if(isCloserToEnd(node,nextNode,endNode)) {
                        dist = distance;
                        bestNode = node;
                    }
                }
            }

            bestNode.checked = true;
            way.add(new Vector3(bestNode.x[0],bestNode.x[1],bestNode.x[2]));
            nextNode = bestNode;

        }while(!nextNode.equal(nextNode.x,endNode.x,3));

        return way;
    }

    private static boolean isCloserToEnd(KD3DNode node, KD3DNode next, KD3DNode end) {

        if(end.equal(end.x,next.x,3)) return true;

        double distNode = end.distance2(node.x,end.x,3);
        double distNext = end.distance2(next.x,end.x,3);

        return distNode < distNext;
    }

    @Override
    public String toString() {
        return "PointsRoutModel{" +
                "pointList=" + pointList +
                ", kdt=" + kdt +
                '}';
    }
}
