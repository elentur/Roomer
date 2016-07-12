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
    private String uuid;
    private Building building;

    public ADF() {
    }

    public ADF(long id, Vector3 position, String name,String uuid, Building building) {
        this.id = id;
        this.position = position;
        this.name = name;
        this.uuid = uuid;
        this.building = building;
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
