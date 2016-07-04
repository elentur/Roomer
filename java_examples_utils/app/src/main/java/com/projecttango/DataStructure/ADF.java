package com.projecttango.DataStructure;

import org.rajawali3d.math.vector.Vector3;

/**
 * Created by
 * Roberto on 04.07.16.
 */
public class ADF {
    private long id;
    private Vector3 position;
    private String name;

    public ADF() {
    }

    public ADF(long id, Vector3 position, String name) {
        this.id = id;
        this.position = position;
        this.name = name;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ADF{" +
                "id=" + id +
                ", position=" + position +
                ", name='" + name + '\'' +
                '}';
    }
}
