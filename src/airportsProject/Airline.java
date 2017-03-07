package airportsProject;

import java.util.Vector;

public class Airline {

    private String name;
    private String nationality;
    private int numPlanes;
    private Vector airplanes;

    public Airline(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
        this.numPlanes = 0;
        this.airplanes = null;
    }

    public void addPlane(Airplane newPlane) {
    }

    public boolean removePlane( Airplane plane) {
    return false;
  }

    public int fleet() {
    return 0;
  }

    public String getName() {
    return name;
  }

    public void setName(String name) {
    this.name = name;
  }

    public String getNationality() {
    return nationality;
  }

    public void setNationality(String nationality) {
    this.nationality = nationality;
  }

    public int getNumPlanes() {
    return numPlanes;
  }

    public void setNumPlanes(int numPlanes) {
    this.numPlanes = numPlanes;
  }

    /**
     *
     * @element-type Airplane
     */
    public Vector getAirplanes() {
    return airplanes;
  }

    public void setAirplanes(Vector airplanes) {
    this.airplanes = airplanes;
  }

    @Override
    public String toString() {
        return "Airline{" +
                "name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", numPlanes=" + numPlanes +
                '}';
    }
}