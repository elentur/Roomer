package com.projecttango.DataStructure;

import org.rajawali3d.math.vector.Vector3;

/**
 * This class represents an ADF object. It saves its id, a position regarded to its neighbour,
 * name and UUID given by Tango. Also ut knows his building.
 * Created by
 * Roberto on 04.07.16.
 */
public class ADF {
    /**
     * id for the database
     */
    private long id;
    /**
     * position regarded to its neighbour ADF
     */
    private Vector3 position;
    /**
     * name of the ADF object
     */
    private String name;
    /**
     * UUDI from tango
     */
    private String uuid;
    /**
     * a building it belongs to
     */
    private Building building;

    /**
     * Creates a object of ADF
     */
    public ADF() {
    }

    /**
     * Creates a object of ADF
     * @param id for the database
     * @param position regarded to its neighbour ADF in Vector3 format
     * @param name of the ADF
     * @param uuid of the ADF
     * @param building it belongs to
     */
    public ADF(long id, Vector3 position, String name,String uuid, Building building) {
        this.id = id;
        this.position = position;
        this.name = name;
        this.uuid = uuid;
        this.building = building;
    }

    /**
     * GETTER AND SETTER
     */

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    @Override
    public String toString() {
        return "ADF{" +
                "id=" + id +
                ", position=" + position +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ADF adf = (ADF) o;

        if (id != adf.id) return false;
        if (!position.equals(adf.position)) return false;
        if (!name.equals(adf.name)) return false;
        if (!uuid.equals(adf.uuid)) return false;
        return building.equals(adf.building);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + position.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + uuid.hashCode();
        result = 31 * result + building.hashCode();
        return result;
    }
}
