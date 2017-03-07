package airportsProject;

import edu.princeton.cs.algs4.RedBlackBST;

import java.util.Vector;

public class Airport {


    private String name;
    private String code;
    private String city;
    private String country;
    private String continent;
    private float rating;
//    private RedBlackBST<Integer, String> logs = new RedBlackBST<>();
    private Vector  airplanes;
    private Vector  flights;

    public Airport(String name, String code, String city, String country, String continent, float rating) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.country = country;
        this.continent = continent;
        this.rating = rating;
        this.airplanes = null;
        this.flights = null;
    }

    public void receivePlane(Airplane newPlane) {
    }

    public int numberOfPlanes() {
  return 0;
  }

    public Date sendPlane( Airplane plane) {
  return null;
  }

    public boolean remove() {
  return false;
  }

    public String getName() {
    return name;
  }

    public void setName(String name) {
    this.name = name;
  }

    public String getCode() {
    return code;
  }

    public void setCode(String code) {
    this.code = code;
  }

    public String getCity() {
    return city;
  }

    public void setCity(String city) {
    this.city = city;
  }

    public String getCountry() {
    return country;
  }

    public void setCountry(String country) {
    this.country = country;
  }

    public String getContinent() {
    return continent;
  }

    public void setContinent(String continent) {
    this.continent = continent;
  }

    public float getRating() {
    return rating;
  }

    public void setRating(float rating) {
    this.rating = rating;
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

    /**
    *
    * @element-type Flight
    */
    public Vector getFlights() {
    return flights;
  }

    public void setFlights(Vector flights) {
    this.flights = flights;
  }

    @Override
    public String toString() {
        return "Airport{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", continent='" + continent + '\'' +
                ", rating=" + rating +
                '}';
    }
}