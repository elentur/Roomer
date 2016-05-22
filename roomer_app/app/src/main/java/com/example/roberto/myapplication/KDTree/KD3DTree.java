/**
 * Created by
 * roberto on 21.05.16.
 *
 * From: http://www.sanfoundry.com/java-program-find-location-point-placed-three-dimensions-using-k-d-trees/
 *
 * This is a Java Program to implement 3D KD Tree and Search an element. In computer science, a k-d tree (short for k-dimensional tree)
 * is a space-partitioning data structure for organizing points in a k-dimensional space. k-d trees are a useful data structure for several
 * applications, such as searches involving a multidimensional search key (e.g. range searches and nearest neighbor searches). k-d trees
 * are a special case of binary space partitioning trees.
 */

package com.example.roberto.myapplication.KDTree;

import java.util.ArrayList;

/**
 * Max 2000000 nodes
 */
public class KD3DTree {

    // the first node
    private KD3DNode Root;

    private int TimeStart, TimeFinish;
    private int CounterFreq;
    private double d_min;

    private KD3DNode nearest_neighbour;

    private int KD_id;
    private int nList;


    private KD3DNode CheckedNodes[];
    private int checked_nodes;
    private KD3DNode List[];


    private double x_min[], x_max[];
    private boolean max_boundary[], min_boundary[];
    private int n_boundary;

    /**
     * Creates a empty KD3DTree and sets all parameters
     * @param i int > number of elements of the KD3DTree
     */
    public KD3DTree(int i) {
        Root = null;
        KD_id = 1;
        nList = 0;
        List = new KD3DNode[i];
        CheckedNodes = new KD3DNode[i];
        max_boundary = new boolean[3];
        min_boundary = new boolean[3];
        x_min = new double[3];
        x_max = new double[3];
    }

    /**
     * Adds a new node in the tree.
     * @param x double Array with x, y and z coordinates
     * @return boolean if the node is set
     */
    public boolean add(double[] x) {

        if (nList >= 2000000 - 1)
            return false; // can't add more points

        // is it the first node
        if (Root == null) {
            Root = new KD3DNode(x, 0);
            Root.id = KD_id++;
            List[nList++] = Root;
        } else {
            KD3DNode pNode;
            if ((pNode = Root.Insert(x)) != null) {
                pNode.id = KD_id++;
                List[nList++] = pNode;
            }
        }

        return true;
    }

    /**
     * finds the nearest node of the given node
     * @param x double Array with x, y and z coordinates
     * @return KD3DNode. Can be null!
     */
    public KD3DNode find_nearest(double[] x) {
        if (Root == null)
            return null;

        checked_nodes = 0;
        KD3DNode parent = Root.FindParent(x);
        nearest_neighbour = parent;
        d_min = Root.distance2(x, parent.x, 3);

        if (parent.equal(x, parent.x, 3))
            return nearest_neighbour;

        search_parent(parent, x);
        uncheck();

        return nearest_neighbour;
    }

    /**
     * Checks the subtrees of a note if there is a smaller distends between this and a other node
     * @param node KD3DNode which contains the sub tree
     * @param x double Array with x, y and z coordinates
     */
    private void check_subtree(KD3DNode node, double[] x) {
        if ((node == null) || node.checked)
            return;

        CheckedNodes[checked_nodes++] = node;
        node.checked = true;
        set_bounding_cube(node, x);

        int dim = node.axis;
        double d = node.x[dim] - x[dim];

        if (d * d > d_min) {
            if (node.x[dim] > x[dim])
                check_subtree(node.Left, x);
            else
                check_subtree(node.Right, x);
        } else {
            check_subtree(node.Left, x);
            check_subtree(node.Right, x);
        }

    }

    /**
     * Finds the nearest neighbour using bounding cube.
     * Sets nearest_neighbour and d_min
     * @param node KD3DNode
     * @param x double Array with x, y and z coordinates
     */
    private void set_bounding_cube(KD3DNode node, double[] x) {

        if (node == null)
            return;

        int d = 0;
        double dx;
        for (int k = 0; k < 3; k++) {
            dx = node.x[k] - x[k];
            if (dx > 0) {
                dx *= dx;
                if (!max_boundary[k]) {
                    if (dx > x_max[k])
                        x_max[k] = dx;
                    if (x_max[k] > d_min) {
                        max_boundary[k] = true;
                        n_boundary++;
                    }
                }

            } else {
                dx *= dx;

                if (!min_boundary[k]) {
                    if (dx > x_min[k])
                        x_min[k] = dx;
                    if (x_min[k] > d_min) {
                        min_boundary[k] = true;
                        n_boundary++;
                    }
                }
            }

            d += dx;
            if (d > d_min)
                return;
        }

        if (d < d_min) {
            d_min = d;
            nearest_neighbour = node;
        }
    }

