package com.example.roberto.myapplication.model;

import com.example.roberto.myapplication.KDTree.KD3DNode;
import com.example.roberto.myapplication.KDTree.KD3DTree;
import org.rajawali3d.math.vector.Vector3;

import java.util.ArrayList;

/**
 * Modal-Object of the Vector3 Point Data.
 * Creates a KD-Tree and can be use to get ordered lists, search Vector3 Point in list and find shortest way
 * Created by
 * @author roberto on 21.05.16.
 */
public class PointsRoutModel {

    // the list of raw Vector3 points
    private ArrayList<Vector3> pointList;

    // KD-tree
    private KD3DTree kdt;

    /**
     * Gets the Modal from database by id
     * @param id database id
     */
    public PointsRoutModel(int id) {
        searchPointListById(id);
    }

    /**
     * Gets the Modal from database by name
     * @param name modal name
     */
    public PointsRoutModel(String name) {
        searchPointListByName(name);
    }

    /**
     * Starts Model with a given ector3 points list
     * @param pointList list of Vector3 points
     */
    public PointsRoutModel(ArrayList<Vector3> pointList) {
        if(pointList == null || pointList.size() < 1) throw new IllegalArgumentException("The PointList cannot be empty!");

        this.pointList = pointList;
        buildKDTree();
    }

    /**
     * Has to be implemented
     * @param id
     */
    private void searchPointListById(int id) {
        //TODO creating a Database query to the pointList by Id
    }

    /**
     * Has to be implemented
     * @param name
     */
    private void searchPointListByName(String name) {
        //TODO creating a Database query to the pointList by Name
    }

    /**
     * Creates the KD-Tree based of the given Vector3 point list.
     */
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

    /**
     * Gives back a List of Vector3 Vector3 like the KD-Tree
     * @return ArrayList<Vector3>
     */
    public ArrayList<Vector3> listInOrder(){

        ArrayList<Vector3> inOrderList = new ArrayList<Vector3>();

        ArrayList<KD3DNode> list = kdt.inorder();

        for(KD3DNode d : list){
            Vector3 vec = new Vector3(d.x[0],d.x[1],d.x[2]);
            inOrderList.add(vec);
        }

        return inOrderList;
    }

    /**
     * Gives back a List of Vector3 ordered from the beginning of the KD-Tree
     * @return ArrayList<Vector3>
     */
    public ArrayList<Vector3> listPreOrder(){
        ArrayList<Vector3> preOrderList = new ArrayList<Vector3>();

        ArrayList<KD3DNode> list = kdt.preorder();

        for(KD3DNode d : list){
            Vector3 vec = new Vector3(d.x[0],d.x[1],d.x[2]);
            preOrderList.add(vec);
        }

        return preOrderList;
    }

    /**
     * Gives back a List of Vector3 ordered from the ending of the KD-Tree
     * @return ArrayList<Vector3>
     */
    public ArrayList<Vector3> listPostOrder(){
        ArrayList<Vector3> postOrderList = new ArrayList<Vector3>();

        ArrayList<KD3DNode> list = kdt.postorder();

        for(KD3DNode d : list){
            Vector3 vec = new Vector3(d.x[0],d.x[1],d.x[2]);
            postOrderList.add(vec);
        }

        return postOrderList;
    }

    /**
     * Checks, if a specific Vector3 is in the tree
     * @param vec searched Vector3
     * @return Vector3
     */
    public Vector3 searchInList(Vector3 vec){

        kdt.search(vec.x,vec.y,vec.z);

        return new Vector3();
    }

    /**
     * gives a Vector3 list of the shortest way between start and end.
     * start and end are included in the list!
     * @param start beginning of the way
     * @param end ending of the way
     * @return ArrayList<Vector3>
     */
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

            double d_min = startNode.distance2(startNode.x, endNode.x, 3);

            for (KD3DNode node : list) {

                if(node.checked) continue;

                double d = nextNode.distance2(nextNode.x, node.x, 3);

                if (d <= d_min && isCloserToEnd(node,nextNode,endNode)) {
                        d_min = d;
                        bestNode = node;
                }
            }

            bestNode.checked = true;
            way.add(new Vector3(bestNode.x[0],bestNode.x[1],bestNode.x[2]));
            nextNode = bestNode;

        }while(!nextNode.equal(nextNode.x,endNode.x,3));

        return way;
    }

    /**
     * Checks if the first note is closer to end than the second
     * @param first Node
     * @param second Node
     * @param end Node
     * @return boolean
     */
    private static boolean isCloserToEnd(KD3DNode first, KD3DNode second, KD3DNode end) {

        if(end.equal(end.x,second.x,3)) return true;

        double distNode = end.distance2(first.x,end.x,3);
        double distNext = end.distance2(second.x,end.x,3);

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
