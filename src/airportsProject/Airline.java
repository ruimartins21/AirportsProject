package airportsProject;

import libs.RedBlackBST;

public class Airline {

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

    public RedBlackBST<Integer, Airplane> getFleet() {
        return airplanes;
    }

    @Override
    public String toString() {
        return "Airline{" +
                "name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}