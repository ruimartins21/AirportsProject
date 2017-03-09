package airportsProject;

import java.util.ArrayList;

public class Airline {

    private String name;
    private String nationality;
    private ArrayList<Airplane> airplanes = new ArrayList<>();

    public Airline(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
    }

    public void addPlane(Airplane newPlane) {
        this.airplanes.add(newPlane);
    }

    public Airplane removePlane( Airplane plane) {
        return this.airplanes.remove(this.airplanes.indexOf(plane));
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    public ArrayList<Airplane> getFleet() {
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