    /**
     * Finds the parent node of a given node inside a bounding cube
     * @param parent KD3DNode
     * @param x double Array with x, y and z coordinates
     * @return KD3DNode
     */
    private KD3DNode search_parent(KD3DNode parent, double[] x) {

        for (int k = 0; k < 3; k++) {
            x_min[k] = x_max[k] = 0;
            max_boundary[k] = min_boundary[k] = false;
        }

        n_boundary = 0;
        KD3DNode search_root = parent;

        while (parent != null && (n_boundary != 3 * 3)) {

            check_subtree(parent, x);
            search_root = parent;
            parent = parent.Parent;
        }

        return search_root;
    }

    /**
     * unchecks all nodes in the CheckedNodes list from zero to the checked_nodes
     */
    private void uncheck() {
        for (int n = 0; n < checked_nodes; n++)
            CheckedNodes[n].checked = false;
    }

    /**
     * Gives back a List of KD3DNode ordered like the KD-Tree
     * @return ArrayList<KD3DNode>
     */
    public ArrayList<KD3DNode> inorder() {

        ArrayList<KD3DNode> list = new ArrayList<KD3DNode>();

        return inorder(Root, list);
    }

    /**
     * Starts a recursive order of the kd-tree
     * @param root KD3DNode root node
     * @param list ArrayList<KD3DNode> to fill with
     * @return ArrayList<KD3DNode>
     */
    private ArrayList<KD3DNode> inorder(KD3DNode root, ArrayList<KD3DNode> list) {

        if(root != null) {

            inorder(root.Left, list);

            list.add(root);

            inorder(root.Right, list);
        }

        return list;
    }

    /**
     * Gives back a List of KD3DNode ordered from the beginning of the KD-Tree
     * @return ArrayList<KD3DNode>
     */
    public ArrayList<KD3DNode>  preorder() {
        ArrayList<KD3DNode> list = new ArrayList<KD3DNode>();

        return preorder(Root,list);
    }

    /**
     * Starts a recursive order of the kd-tree
     * @param root KD3DNode root node
     * @param list ArrayList<KD3DNode> to fill with
     * @return ArrayList<KD3DNode>
     */
    private ArrayList<KD3DNode>  preorder(KD3DNode root, ArrayList<KD3DNode> list) {
        if (root != null) {
            list.add(root);

            inorder(root.Left,list);
            inorder(root.Right,list);
        }

        return list;
    }

    /**
     * Gives back a List of KD3DNode ordered from the ending of the KD-Tree
     * @return ArrayList<KD3DNode>
     */
    public ArrayList<KD3DNode>  postorder() {
        ArrayList<KD3DNode> list = new ArrayList<KD3DNode>();
        return postorder(Root,list);
    }

    /**
     * Starts a recursive order of the kd-tree
     * @param root KD3DNode root node
     * @param list ArrayList<KD3DNode> to fill with
     * @return ArrayList<KD3DNode>
     */
    private ArrayList<KD3DNode>  postorder(KD3DNode root, ArrayList<KD3DNode> list) {
        if (root != null) {
            inorder(root.Left,list);
            inorder(root.Right,list);

            list.add(root);
        }

        return list;
    }

    /**
     * Checks, if a specific node is in the tree
     * @param x double coordinate
     * @param y double coordinate
     * @param z double coordinate
     * @return boolean
     */
    public boolean search(double x, double y, double z) {
        return search(Root, x, y, z);
    }

    /**
     * Starts a recursive search of the kd-tree
     * @param root KD3DNode root node
     * @param x double coordinate
     * @param y double coordinate
     * @param z double coordinate
     * @return boolean
     */
    private boolean search(KD3DNode root, double x, double y, double z) {

        if (root != null) {
            search(root.Left, x, y, z);
            if (x == root.x[0] && y == root.x[1] && z == root.x[2]) {
                return true;
            }
            search(root.Right, x, y, z);
        }
        return false;
    }
}