package com.example.roberto.myapplication.KDTree;

import java.util.Arrays;

/**
 * A KD3DNode Object which contains its coordinates, axis (dimension),
 * parent node and the left and right knot.
 * When it is added, it takes its position autonomously in tree.
 *
 */
public class KD3DNode {

    // Dimension
    int axis;
    // x y z values
    public double[] x;
    // Obj id
    int id;
    // Checkes if we already have been here
    public boolean checked;
    // left or right
    boolean orientation;
    // Parent Node
    KD3DNode Parent;
    // Left knot
    KD3DNode Left;
    // Right knot
    KD3DNode Right;

    /**
     * Constructor which creates a KD3DNode according the given
     * coordinate and the axis of the KD 3 Tree.
     * @param x0 double Array with x, y and z coordinates
     * @param axis0 int which represents the axis in the KD Tree and shows of
     *              the position of the double Array can be 1 or 2 or 3
     */
    KD3DNode(double[] x0, int axis0) {
        x = new double[3];
        axis = axis0;
        System.arraycopy(x0, 0, x, 0, 3);
        Left = Right = Parent = null;
        checked = false;
        id = 0;
    }

    /**
     * Finds the parent node of the given node.
     * @param x0 double Array with x, y and z coordinates
     * @return KD3DNode parent node. Can be null!
     */
    KD3DNode FindParent(double[] x0) {

        KD3DNode parent = null;
        KD3DNode next = this;
        // direction where to go in the tree
        int split;

        while (next != null) {
            split = next.axis;
            parent = next;

            // is x0 bigger wo go to the right side
            if (x0[split] > next.x[split])
                next = next.Right;
            else
                next = next.Left;
        }

        return parent;
    }

    /**
     * Insert a node on the correct position. The node is being discarded if already exists.
     * @param p double Array with x, y and z coordinates
     * @return KD3DNode. Can be null!
     */
    KD3DNode Insert(double[] p) {
        x = new double[3];
        KD3DNode parent = FindParent(p);

        // are node and parent the same?
        if (equal(p, parent.x, 3))
            return null;

        // creating a new node
        KD3DNode newNode = new KD3DNode(p, parent.axis + 1 < 3 ? parent.axis + 1 : 0);
        newNode.Parent = parent;

        // putting on the left or right knot of the parent
        if (p[parent.axis] > parent.x[parent.axis]) {
            parent.Right = newNode;
            newNode.orientation = true;
        } else {
            parent.Left = newNode;
            newNode.orientation = false;
        }

        return newNode;
    }

    /**
     * Checks if two nodes are the same.
     * @param x1 first double array with x, y and z coordinates
     * @param x2 second double array with x, y and z coordinates
     * @param dim which dimension have to be checked.
     * @return boolean
     */
    public boolean equal(double[] x1, double[] x2, int dim) {
        for (int k = 0; k < dim; k++) {
            if (x1[k] != x2[k])
                return false;
        }

        return true;
    }

    /**
     * Calculates the distance between two nodes.
     * @param x1 first double array with x, y and z coordinates
     * @param x2 second double array with x, y and z coordinates
     * @param dim which dimension have to be checked.
     * @return double which contains the distance.
     */
    public double distance2(double[] x1, double[] x2, int dim) {

        double S = 0;
        for (int k = 0; k < dim; k++)
            S += (x1[k] - x2[k]) * (x1[k] - x2[k]);

        return S;
    }

    @Override
    public String toString() {

        String parent = Parent != null ? Arrays.toString(Parent.x) : "null";
        String left = Left != null ? Arrays.toString(Left.x) : "null";
        String right = Right != null ? Arrays.toString(Right.x) : "null";

        return "KD3DNode{" +
                "axis=" + axis +
                ", x=" + Arrays.toString(x) +
                ", id=" + id +
                ", checked=" + checked +
                ", orientation=" + orientation +
                ", Parent=" + parent +
                ", Left=" + left +
                ", Right=" + right +
                '}';
    }
}