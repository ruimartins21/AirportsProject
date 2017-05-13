package airportsProject;

import libs.RedBlackBST;

import java.io.Serializable;

/**
 * The airline class represents an actual airline that owns airplanes and does their management:
 * Insert/Removing airplanes
 * It also gives information on its fleet, how many airplanes a company has
 */
public class Airline implements Serializable {

    private String name;
    private String nationality;
    private RedBlackBST<Integer, Airplane> airplanes = new RedBlackBST<>();

    public Airline(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
    }

    public void addPlane(Airplane newPlane) {
        this.airplanes.put(newPlane.getId(), newPlane);
    }

    public void removePlane(Airplane plane) {
        this.airplanes.put(plane.getId(), null);
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public RedBlackBST<Integer, Airplane> getFleet() {
        return airplanes;
    }

    public void setFleet(RedBlackBST<Integer, Airplane> fleet){
        this.airplanes = fleet;
    }

    @Override
    public String toString() {
        return "Airline{" +
                "name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}