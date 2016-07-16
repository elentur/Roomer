package com.projecttango.DataStructure;

/**
 * This class represents an Building object. It saves its idand its name.
 * Created by
 * Roberto on 04.07.16.
 */
public class Building {
    /**
     * buildung id for the database
     */
    private long id;
    /**
     * building name
     */
    private String name;

    /**
     * Creates a object of Building
     */
    public Building(){
    }

    /**
     * Creates a object of Building
     * @param id of the building for the database
     * @param name of the building
     */
    public Building(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     *
     * GETTER AND SETTER
     */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Building building = (Building) o;

        if (id != building.id) return false;
        return name != null ? name.equals(building.name) : building.name == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
