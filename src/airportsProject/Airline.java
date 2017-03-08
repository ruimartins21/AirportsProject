package airportsProject;

import java.util.ArrayList;

public class Airline {

    private String name;
    private String nationality;
    private int numPlanes;
    private ArrayList<Airplane> airplanes = new ArrayList<>();

    public Airline(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
        this.numPlanes = 0;
    }

    public Airline addPlane(Airplane newPlane) {
        this.airplanes.add(newPlane);
        this.numPlanes += 1;
        return this;
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
    public ArrayList<Airplane> getAirplanes() {
    return airplanes;
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